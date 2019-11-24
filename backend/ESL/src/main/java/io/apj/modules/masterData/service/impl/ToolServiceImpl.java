package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.ModelToolRelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ToolDao;
import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.ToolService;

@Service("toolService")
public class ToolServiceImpl extends ServiceImpl<ToolDao, ToolEntity> implements ToolService {
	@Autowired
	private ModelToolRelaService modelToolRelaService;
	@Autowired
	private ModelService modelService;

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
		EntityWrapper<ModelToolRelaEntity> relaEntityWrapper = new EntityWrapper<>();
		relaEntityWrapper.eq("tool_id", id).isNull("delete_at");
		Page<ModelToolRelaEntity> page = modelToolRelaService
				.selectPage(new Query<ModelToolRelaEntity>(params).getPage(), relaEntityWrapper);
		for (ModelToolRelaEntity item : page.getRecords()) {
			item.setModelEntity(modelService.selectById(item.getModelId()));
		}
		return new PageUtils(page);
	}
}

