package io.apj.modules.workBook.service.impl;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.workBook.dao.WorkOperationsDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkOperationsService;

@Service("workOperationsService")
public class WorkOperationsServiceImpl extends ServiceImpl<WorkOperationsDao, WorkOperationsEntity>
		implements WorkOperationsService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<WorkOperationsEntity> page = this.selectPage(new Query<WorkOperationsEntity>(params).getPage());

		return new PageUtils(page);
	}

	@Override
	public void deletebyWrapper(Wrapper<WorkOperationsEntity> wrapper) {
		List<WorkOperationsEntity> WorkOperationsList = this.selectList(wrapper);
		for (WorkOperationsEntity item : WorkOperationsList) {
			item.setDeleteAt(new Date());
		}
		this.updateAllColumnBatchById(WorkOperationsList);
	}

}