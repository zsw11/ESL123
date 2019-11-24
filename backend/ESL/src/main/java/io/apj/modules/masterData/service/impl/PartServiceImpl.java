package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.PartDao;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("partService")
public class PartServiceImpl extends ServiceImpl<PartDao, PartEntity> implements PartService {
	@Autowired
	private ModelPartRelaService modelPartRelaService;
	@Autowired
	private ModelService modelService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<PartEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at");
		if (params.get("name") != null && params.get("name") != "") {
			params.put("name", ((String) params.get("name")).replace('*', '%'));
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}
		Page<PartEntity> page = this.selectPage(new Query<PartEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public PageUtils partModeRelaList(Integer id, Map<String, Object> params) {
		EntityWrapper<ModelPartRelaEntity> relaEntityWrapper = new EntityWrapper<ModelPartRelaEntity>();
		relaEntityWrapper.eq("part_id", id).isNull("delete_at");
		Page<ModelPartRelaEntity> page = modelPartRelaService
				.selectPage(new Query<ModelPartRelaEntity>(params).getPage(), relaEntityWrapper);
		for (ModelPartRelaEntity item : page.getRecords()) {
			item.setModelEntity(modelService.selectById(item.getModelId()));
		}
		return new PageUtils(page);
	}

}