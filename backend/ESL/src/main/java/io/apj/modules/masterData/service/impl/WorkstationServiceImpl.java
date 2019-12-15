package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.WorkstationTypeService;
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
import io.apj.modules.masterData.dao.WorkstationDao;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.WorkstationService;

@Service("workstationService")
public class WorkstationServiceImpl extends ServiceImpl<WorkstationDao, WorkstationEntity>
		implements WorkstationService {

	@Autowired
	private WorkstationTypeService workstationTypeService;
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<WorkstationEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at",false)
				.like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"))
				.like(params.get("keyWord") != null && params.get("keyWord") != "", "name",
						(String) params.get("keyWord"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}
		Page<WorkstationEntity> page = this.selectPage(new Query<WorkstationEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	/**
	 * 判断工位是否SUB
	 * @param wookStationId
	 * @return
	 */
	public Boolean wookStationIdIsSub(Integer wookStationId){
		WorkstationEntity workstationEntity = selectById(wookStationId);
		if(workstationEntity.getWorkstationTypeId()>0){
			WorkstationTypeEntity workstationType = workstationTypeService.selectById(workstationEntity.getWorkstationTypeId());
			Boolean bool = workstationType ==null?false:workstationType.getName()== null ? false: workstationType.getName().toLowerCase().contains("sub");
			return bool;
		}else {
			return false;
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<WorkstationEntity> workstationEntities = this.selectBatchIds(ids);
		for(WorkstationEntity item : workstationEntities){
			item.setDeleteAt(new Date());
		}
		if(workstationEntities.size()>0){
			this.updateAllColumnBatchById(workstationEntities);
		}

	}

	@Override
	public void deleteByWrapper(Wrapper<WorkstationEntity> wrapper) {
		List<WorkstationEntity> workstationEntityList = this.selectList(wrapper);
		for(WorkstationEntity item: workstationEntityList){
			item.setDeleteAt(new Date());
		}
		if(workstationEntityList.size()>0){
			this.updateAllColumnBatchById(workstationEntityList);
		}

	}

	@Override
	public void updatePinAndDataById(WorkstationEntity workstationEntity) {
		workstationEntity.setPinyin(PinyinUtil.getPinYin(workstationEntity.getName()));
		workstationEntity.setUpdateAt(new Date());
		this.updateById(workstationEntity);
	}

}
