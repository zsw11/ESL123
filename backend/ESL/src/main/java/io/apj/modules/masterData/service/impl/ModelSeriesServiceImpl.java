package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelSeriesDao;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.masterData.service.ModelSeriesService;

@Service("modelSeriesService")
public class ModelSeriesServiceImpl extends ServiceImpl<ModelSeriesDao, ModelSeriesEntity>
		implements ModelSeriesService {
	@Autowired
	private StaffService staffService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ModelSeriesEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("remark") != null && params.get("remark") != "", "remark",
						(String) params.get("remark"))
				.like(params.get("keyWord") != null && params.get("keyWord") != "", "name",
						(String) params.get("keyWord"))
				.orderBy("update_at", false);
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"UPPER(pinyin) like '%" + params.get("name").toString().toUpperCase() + "%' " + "or UPPER(name) like '%" + params.get("name").toString().toUpperCase() + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("createBy"))) {
			entityWrapper.eq("create_By", Integer.parseInt((String) params.get("createBy")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
			entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
		}
		Page<ModelSeriesEntity> page = this.selectPage(new Query<ModelSeriesEntity>(params).getPage(), entityWrapper);
		for(ModelSeriesEntity entity : page.getRecords()){
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}
		return new PageUtils(page);
	}

	@Override
	public void deleteList(List<ModelSeriesEntity> modelSeriesEntityList) {
		for (ModelSeriesEntity item : modelSeriesEntityList) {
			item.setDeleteAt(new Date());
		}
		if (modelSeriesEntityList.size() > 0) {
			this.updateAllColumnBatchById(modelSeriesEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<ModelSeriesEntity> modelSeriesEntityList = this.selectBatchIds(ids);
		for (ModelSeriesEntity item : modelSeriesEntityList) {
			item.setDeleteAt(new Date());
		}
		if (modelSeriesEntityList.size() > 0) {
			this.updateAllColumnBatchById(modelSeriesEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<ModelSeriesEntity> wrapper) {
		List<ModelSeriesEntity> modelSeriesEntityList = this.selectList(wrapper);
		for (ModelSeriesEntity item : modelSeriesEntityList) {
			item.setDeleteAt(new Date());
		}
		if (modelSeriesEntityList.size() > 0) {
			this.updateAllColumnBatchById(modelSeriesEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(ModelSeriesEntity modelSeriesEntity) {
		modelSeriesEntity.setPinyin(PinyinUtil.getPinYin(modelSeriesEntity.getName()));
		modelSeriesEntity.setUpdateAt(new Date());
		this.updateById(modelSeriesEntity);
	}

}