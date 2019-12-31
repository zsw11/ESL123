package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.*;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.modules.masterData.dao.ToolDao;
import org.springframework.transaction.annotation.Transactional;


@Service("toolService")
public class ToolServiceImpl extends ServiceImpl<ToolDao, ToolEntity> implements ToolService {
	@Autowired
	private ModelToolRelaService modelToolRelaService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ModelSeriesService modelSeriesService;
	@Autowired
	private SysDeptService deptService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ToolEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.eq(params.get("common") != null && params.get("common") != "", "common", Boolean.parseBoolean((String) params.get("common")))
				.like(params.get("remark") != null && params.get("remark") != "","remark", (String) params.get("remark"))
				.orderBy("update_at",false);
		if (params.get("name") != null && params.get("name") != "") {
			params.put("name", ((String) params.get("name")).replace('*', '%'));
			entityWrapper.andNew(
					"UPPER(pinyin) like '%" + ((String) params.get("name")).toUpperCase() + "%' " + "or UPPER(name) like '%" + ((String) params.get("name")).toUpperCase() + "%'");
		}
		Page<ToolEntity> page = this.selectPage(new Query<ToolEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public PageUtils toolModeRelaList(Integer id, Map<String, Object> params) {
		Page<Map<String,Object>> page  = new Page<>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
		String modelName = (String) params.get("name");
		String code = (String) params.get("code");
		String remark = (String) params.get("remark");
		int modelSeriesId = 0;
		int deptId = 0;
		if((String) params.get("modelSeriesId")!=null && (String) params.get("modelSeriesId")!=""){
			modelSeriesId = Integer.parseInt((String) params.get("modelSeriesId"));
		}
		if((String) params.get("deptId")!=null && (String) params.get("deptId")!=""){
			deptId = Integer.parseInt((String) params.get("deptId"));
		}
		return new PageUtils(page.setRecords(this.baseMapper.selecttoolModel(id, page, modelName,deptId,modelSeriesId,code,remark)));

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<Object> toolImport(Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		for(Map<String, Object> i:maps ){
			if(StringUtils.isEmpty((CharSequence) i.get("tool.name")) ){
				return RD.INTERNAL_SERVER_ERROR("导入治工具时名称为空，请检查治工具名称是否为空");
			}
		}
		List<ToolEntity> toolEntityList = new ArrayList<>();
		List<String> modelNameList = new ArrayList<>();
		List<String> toolNameList = new ArrayList<String>();
		List<ModelToolRelaEntity> modelToolRelaList = new ArrayList<>();
		// 遍历所有导入数据，插入部品
		for (int i = 0; i < maps.size(); i++) {
			ToolEntity toolEntity = new ToolEntity();
			Map<String, Object> toolMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				if (keyStrs[0].equals("tool")) {
					if (keyStrs[1].equals("name")) {
						if (!toolNameList.contains(value)) {
							toolNameList.add((String) value);
						}
					}
					toolMap.put(keyStrs[1], value);
				}
				if (keyStrs[0].equals("model")) {
					if (keyStrs[1].equals("name")) {
						if (!modelNameList.contains(value)) {
							modelNameList.add((String) value);
						}
					}
				}
			}
			DataUtils.transMap2Bean2(toolMap, toolEntity);
			ValidatorUtils.validateEntity(toolEntity, i);
			toolEntity.setPinyin(PinyinUtil.getPinYin(toolEntity.getName()));
			toolEntity.setCreateBy((Integer) map.get("userID"));
			toolEntityList.add(toolEntity);
		}
		try {
			this.insertBatch(toolEntityList, Constant.importNum);
		} catch (Exception e) {
			throw new RRException("治工具导入失败，请检查治工具是否重复");
		}
		// 遍历所有本次导入所用到的机种
		Map<String, Integer> modelIDAndNameMap = new HashMap<String, Integer>();
		EntityWrapper<ModelEntity> modelWrapper = new EntityWrapper<>();
		modelWrapper.in("name", modelNameList).isNull("delete_at");
		List<ModelEntity> modelList = modelService.selectList(modelWrapper);
		for (ModelEntity item : modelList) {
			modelIDAndNameMap.put(item.getName(), item.getId());
		}
		Map<String, Integer> toolIDAndNameMap = new HashMap<String, Integer>();
		EntityWrapper<ToolEntity> toolWrapper = new EntityWrapper<>();
		toolWrapper.in("name", toolNameList).isNull("delete_at");
		List<ToolEntity> toolList = this.selectList(toolWrapper);
		for (ToolEntity item : toolList) {
			toolIDAndNameMap.put(item.getName(), item.getId());
		}
		// 存储不存在的机种名称
		List<String> notExistModelName = new ArrayList<String>();
		// 关系表列表
		for (int i = 0; i < maps.size(); i++) {
			ModelToolRelaEntity modelToolRela = new ModelToolRelaEntity();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("model.name")) {
					if (modelIDAndNameMap.get(value) != null) {
						modelToolRela.setModelId(modelIDAndNameMap.get(value));
					} else {
						if (!notExistModelName.contains((String) value)) {
							notExistModelName.add((String) value);
						}
					}
				} else if (key.equals("tool.name")) {
					modelToolRela.setToolId(toolIDAndNameMap.get(value));
				}
			}
			modelToolRelaList.add(modelToolRela);
		}
		modelToolRelaService.insertBatch(modelToolRelaList);
		if (notExistModelName.size() > 0) {
			throw new RRException("机种" + notExistModelName.toString() + "不存在，请添加机种后再导入相关的数据");
		}
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("code", 200);
		return RD.success(data);
	}

	@Override
	public void deleteList(List<ToolEntity> toolEntityList) {
		for(ToolEntity item : toolEntityList){
			item.setDeleteAt(new Date());
		}
		if(toolEntityList.size()>0){
			this.updateAllColumnBatchById(toolEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<ToolEntity> toolEntityList = this.selectBatchIds(ids);
		for(ToolEntity item : toolEntityList){
			item.setDeleteAt(new Date());
		}
		if(toolEntityList.size()>0){
			this.updateAllColumnBatchById(toolEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<ToolEntity> wrapper) {
		List<ToolEntity> toolEntityList = this.selectList(wrapper);
		for(ToolEntity item: toolEntityList){
			item.setDeleteAt(new Date());
		}
		if(toolEntityList.size()>0){
			this.updateAllColumnBatchById(toolEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(ToolEntity toolEntity) {
		toolEntity.setPinyin(PinyinUtil.getPinYin(toolEntity.getName()));
		toolEntity.setUpdateAt(new Date());
		this.updateById(toolEntity);
	}
}

