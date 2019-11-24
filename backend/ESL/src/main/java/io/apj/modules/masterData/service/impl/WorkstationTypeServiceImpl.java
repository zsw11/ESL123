package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.WorkstationTypeDao;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.WorkstationTypeService;

@Service("workstationTypeService")
public class WorkstationTypeServiceImpl extends ServiceImpl<WorkstationTypeDao, WorkstationTypeEntity>
		implements WorkstationTypeService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<WorkstationTypeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").like(params.get("remark") != null && params.get("remark") != "", "remark",
				(String) params.get("remark"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}
		Page<WorkstationTypeEntity> page = this.selectPage(new Query<WorkstationTypeEntity>(params).getPage(),
				entityWrapper);

		return new PageUtils(page);
	}

}