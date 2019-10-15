package io.apj.modules.basic.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;

import io.apj.modules.basic.dao.JobDao;
import io.apj.modules.basic.entity.JobEntity;
import io.apj.modules.basic.service.JobService;

@Service("jobService")
public class JobServiceImpl extends ServiceImpl<JobDao, JobEntity> implements JobService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		Page<JobEntity> page = this.selectPage(new Query<JobEntity>(params).getPage(),
				this.basicSerachReturnWrapper(params));

		return new PageUtils(page);
	}

	@Override
	public void save(JobEntity job) {
		this.insert(job);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(JobEntity job) {
		JobEntity entity = this.selectById(job.getId());
		job.setCreateAt(entity.getCreateAt());
		job.setCreateBy(entity.getCreateBy());
		job.setDeleteAt(entity.getDeleteAt());
		this.updateAllColumnById(job);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 逻辑删除
		List<JobEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (JobEntity item : list) {
			item.setDeleteAt(new Date());
		}
		this.updateBatchById(list, list.size());
	}

	@Override
	public EntityWrapper<JobEntity> basicSerachReturnWrapper(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Wrapper<JobEntity> entityWrapper = new EntityWrapper<JobEntity>()
				.eq(StringUtils.isNotEmpty((CharSequence) map.get("grade")), "grade", map.get("grade"))
				.like(StringUtils.isNotEmpty((CharSequence) map.get("code")), "code", (String) map.get("code"));
		if (StringUtils.isNotEmpty((CharSequence) map.get("keyword"))) {
			String name = (String) map.get("keyword");
			name = name.replace("'", "");
			entityWrapper.andNew("pinyin like '%" + name + "%' " + "or name like '%" + name + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) map.get("name"))) {
			entityWrapper
					.andNew("pinyin like '%" + map.get("name") + "%' " + "or name like '%" + map.get("name") + "%'");
		}
		entityWrapper.orderBy("create_at", false);
		return (EntityWrapper<JobEntity>) entityWrapper;
	}

}
