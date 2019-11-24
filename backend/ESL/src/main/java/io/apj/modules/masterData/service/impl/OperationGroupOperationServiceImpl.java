package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.OperationGroupOperationDao;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;

@Service("operationGroupOperationService")
public class OperationGroupOperationServiceImpl
		extends ServiceImpl<OperationGroupOperationDao, OperationGroupOperationEntity>
		implements OperationGroupOperationService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<OperationGroupOperationEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.like(params.get("operationGroupId") != null && params.get("operationGroupId") != "",
						"operation_group_id", (String) params.get("operationGroupId"))
				.eq(params.get("seqNumber") != null && params.get("seqNumber") != "", "seq_number",
						(String) params.get("seqNumber"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("operation"))) {
			entityWrapper.andNew("pinyin like '%" + params.get("operation") + "%' " + "or operation like '%"
					+ params.get("operation") + "%'");
		}
		Page<OperationGroupOperationEntity> page = this
				.selectPage(new Query<OperationGroupOperationEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

}