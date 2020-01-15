package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.WorkstationTypeService;
import io.apj.modules.sys.service.SysDeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.modules.masterData.dao.WorkstationDao;
import io.apj.modules.masterData.service.ModelSeriesService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.ModelWorkstationRelaService;
import io.apj.modules.masterData.service.WorkstationService;
import org.springframework.transaction.annotation.Transactional;

@Service("workstationService")
public class WorkstationServiceImpl extends ServiceImpl<WorkstationDao, WorkstationEntity>
		implements WorkstationService {

	@Autowired
	private WorkstationTypeService workstationTypeService;
	@Autowired
	private ModelWorkstationRelaService modelWorkstationRelaService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ModelSeriesService modelSeriesService;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private WorkstationService workstationService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<WorkstationEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("remark") != null && params.get("remark") != "","remark", (String) params.get("remark"))
//				.like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"))
				.like(params.get("keyWord") != null && params.get("keyWord") != "", "name",
						(String) params.get("keyWord"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"UPPER(pinyin) like '%" + ((String) params.get("name")).toUpperCase() + "%' " + "or UPPER(name) like '%" + ((String) params.get("name")).toUpperCase() + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("createBy"))) {
			entityWrapper.eq("create_By", Integer.parseInt((String) params.get("createBy")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
			entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
		}
		//根据机种查找对应的工位
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			EntityWrapper<ModelWorkstationRelaEntity> workstationRelaWrapper = new EntityWrapper<>();
			workstationRelaWrapper.eq("model_id",  Integer.parseInt(params.get("modelId").toString())).isNotNull("delete_at");
			List<ModelWorkstationRelaEntity>  workstationRelaList= modelWorkstationRelaService.selectList(workstationRelaWrapper);
			List<Integer> workstationIds = new ArrayList<>();
			if(workstationRelaList != null && workstationRelaList.size() > 0){
				for(ModelWorkstationRelaEntity entity : workstationRelaList){
					workstationIds.add(entity.getWorkstationId());
				}
			}
			
			if(workstationIds.size()>0){
				entityWrapper.in("id", workstationIds);
			}
			
			//entityWrapper.eq("modelId", Integer.parseInt((String) params.get("updateBy")));
		}
		Page<WorkstationEntity> page = this.selectPage(new Query<WorkstationEntity>(params).getPage(), entityWrapper);
		for(WorkstationEntity entity : page.getRecords()){
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}

		return new PageUtils(page);
	}

	/**
	 * 判断工位是否SUB
	 * 
	 * @param wookStationId
	 * @return
	 */
	public Boolean wookStationIdIsSub(Integer wookStationId) {
		WorkstationEntity workstationEntity = selectById(wookStationId);
		if (workstationEntity.getWorkstationTypeId() > 0) {
			WorkstationTypeEntity workstationType = workstationTypeService
					.selectById(workstationEntity.getWorkstationTypeId());
			Boolean bool = workstationType == null ? false
					: workstationType.getName() == null ? false
							: workstationType.getName().toLowerCase().contains("sub");
			return bool;
		} else {
			return false;
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<WorkstationEntity> workstationEntities = this.selectBatchIds(ids);
		for (WorkstationEntity item : workstationEntities) {
			item.setDeleteAt(new Date());
		}
		if (workstationEntities.size() > 0) {
			this.updateAllColumnBatchById(workstationEntities);
		}

	}

	@Override
	public void deleteByWrapper(Wrapper<WorkstationEntity> wrapper) {
		List<WorkstationEntity> workstationEntityList = this.selectList(wrapper);
		for (WorkstationEntity item : workstationEntityList) {
			item.setDeleteAt(new Date());
		}
		if (workstationEntityList.size() > 0) {
			this.updateAllColumnBatchById(workstationEntityList);
		}

	}

	@Override
	public void updatePinAndDataById(WorkstationEntity workstationEntity) {
		workstationEntity.setPinyin(PinyinUtil.getPinYin(workstationEntity.getName()));
		workstationEntity.setUpdateAt(new Date());
		this.updateById(workstationEntity);
	}

	@Override
	public PageUtils modelDetailList(Integer id, Map<String, Object> params) {
		EntityWrapper<ModelWorkstationRelaEntity> relaWrapper = new EntityWrapper<>();
		relaWrapper.isNull("delete_at").eq("workstation_id", id);
		List<ModelWorkstationRelaEntity> relaList = modelWorkstationRelaService.selectList(relaWrapper);
		List<Integer> modelIDs = new ArrayList<>();
		Map<Integer, Integer> relaIdMap = new HashMap<Integer, Integer>();
		for (ModelWorkstationRelaEntity item : relaList) {
			modelIDs.add(item.getModelId());
			relaIdMap.put(item.getModelId(), item.getId());
		}
		modelIDs.add(0);
		EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").in("id", modelIDs)
				.like(params.get("remark") != null && params.get("remark") != "", "remark", (String) params.get("remark"))
		.like(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"));

		if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
			entityWrapper.eq("dept_id",Integer.parseInt((String) params.get("deptId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelSeriesId"))) {
			entityWrapper.eq("model_series_id", Integer.parseInt((String) params.get("modelSeriesId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"name  like '%" + params.get("name") + "%'" + " or pinyin  like '%" + params.get("name") + "%'");
		}
		Page<ModelEntity> page = modelService.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);
		for (ModelEntity entity : page.getRecords()) {
			entity.setModelSeriesEntity(modelSeriesService.selectById(entity.getModelSeriesId()));
			entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
			entity.setModelWorkStationRelaId(relaIdMap.get(entity.getId()));
		}
		PageUtils pageUtils = new PageUtils(page);
		pageUtils.setRelaName(workstationService.selectById(id).getName());
		return pageUtils;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> workstationImport(Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		for(Map<String, Object> i:maps ){
			if(StringUtils.isEmpty((CharSequence) i.get("workstation.name"))){
				return RD.INTERNAL_SERVER_ERROR("导入时工位名称名称为空，请检查工位名称是否为空");
			}
		}
		List<WorkstationEntity> workstationEntityList = new ArrayList<>();
		List<String> modelNameList = new ArrayList<>();
		List<String> workStationNameList = new ArrayList<String>();
		List<ModelWorkstationRelaEntity> modelWorkstationRelaEntityList = new ArrayList<>();
		// 遍历所有导入数据，插入工位
		for (int i = 0; i < maps.size(); i++) {
			WorkstationEntity workstationEntity = new WorkstationEntity();
			Map<String, Object> WorkstationMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				if (keyStrs[0].equals("workstation")) {
					if (keyStrs[1].equals("name")) {
						if (!workStationNameList.contains(value)) {
							workStationNameList.add((String) value);
						}
					}
					WorkstationMap.put(keyStrs[1], value);
				}
				if (keyStrs[0].equals("model")) {
					if (keyStrs[1].equals("name")) {
						if (!modelNameList.contains(value)) {
							modelNameList.add((String) value);
						}
					}
				}
			}
			DataUtils.transMap2Bean2(WorkstationMap, workstationEntity);
			ValidatorUtils.validateEntity(workstationEntity, i);
			workstationEntity.setPinyin(PinyinUtil.getPinYin(workstationEntity.getName()));
			workstationEntity.setCreateBy((Integer) map.get("userID"));
			workstationEntityList.add(workstationEntity);
		}
		try {
			this.insertBatch(workstationEntityList, Constant.importNum);
		} catch (Exception e) {
			throw new RRException("工位导入失败，请检查工位是否重复");
		}
		// 遍历所有本次导入所用到的机种
		Map<String, Integer> modelIDAndNameMap = new HashMap<String, Integer>();
		EntityWrapper<ModelEntity> modelWrapper = new EntityWrapper<>();
		modelWrapper.in("name", modelNameList).isNull("delete_at");
		List<ModelEntity> modelList = modelService.selectList(modelWrapper);
		for (ModelEntity item : modelList) {
			modelIDAndNameMap.put(item.getName(), item.getId());
		}
		Map<String, Integer> WorkstationIDAndNameMap = new HashMap<String, Integer>();
		EntityWrapper<WorkstationEntity> WorkstationWrapper = new EntityWrapper<>();
		WorkstationWrapper.in("name", workStationNameList).isNull("delete_at");
		List<WorkstationEntity> workstationEntityList1 = this.selectList(WorkstationWrapper);
		for (WorkstationEntity item : workstationEntityList1) {
			WorkstationIDAndNameMap.put(item.getName(), item.getId());
		}
		// 存储不存在的机种名称
		List<String> notExistModelName = new ArrayList<String>();
		// 关系表列表
		for (int i = 0; i < maps.size(); i++) {
			ModelWorkstationRelaEntity modelWorkstationRelaEntity = new ModelWorkstationRelaEntity();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("model.name")) {
					if (modelIDAndNameMap.get(value) != null) {
						modelWorkstationRelaEntity.setModelId(modelIDAndNameMap.get(value));
					} else {
						if (!notExistModelName.contains((String) value)) {
							notExistModelName.add((String) value);
						}
					}
				} else if (key.equals("workstation.name")) {
					modelWorkstationRelaEntity.setWorkstationId(WorkstationIDAndNameMap.get(value));
				}
			}
			modelWorkstationRelaEntityList.add(modelWorkstationRelaEntity);
		}
		modelWorkstationRelaService.insertBatch(modelWorkstationRelaEntityList);
		if (notExistModelName.size() > 0) {
			throw new RRException("机种" + notExistModelName.toString() + "不存在，请添加机种后再导入相关的数据");
		}
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("code", 200);
		return RD.success(data);

	}

}
