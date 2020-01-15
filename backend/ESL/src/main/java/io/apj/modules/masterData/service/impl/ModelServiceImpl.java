package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.dao.ModelDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelWorkstationRelaEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service("modelService")
public class ModelServiceImpl extends ServiceImpl<ModelDao, ModelEntity> implements ModelService {
	@Autowired
	private ModelSeriesService modelSeriesService;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private PartService partService;
	@Autowired
	private ModelToolRelaService modelToolRelaService;
	@Autowired
	private ModelWorkstationRelaService modelWorkStationRelaService;
	@Autowired
	private WorkstationService workStationService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ModelService modelService;

	@Override
	@DataFilter(subDept = true, user = true, deptId = "dept_id")
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"))
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
		if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
			entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelSeriesId"))) {
			entityWrapper.eq("model_series_id", Integer.parseInt((String) params.get("modelSeriesId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))) {
			String name = (String) params.get("keyWord");
			name = name.replace(",", "");
			entityWrapper.andNew("name  like '%" + name + "%'" + " or pinyin  like '%" + name + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			String name = (String) params.get("name");
			name = name.replace(",", "");
			entityWrapper.andNew("UPPER(name)  like '%" + name.toUpperCase() + "%'" + " or UPPER(pinyin)  like '%"
					+ name.toUpperCase() + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("workstationId"))) {
			EntityWrapper<ModelWorkstationRelaEntity> workstationRelaWrapper = new EntityWrapper<>();
			workstationRelaWrapper.eq("workstation_id",  Integer.parseInt(params.get("modelId").toString())).isNotNull("delete_at");
			List<ModelWorkstationRelaEntity>  workstationRelaList= modelWorkStationRelaService.selectList(workstationRelaWrapper);
			List<Integer> modelIds = new ArrayList<>();
			if(workstationRelaList != null && workstationRelaList.size() > 0){
				for(ModelWorkstationRelaEntity entity : workstationRelaList){
					modelIds.add(entity.getWorkstationId());
				}
			}
			
			if(modelIds.size()>0){
				entityWrapper.in("id", modelIds);
			}
		}
		
		Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);
		for (ModelEntity entity : page.getRecords()) {
			entity.setModelSeriesEntity(modelSeriesService.selectById(entity.getModelSeriesId()));
			entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}
		return new PageUtils(page);
	}

	@Override
	public PageUtils selectBySeriesId(int id, Map<String, Object> params) {
		EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
			entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("id"))) {
			entityWrapper.eq("model_series_id", Integer.parseInt((String) params.get("id")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("createBy"))) {
			entityWrapper.eq("create_By", Integer.parseInt((String) params.get("createBy")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
			entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"name  like '%" + params.get("name") + "%'" + " or pinyin  like '%" + params.get("name") + "%'");
		}
		Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);
		for (ModelEntity entity : page.getRecords()) {
			entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
		}
		PageUtils pageUtils = new PageUtils(page);
		pageUtils.setRelaName(modelSeriesService.selectById(id).getName());
		return pageUtils;
	}

	@Override
	public PageUtils modelPartRelaList(int id, Map<String, Object> params) {
		Page<Map<String, Object>> page = new Page<>(Integer.parseInt(params.get("page").toString()),
				Integer.parseInt(params.get("limit").toString()));
		String name = (String) params.get("name");
		String remark = (String) params.get("remark");
		Integer common = null;
		if (params.get("common") != null) {
			common = Integer.parseInt((String) params.get("common"));
		}
		page = page.setRecords(this.baseMapper.selectmodelPart(id, page, name, remark, common));
		PageUtils pageUtils = new PageUtils(page);
		pageUtils.setRelaName(modelService.selectById(id).getName());
		return pageUtils;

	}

	@Override
	public PageUtils modelToolRelaList(int id, Map<String, Object> params) {
		Page<Map<String, Object>> page = new Page<>(Integer.parseInt(params.get("page").toString()),
				Integer.parseInt(params.get("limit").toString()));
		String name = (String) params.get("name");
		String remark = (String) params.get("remark");
		Integer common = null;
		if (params.get("common") != null) {
			common = Integer.parseInt((String) params.get("common"));
		}
		page = page.setRecords(this.baseMapper.selectmodelTool(id, page, name, remark, common));
		PageUtils pageUtils = new PageUtils(page);
		pageUtils.setRelaName(modelService.selectById(id).getName());
		return pageUtils;

	}

	@Override
	public void deleteList(List<ModelEntity> modelEntityList) {
		for (ModelEntity item : modelEntityList) {
			item.setDeleteAt(new Date());
		}
		if (modelEntityList.size() > 0) {
			this.updateAllColumnBatchById(modelEntityList);
		}
	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<ModelEntity> modelEntityList = this.selectBatchIds(ids);
		for (ModelEntity item : modelEntityList) {
			item.setDeleteAt(new Date());
		}
		if (modelEntityList.size() > 0) {
			this.updateAllColumnBatchById(modelEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<ModelEntity> wrapper) {
		List<ModelEntity> modelEntityList = this.selectList(wrapper);
		for (ModelEntity item : modelEntityList) {
			item.setDeleteAt(new Date());
		}
		if (modelEntityList.size() > 0) {
			this.updateAllColumnBatchById(modelEntityList);
		}

	}

	@Override
	public void updatePinAndDataById(ModelEntity modelEntity) {
		modelEntity.setPinyin(PinyinUtil.getPinYin(modelEntity.getName()));
		modelEntity.setUpdateAt(new Date());
		this.updateById(modelEntity);
	}

	@Override
	public PageUtils modelWorkStationList(Integer id, Map<String, Object> params) {
		EntityWrapper<ModelWorkstationRelaEntity> relaWrapper = new EntityWrapper<>();
		relaWrapper.isNull("delete_at").eq("model_id", id);
		List<ModelWorkstationRelaEntity> relaList = modelWorkStationRelaService.selectList(relaWrapper);
		List<Integer> workStationIDs = new ArrayList<>();
		Map<Integer, Integer> relaIdMap = new HashMap<Integer, Integer>();
		for (ModelWorkstationRelaEntity item : relaList) {
			workStationIDs.add(item.getWorkstationId());
			relaIdMap.put(item.getWorkstationId(), item.getId());
		}
		workStationIDs.add(0);
		EntityWrapper<WorkstationEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").in("id", workStationIDs).like(
				params.get("remark") != null && params.get("remark") != "", "remark", (String) params.get("remark"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"name  like '%" + params.get("name") + "%'" + " or pinyin  like '%" + params.get("name") + "%'");
		}
		Page<WorkstationEntity> page = workStationService.selectPage(new Query<WorkstationEntity>(params).getPage(),
				entityWrapper);
		for (WorkstationEntity entity : page.getRecords()) {
			entity.setModelWorkStationRelaId(relaIdMap.get(entity.getId()));
		}
		PageUtils pageUtils = new PageUtils(page);
		pageUtils.setRelaName(modelService.selectById(id).getName());
		return pageUtils;
	}
}