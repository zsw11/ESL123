package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.PartEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ApproveOpininonDao;
import io.apj.modules.masterData.entity.ApproveOpininonEntity;
import io.apj.modules.masterData.service.ApproveOpininonService;

@Service("approveOpininonService")
public class ApproveOpininonServiceImpl extends ServiceImpl<ApproveOpininonDao, ApproveOpininonEntity>
		implements ApproveOpininonService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ApproveOpininonEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").like(params.get("opininon") != null && params.get("opininon") != "",
				"opininon", (String) params.get("opininon"));
		if (params.get("approveOperation") != null && params.get("approveOperation") != "") {
			entityWrapper.andNew("pinyin like '%" + params.get("approveOperation") + "%' "
					+ "or approve_operation like '%" + params.get("approveOperation") + "%'");
		}
		Page<ApproveOpininonEntity> page = this.selectPage(new Query<ApproveOpininonEntity>(params).getPage(),
				entityWrapper);

		return new PageUtils(page);
	}

}