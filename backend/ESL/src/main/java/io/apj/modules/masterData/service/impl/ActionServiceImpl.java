package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.masterData.entity.ReportEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ActionDao;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.service.ActionService;

@Service("actionService")
public class ActionServiceImpl extends ServiceImpl<ActionDao, ActionEntity> implements ActionService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ActionEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at",false)
				.like(params.get("remark") != null && params.get("remark") != "","remark", (String) params.get("remark"));
		if (params.get("name") != null && params.get("name") != "") {
			params.put("name", ((String) params.get("name")).replace('*', '%'));
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}

		Page<ActionEntity> page = this.selectPage(new Query<ActionEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public void deleteList(List<ActionEntity> actionEntityList) {
		for(ActionEntity item : actionEntityList){
			item.setDeleteAt(new Date());
		}
		if(actionEntityList.size()>0){
			this.updateAllColumnBatchById(actionEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<ActionEntity> actionEntityList = this.selectBatchIds(ids);
		for(ActionEntity item : actionEntityList){
			item.setDeleteAt(new Date());
		}
		if(actionEntityList.size()>0){
			this.updateAllColumnBatchById(actionEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<ActionEntity> wrapper) {
		List<ActionEntity> actionEntityList = this.selectList(wrapper);
		for(ActionEntity item: actionEntityList){
			item.setDeleteAt(new Date());
		}
		if(actionEntityList.size()>0){
			this.updateAllColumnBatchById(actionEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(ActionEntity actionEntity) {
		actionEntity.setPinyin(PinyinUtil.getPinYin(actionEntity.getName()));
		actionEntity.setUpdateAt(new Date());
		this.updateById(actionEntity);
	}

}