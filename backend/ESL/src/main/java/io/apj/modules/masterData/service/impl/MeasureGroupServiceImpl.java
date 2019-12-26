package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.exception.RRException;
import io.apj.common.utils.Constant;
import io.apj.common.utils.DataUtils;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.MeasureGroupDao;
import io.apj.modules.masterData.entity.MeasureGroupEntity;
import io.apj.modules.masterData.service.MeasureGroupService;
import org.springframework.transaction.annotation.Transactional;

@Service("measureGroupService")
public class MeasureGroupServiceImpl extends ServiceImpl<MeasureGroupDao, MeasureGroupEntity>
		implements MeasureGroupService {
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private MeasureGroupService measureGroupService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<MeasureGroupEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false);
		if(params.get("code") != null && params.get("code") != ""){
			entityWrapper.like("UPPER(code)",params.get("code").toString().toUpperCase());
		}
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
	@Transactional
	public void measureGroupImport(Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<MeasureGroupEntity> measureGroupEntityList = new ArrayList<>();
		for (int i = 0; i < maps.size(); i++) {
			MeasureGroupEntity measureGroupEntity = new MeasureGroupEntity();
			// deviceMap
			Map<String, Object> deviceMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 设备
				if (keyStrs[0].equals("measureGroup")) {
					if (keyStrs[1].equals("common")) {
						if(value.equals("是")) {
							deviceMap.put(keyStrs[1], true);
						} else {
							deviceMap.put(keyStrs[1], false);
						}
						continue;
					}
					deviceMap.put(keyStrs[1], value);
				}
			}
			DataUtils.transMap2Bean2(deviceMap, measureGroupEntity);
			ValidatorUtils.validateEntity(measureGroupEntity, i);
			measureGroupEntity.setCreateBy((Integer) map.get("userID"));
			measureGroupEntityList.add(measureGroupEntity);
		}
		try {
			measureGroupService.insertBatch(measureGroupEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("批量导入失败", 500);
		}

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