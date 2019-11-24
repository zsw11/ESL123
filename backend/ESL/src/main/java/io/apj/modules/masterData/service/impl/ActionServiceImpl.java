package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ToolEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ActionDao;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.service.ActionService;

@Service("actionService")
public class ActionServiceImpl extends ServiceImpl<ActionDao, ActionEntity> implements ActionService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ActionEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at");
		if (params.get("name") != null && params.get("name") != "") {
			params.put("name", ((String) params.get("name")).replace('*', '%'));
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}

		Page<ActionEntity> page = this.selectPage(new Query<ActionEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

}