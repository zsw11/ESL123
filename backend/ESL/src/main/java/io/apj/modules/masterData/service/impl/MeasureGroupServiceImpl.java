package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.MeasureGroupDao;
import io.apj.modules.masterData.entity.MeasureGroupEntity;
import io.apj.modules.masterData.service.MeasureGroupService;

@Service("measureGroupService")
public class MeasureGroupServiceImpl extends ServiceImpl<MeasureGroupDao, MeasureGroupEntity>
		implements MeasureGroupService {
	@Autowired
	private SysDeptService deptService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<MeasureGroupEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false).like(
				params.get("code") != null && params.get("code") != "", "UPPER(code)",
				params.get("code").toString().toUpperCase());
		if (StringUtils.isNotEmpty((CharSequence) params.get("frequency"))) {
			entityWrapper.eq("frequency", Integer.parseInt((String) params.get("frequency")));
		}
		Page<MeasureGroupEntity> page = this.selectPage(new Query<MeasureGroupEntity>(params).getPage(), entityWrapper);
		for (MeasureGroupEntity entity : page.getRecords()) {
			entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
		}

		return new PageUtils(page);
	}

	@Override
	public void deleteList(List<MeasureGroupEntity> measureGroupEntityList) {
		for (MeasureGroupEntity item : measureGroupEntityList) {
			item.setDeleteAt(new Date());
		}
		if (measureGroupEntityList.size() > 0) {
			this.updateAllColumnBatchById(measureGroupEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<MeasureGroupEntity> measureGroupEntityList = this.selectBatchIds(ids);
		for (MeasureGroupEntity item : measureGroupEntityList) {
			item.setDeleteAt(new Date());
		}
		if (measureGroupEntityList.size() > 0) {
			this.updateAllColumnBatchById(measureGroupEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<MeasureGroupEntity> wrapper) {
		List<MeasureGroupEntity> measureGroupEntityList = this.selectList(wrapper);
		for (MeasureGroupEntity item : measureGroupEntityList) {
			item.setDeleteAt(new Date());
		}
		if (measureGroupEntityList.size() > 0) {
			this.updateAllColumnBatchById(measureGroupEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(MeasureGroupEntity measureGroupEntity) {
		measureGroupEntity.setUpdateAt(new Date());
		this.updateById(measureGroupEntity);
	}

}