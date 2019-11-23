package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelSeriesDao;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.masterData.service.ModelSeriesService;

@Service("modelSeriesService")
public class ModelSeriesServiceImpl extends ServiceImpl<ModelSeriesDao, ModelSeriesEntity>
		implements ModelSeriesService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ModelSeriesEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"))
				.like(params.get("keyWord") != null && params.get("keyWord") != "", "name",
						(String) params.get("keyWord"));
		Page<ModelSeriesEntity> page = this.selectPage(new Query<ModelSeriesEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

}