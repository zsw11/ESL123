package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.PartEntity;
import io.swagger.annotations.ApiOperation;
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
import io.apj.modules.masterData.dao.WorkstationTypeDao;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.WorkstationTypeService;

@Service("workstationTypeService")
public class WorkstationTypeServiceImpl extends ServiceImpl<WorkstationTypeDao, WorkstationTypeEntity>
		implements WorkstationTypeService {
	@Autowired
	private StaffService staffService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<WorkstationTypeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").like(params.get("remark") != null && params.get("remark") != "", "remark",
				(String) params.get("remark"));
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
		Page<WorkstationTypeEntity> page = this.selectPage(new Query<WorkstationTypeEntity>(params).getPage(),
				entityWrapper);
		for(WorkstationTypeEntity entity : page.getRecords()){
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}

		return new PageUtils(page);
	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<WorkstationTypeEntity> workstationTypeEntityList = this.selectBatchIds(ids);
		for(WorkstationTypeEntity item : workstationTypeEntityList){
			item.setDeleteAt(new Date());
		}
		if(workstationTypeEntityList.size()>0){
			this.updateAllColumnBatchById(workstationTypeEntityList);
		}

	}

	@Override
	public void deleteByWrapper(Wrapper<WorkstationTypeEntity> wrapper) {
		List<WorkstationTypeEntity> workstationTypeEntities = this.selectList(wrapper);
		for(WorkstationTypeEntity item: workstationTypeEntities){
			item.setDeleteAt(new Date());
		}
		if(workstationTypeEntities.size()>0){
			this.updateAllColumnBatchById(workstationTypeEntities);
		}
	}

	@Override
	public void updatePinAndDataById(WorkstationTypeEntity workstationTypeEntity) {
		workstationTypeEntity.setPinyin(PinyinUtil.getPinYin(workstationTypeEntity.getName()));
		workstationTypeEntity.setUpdateAt(new Date());
		this.updateById(workstationTypeEntity);
	}

}