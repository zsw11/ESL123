package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.DeptActionRelaDao;
import io.apj.modules.masterData.entity.DeptActionRelaEntity;
import io.apj.modules.masterData.service.DeptActionRelaService;

@Service("deptOperationRelaService")
public class DeptActionRelaServiceImpl extends ServiceImpl<DeptActionRelaDao, DeptActionRelaEntity>
		implements DeptActionRelaService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<DeptActionRelaEntity> page = this.selectPage(new Query<DeptActionRelaEntity>(params).getPage());

		return new PageUtils(page);
	}

}