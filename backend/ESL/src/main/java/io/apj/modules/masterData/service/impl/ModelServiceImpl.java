package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.sys.service.SysDeptService;

@Service("modelService")
public class ModelServiceImpl extends ServiceImpl<ModelDao, ModelEntity> implements ModelService {
	@Autowired
	private ModelSeriesService modelSeriesService;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private PartService partService;
	@Autowired
	private ToolService toolService;
	@Autowired
	private ModelPartRelaService modelPartRelaService;
	@Autowired
	private ModelToolRelaService modelToolRelaService;


	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at",false)
				.like(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"));
		if(StringUtils.isNotEmpty((CharSequence) params.get("deptId"))){
			entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
		}
		if(StringUtils.isNotEmpty((CharSequence) params.get("modelSeriesId"))){
			entityWrapper.eq("model_series_id", Integer.parseInt((String) params.get("modelSeriesId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))) {
			String name = (String) params.get("keyWord");
			name = name.replace(",", "");
			entityWrapper.andNew("name  like '%" + name + "%'" + " or code  like '%" + name + "%'"
					+ " or pinyin  like '%" + name + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			String name = (String) params.get("name");
			name = name.replace(",", "");
			entityWrapper.andNew("name  like '%" + name + "%'" + " or pinyin  like '%" + name + "%'");
		}
		Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);
		for (ModelEntity entity : page.getRecords()) {
			entity.setModelSeriesEntity(modelSeriesService.selectById(entity.getModelSeriesId()));
			entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
		}
		return new PageUtils(page);
	}

	@Override
	public PageUtils selectBySeriesId(int id, Map<String, Object> params) {
		EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").eq("model_series_id", id).orderBy("update_at",false);
		Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public PageUtils modelPartRelaList(int id, Map<String, Object> params) {
		Page<Map<String,Object>> page  = new Page<>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
		String name = (String) params.get("name");
		return new PageUtils(page.setRecords(this.baseMapper.selectmodelPart(id, page, name)));

	}

//		EntityWrapper<ModelPartRelaEntity> relaEntityWrapper = new EntityWrapper<ModelPartRelaEntity>();
//		relaEntityWrapper.eq("model_id", id).isNull("delete_at");
//		Page<ModelPartRelaEntity> page = modelPartRelaService
//				.selectPage(new Query<ModelPartRelaEntity>(params).getPage(), relaEntityWrapper);
//		for (ModelPartRelaEntity item : page.getRecords()) {
//			item.setPartEntity(partService.selectById(item.getPartId()));
//		}
//		return new PageUtils(page);
//	}

	@Override
	public PageUtils modelToolRelaList(int id, Map<String, Object> params) {
		Page<Map<String,Object>> page  = new Page<>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
		String name = (String) params.get("name");
		return new PageUtils(page.setRecords(this.baseMapper.selectmodelTool(id, page, name)));


	}
//		EntityWrapper<ModelToolRelaEntity> relaEntityWrapper = new EntityWrapper<ModelToolRelaEntity>();
//		relaEntityWrapper.eq("model_id", id).isNull("delete_at");
//		Page<ModelToolRelaEntity> page = modelToolRelaService
//				.selectPage(new Query<ModelToolRelaEntity>(params).getPage(), relaEntityWrapper);
//		for (ModelToolRelaEntity item : page.getRecords()) {
//			item.setToolEntity(toolService.selectById(item.getToolId()));
//		}
//		return new PageUtils(page);
//	}

	@Override
	public void deleteList(List<ModelEntity> modelEntityList) {
		for(ModelEntity item : modelEntityList){
			item.setDeleteAt(new Date());
		}
		this.updateAllColumnBatchById(modelEntityList);
	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<ModelEntity> modelEntityList = this.selectBatchIds(ids);
		for(ModelEntity item : modelEntityList){
			item.setDeleteAt(new Date());
		}
		this.updateAllColumnBatchById(modelEntityList);
	}

	@Override
	public void deleteByWrapper(Wrapper<ModelEntity> wrapper) {
		List<ModelEntity> modelEntityList = this.selectList(wrapper);
		for(ModelEntity item: modelEntityList){
			item.setDeleteAt(new Date());
		}
		this.updateAllColumnBatchById(modelEntityList);
	}

	@Override
	public void updatePinAndDataById(ModelEntity modelEntity) {
		modelEntity.setPinyin(PinyinUtil.getPinYin(modelEntity.getName()));
		modelEntity.setUpdateAt(new Date());
		this.updateById(modelEntity);
	}
}