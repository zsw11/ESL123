package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
		entityWrapper.isNull("delete_at");
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
		Integer deptId = (Integer) params.get("deptId");
		Integer modelSeriesId = (Integer) params.get("modelSeriesId");
		return new PageUtils(page.setRecords(this.baseMapper.selecttoolModel(id, page, modelName,deptId,modelSeriesId,code)));

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
}

