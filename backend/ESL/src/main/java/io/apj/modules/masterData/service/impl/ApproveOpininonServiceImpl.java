package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.report.entity.ApproveEntity;
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
import io.apj.modules.masterData.dao.ApproveOpininonDao;
import io.apj.modules.masterData.entity.ApproveOpininonEntity;
import io.apj.modules.masterData.service.ApproveOpininonService;

@Service("approveOpininonService")
public class ApproveOpininonServiceImpl extends ServiceImpl<ApproveOpininonDao, ApproveOpininonEntity>
		implements ApproveOpininonService {
	@Autowired
	private StaffService staffService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ApproveOpininonEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.orderBy("update_at",false).isNull("delete_at").like(params.get("opininon") != null && params.get("opininon") != "",
				"opininon", (String) params.get("opininon"));
		if (params.get("approveOperation") != null && params.get("approveOperation") != "") {
			entityWrapper.andNew("pinyin like '%" + params.get("approveOperation") + "%' "
					+ "or approve_operation like '%" + params.get("approveOperation") + "%'");
		}
		Page<ApproveOpininonEntity> page = this.selectPage(new Query<ApproveOpininonEntity>(params).getPage(),
				entityWrapper);
		for(ApproveOpininonEntity entity: page.getRecords()){
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
		}
		return new PageUtils(page);
	}
	@Override
	public void deleteList(List<ApproveOpininonEntity> approveOpininonEntityList) {
		for(ApproveOpininonEntity item : approveOpininonEntityList){
			item.setDeleteAt(new Date());
		}
		if(approveOpininonEntityList.size()>0){
			this.updateAllColumnBatchById(approveOpininonEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<ApproveOpininonEntity> approveOpininonEntityList = this.selectBatchIds(ids);
		for(ApproveOpininonEntity item : approveOpininonEntityList){
			item.setDeleteAt(new Date());
		}
		if(approveOpininonEntityList.size()>0){
			this.updateAllColumnBatchById(approveOpininonEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<ApproveOpininonEntity> wrapper) {
		List<ApproveOpininonEntity> approveOpininonEntityList = this.selectList(wrapper);
		for(ApproveOpininonEntity item: approveOpininonEntityList){
			item.setDeleteAt(new Date());
		}
		if(approveOpininonEntityList.size()>0){
			this.updateAllColumnBatchById(approveOpininonEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(ApproveOpininonEntity approveOpininonEntity) {
		approveOpininonEntity.setPinyin(PinyinUtil.getPinYin(approveOpininonEntity.getApproveOperation()));
		approveOpininonEntity.setUpdateAt(new Date());
		this.updateById(approveOpininonEntity);
	}

}