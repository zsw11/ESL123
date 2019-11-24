package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportDao;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ReportService;

@Service("reportService")
public class ReportServiceImpl extends ServiceImpl<ReportDao, ReportEntity> implements ReportService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ReportEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.like(params.get("formCode") != null && params.get("formCode") != "", "form_code",
						(String) params.get("formCode"))
				.like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}
		Page<ReportEntity> page = this.selectPage(new Query<ReportEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

}