package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.masterData.entity.PartEntity;
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

	@Override
	public void deleteList(List<OperationGroupOperationEntity> operationGroupOperationEntityList) {
		for(OperationGroupOperationEntity item : operationGroupOperationEntityList){
			item.setDeleteAt(new Date());
		}
		if(operationGroupOperationEntityList.size()>0){
			this.updateAllColumnBatchById(operationGroupOperationEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<OperationGroupOperationEntity> operationGroupOperationEntityList = this.selectBatchIds(ids);
		for(OperationGroupOperationEntity item : operationGroupOperationEntityList){
			item.setDeleteAt(new Date());
		}
		if(operationGroupOperationEntityList.size()>0){
			this.updateAllColumnBatchById(operationGroupOperationEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<OperationGroupOperationEntity> wrapper) {
		List<OperationGroupOperationEntity> operationGroupOperationEntityList = this.selectList(wrapper);
		for(OperationGroupOperationEntity item: operationGroupOperationEntityList){
			item.setDeleteAt(new Date());
		}
		if(operationGroupOperationEntityList.size()>0){
			this.updateAllColumnBatchById(operationGroupOperationEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(OperationGroupOperationEntity operationGroupOperationEntity) {
		operationGroupOperationEntity.setPinyin(PinyinUtil.getPinYin(operationGroupOperationEntity.getOperation()));
		operationGroupOperationEntity.setUpdateAt(new Date());
		this.updateById(operationGroupOperationEntity);
	}

}