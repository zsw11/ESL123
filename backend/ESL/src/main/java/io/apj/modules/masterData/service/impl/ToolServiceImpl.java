package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.sys.service.SysDeptService;
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
import io.apj.modules.masterData.dao.ToolDao;


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
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
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
//		EntityWrapper<ModelToolRelaEntity> relaEntityWrapper = new EntityWrapper<>();
//		relaEntityWrapper.eq("tool_id", id).isNull("delete_at");
//		Page<ModelToolRelaEntity> page = modelToolRelaService
//				.selectPage(new Query<ModelToolRelaEntity>(params).getPage(), relaEntityWrapper);
//		for (ModelToolRelaEntity item : page.getRecords()) {
//			item.setModelEntity(modelService.selectById(item.getModelId()));
//			int modelSeriesId =  modelService.selectById(item.getModelId()).getModelSeriesId();
//			item.setModelSeriesEntity(modelSeriesService.selectById(modelSeriesId));
//			item.setDeptName(deptService.selectById(modelService.selectById(item.getModelId()).getDeptId()).getName());
//		}
//		return new PageUtils(page);
//	}

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

