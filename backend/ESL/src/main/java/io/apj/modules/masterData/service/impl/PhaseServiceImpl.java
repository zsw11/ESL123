package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.PartEntity;
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
import io.apj.modules.masterData.dao.PhaseDao;
import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.service.PhaseService;

@Service("phaseService")
public class PhaseServiceImpl extends ServiceImpl<PhaseDao, PhaseEntity> implements PhaseService {
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private StaffService staffService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<PhaseEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.like(params.get("remark") != null && params.get("remark") != "","remark", (String) params.get("remark"))
				.orderBy("update_at",false).like(params.get("keyword") != null && params.get("keyword") != "", "name",
				(String) params.get("keyword"));
		if(StringUtils.isNotEmpty((CharSequence) params.get("continuePhaseId"))){
			entityWrapper.eq("continue_phase_id",Integer.parseInt((String) params.get("continuePhaseId")));
		}
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
		Page<PhaseEntity> page = this.selectPage(new Query<PhaseEntity>(params).getPage(), entityWrapper);

		for (PhaseEntity entity : page.getRecords()) {
			entity.setPhaseEntity(phaseService.selectById(entity.getContinuePhaseId()));
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}
		return new PageUtils(page);
	}
	@Override
	public void deleteList(List<PhaseEntity> phaseEntityList) {
		for(PhaseEntity item : phaseEntityList){
			item.setDeleteAt(new Date());
		}
		if(phaseEntityList.size()>0){
			this.updateAllColumnBatchById(phaseEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<PhaseEntity> phaseEntityList = this.selectBatchIds(ids);
		for(PhaseEntity item : phaseEntityList){
			item.setDeleteAt(new Date());
		}
		if(phaseEntityList.size()>0){
			this.updateAllColumnBatchById(phaseEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<PhaseEntity> wrapper) {
		List<PhaseEntity> phaseEntityList = this.selectList(wrapper);
		for(PhaseEntity item: phaseEntityList){
			item.setDeleteAt(new Date());
		}
		if(phaseEntityList.size()>0){
			this.updateAllColumnBatchById(phaseEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(PhaseEntity phase) {
		phase.setPinyin(PinyinUtil.getPinYin(phase.getName()));
		phase.setUpdateAt(new Date());
		this.updateById(phase);
	}

}