package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.dao.PartDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelSeriesService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PartService;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Service("partService")
public class PartServiceImpl extends ServiceImpl<PartDao, PartEntity> implements PartService {
	@Autowired
	private ModelPartRelaService modelPartRelaService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private StaffService staffService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<PartEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.eq(params.get("common") != null && params.get("common") != "", "common",
						Boolean.parseBoolean((String) params.get("common")))
				.like(params.get("remark") != null && params.get("remark") != "", "remark",
						(String) params.get("remark"))
				.orderBy("update_at", false);
		if (params.get("name") != null && params.get("name") != "") {
			params.put("name", ((String) params.get("name")).replace('*', '%'));
			entityWrapper.andNew(
					"UPPER(pinyin) like '%" + ((String) params.get("name")).toUpperCase() + "%' " + "or UPPER(name) like '%" + ((String) params.get("name")).toUpperCase() + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("createBy"))) {
			entityWrapper.eq("create_By", Integer.parseInt((String) params.get("createBy")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
			entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
		}
		Page<PartEntity> page = this.selectPage(new Query<PartEntity>(params).getPage(), entityWrapper);
		for(PartEntity entity : page.getRecords()){
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}

		return new PageUtils(page);
	}

	@Override
	public PageUtils partModeRelaList(Integer id, Map<String, Object> params) {
		Page<Map<String, Object>> page = new Page<>(Integer.parseInt(params.get("page").toString()),
				Integer.parseInt(params.get("limit").toString()));
		String modelName = (String) params.get("name");
		String code = (String) params.get("code");
		String remark = (String) params.get("remark");
		int modelSeriesId = 0;
		int deptId = 0;
		if ((String) params.get("modelSeriesId") != null && (String) params.get("modelSeriesId") != "") {
			modelSeriesId = Integer.parseInt((String) params.get("modelSeriesId"));
		}
		if ((String) params.get("deptId") != null && (String) params.get("deptId") != "") {
			deptId = Integer.parseInt((String) params.get("deptId"));
		}
		return new PageUtils(page
				.setRecords(this.baseMapper.selectpartModel(id, page, modelName, deptId, modelSeriesId, code, remark)));

	}

	@Override
	public void deleteList(List<PartEntity> partList) {
		for (PartEntity item : partList) {
			item.setDeleteAt(new Date());
		}
		if (partList.size() > 0) {
			this.updateAllColumnBatchById(partList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<PartEntity> partEntityList = this.selectBatchIds(ids);
		for (PartEntity item : partEntityList) {
			item.setDeleteAt(new Date());
		}
		if (partEntityList.size() > 0) {
			this.updateAllColumnBatchById(partEntityList);
		}

	}

	@Override
	public void deleteByWrapper(Wrapper<PartEntity> wrapper) {
		List<PartEntity> partEntityList = this.selectList(wrapper);
		for (PartEntity item : partEntityList) {
			item.setDeleteAt(new Date());
		}
		if (partEntityList.size() > 0) {
			this.updateAllColumnBatchById(partEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(PartEntity part) {
		part.setPinyin(PinyinUtil.getPinYin(part.getName()));
		part.setUpdateAt(new Date());
		this.updateById(part);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> partImport(Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		for(Map<String, Object> i:maps ){
			if(StringUtils.isEmpty((CharSequence) i.get("part.name"))){
				return RD.INTERNAL_SERVER_ERROR("导入部品时名称为空，请检查部品名称是否为空");
			}
		}
		List<PartEntity> partEntityList = new ArrayList<>();
		List<String> modelNameList = new ArrayList<>();
		List<String> partNameList = new ArrayList<String>();
		List<ModelPartRelaEntity> modelPartRelaList = new ArrayList<ModelPartRelaEntity>();
		// 遍历所有导入数据，插入部品
		for (int i = 0; i < maps.size(); i++) {
			PartEntity partEntity = new PartEntity();
			Map<String, Object> partMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				if (keyStrs[0].equals("part")) {
					if (keyStrs[1].equals("name")) {
						if (!partNameList.contains(value)) {
							partNameList.add((String) value);
						}
					}
					partMap.put(keyStrs[1], value);
				}
				if (keyStrs[0].equals("model")) {
					if (keyStrs[1].equals("name")) {
						if (!modelNameList.contains(value)) {
							modelNameList.add((String) value);
						}
					}
				}
			}
			DataUtils.transMap2Bean2(partMap, partEntity);
			ValidatorUtils.validateEntity(partEntity, i);
			partEntity.setPinyin(PinyinUtil.getPinYin(partEntity.getName()));
			partEntity.setCreateBy((Integer) map.get("userID"));
			partEntityList.add(partEntity);
		}
		try {
			this.insertBatch(partEntityList, Constant.importNum);
		} catch (Exception e) {
			throw new RRException("部件导入失败，请检查部件是否重复");
		}
		// 遍历所有本次导入所用到的机种
		Map<String, Integer> modelIDAndNameMap = new HashMap<String, Integer>();
		EntityWrapper<ModelEntity> modelWrapper = new EntityWrapper<>();
		modelWrapper.in("name", modelNameList).isNull("delete_at");
		List<ModelEntity> modelList = modelService.selectList(modelWrapper);
		for (ModelEntity item : modelList) {
			modelIDAndNameMap.put(item.getName(), item.getId());
		}
		Map<String, Integer> partIDAndNameMap = new HashMap<String, Integer>();
		EntityWrapper<PartEntity> partWrapper = new EntityWrapper<>();
		partWrapper.in("name", partNameList).isNull("delete_at");
		List<PartEntity> partList = this.selectList(partWrapper);
		for (PartEntity item : partList) {
			partIDAndNameMap.put(item.getName(), item.getId());
		}
		// 存储不存在的机种名称
		List<String> notExistModelName = new ArrayList<String>();
		// 关系表列表
		for (int i = 0; i < maps.size(); i++) {
			ModelPartRelaEntity modelPartRela = new ModelPartRelaEntity();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("model.name")) {
					if (modelIDAndNameMap.get(value) != null) {
						modelPartRela.setModelId(modelIDAndNameMap.get(value));
					} else {
						if (!notExistModelName.contains((String) value)) {
							notExistModelName.add((String) value);
						}
					}
				} else if (key.equals("part.name")) {
					modelPartRela.setPartId(partIDAndNameMap.get(value));
				}
			}
			modelPartRelaList.add(modelPartRela);
		}
		modelPartRelaService.insertBatch(modelPartRelaList);
		if (notExistModelName.size() > 0) {
			throw new RRException("机种" + notExistModelName.toString() + "不存在，请添加机种后再导入相关的数据");
		}
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("code", 200);
		return RD.success(data);
	}

}