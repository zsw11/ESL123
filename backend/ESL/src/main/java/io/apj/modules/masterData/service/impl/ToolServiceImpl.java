package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		if (params.get("name") != null && params.get("name") != "") {
			params.put("name", ((String) params.get("name")).replace('*', '%'));
		}
		EntityWrapper<ToolEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").like(params.get("name") != null && params.get("name") != "", "name",
				(String) params.get("name"));
		Page<ToolEntity> page = this.selectPage(new Query<ToolEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

}