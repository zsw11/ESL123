package io.apj.modules.masterData.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.DataUtils;
import io.apj.common.utils.RD;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.OpertaionGroupDao;
import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.OpertaionGroupService;
import org.springframework.transaction.annotation.Transactional;

@Service("opertaionGroupService")
public class OpertaionGroupServiceImpl extends ServiceImpl<OpertaionGroupDao, OpertaionGroupEntity>
		implements OpertaionGroupService {
	@Autowired
	private OpertaionGroupService opertaionGroupService;
	@Autowired
	private OperationGroupOperationService operationGroupOperationService;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private StaffService staffService;

	@Override
	@DataFilter(subDept = true, user = true, deptId = "dept_id")
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("remark") != null && params.get("remark") != "", "remark",
						(String) params.get("remark"))
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
		if (params.get("code") != null && params.get("code") != "") {
			entityWrapper.like("UPPER(code)", params.get("code").toString().toUpperCase());
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("frequency"))) {
			entityWrapper.eq("frequency", Integer.parseInt((String) params.get("frequency")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("usedCount"))) {
			entityWrapper.eq("used_count", Integer.parseInt((String) params.get("usedCount")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
			entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("createBy"))) {
			entityWrapper.eq("create_By", Integer.parseInt((String) params.get("createBy")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
			entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
		}

		Page<OpertaionGroupEntity> page = this.selectPage(new Query<OpertaionGroupEntity>(params).getPage(),
				entityWrapper);
		for (OpertaionGroupEntity entity : page.getRecords()) {
			EntityWrapper<OperationGroupOperationEntity> operationWrapper = new EntityWrapper();
			operationWrapper.isNull("delete_at").eq("operation_group_id", entity.getId());
			entity.setCount(operationGroupOperationService.selectCount(operationWrapper));
			entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
			entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
			entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));

		}

		return new PageUtils(page);
	}

	@Override
	@Transactional
	public ResponseEntity<Object> insertOpGroup(Map<String, Object> map, OpertaionGroupEntity opertaionGroup)
			throws InvocationTargetException, IllegalAccessException, IntrospectionException {
//		OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("operationGroup"), true),
//				OpertaionGroupEntity.class);
////		model.setDeptId(getUserDeptId().intValue());
//		opertaionGroup.setDeptId(getUserDeptId().intValue());
		opertaionGroupService.insert(opertaionGroup);
		EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("code", (opertaionGroup.getCode()));
		OpertaionGroupEntity opertaionGroupEntity = opertaionGroupService.selectOne(entityWrapper);
		// 主表id,更新到子表中
		int id = opertaionGroupEntity.getId();
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("operations");
		for (int i = 0; i < maps.size(); i++) {
			OperationGroupOperationEntity operationGroupOperationEntity = new OperationGroupOperationEntity();
			DataUtils.transMap2Bean2(maps.get(i), operationGroupOperationEntity);
			operationGroupOperationEntity.setOperationGroupId(id);
			operationGroupOperationEntity.setPinyin(PinyinUtil.getPinYin(operationGroupOperationEntity.getOperation()));
			operationGroupOperationService.insert(operationGroupOperationEntity);
		}

		return RD.ok(opertaionGroup);
	}

	@Override
	@Transactional
	public ResponseEntity<Object> UpdataOpertaionGroup(Map<String, Object> map) {
		OpertaionGroupEntity opertaionGroup = new OpertaionGroupEntity();
		DataUtils.transMap2Bean2((Map<String, Object>) map.get("operationGroup"), opertaionGroup);
//		opertaionGroup.setUpdateBy((Integer) map.get("updateBy"));
		// 更新主表
		opertaionGroupService.updatePinAndDataById(opertaionGroup);
		// 删除原来子表
		EntityWrapper<OperationGroupOperationEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("operation_group_id", opertaionGroup.getId());
		operationGroupOperationService.deleteByWrapper(entityWrapper);

		// 遍历子表
		List<Map<String, Object>> operations = (List<Map<String, Object>>) map.get("operations");
		for (int i = 0; i < operations.size(); i++) {
			OperationGroupOperationEntity operationGroupOperationEntity = new OperationGroupOperationEntity();
//			DataUtils.transMap2Bean2(operations.get(i), operationGroupOperationEntity);
			operationGroupOperationEntity = JSON.parseObject(JSON.toJSONString(operations.get(i)),
					OperationGroupOperationEntity.class);
			operationGroupOperationEntity.setOperationGroupId(opertaionGroup.getId());
			operationGroupOperationEntity.setPinyin(PinyinUtil.getPinYin(operationGroupOperationEntity.getOperation()));
			operationGroupOperationService.insert(operationGroupOperationEntity);
		}

		return RD.ok(opertaionGroup);
	}

	@Override
	public void deleteList(List<OpertaionGroupEntity> opertaionGroupEntityList) {
		for (OpertaionGroupEntity item : opertaionGroupEntityList) {
			item.setDeleteAt(new Date());
		}
		if (opertaionGroupEntityList.size() > 0) {
			this.updateAllColumnBatchById(opertaionGroupEntityList);
		}

	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		List<OpertaionGroupEntity> opertaionGroupEntityList = this.selectBatchIds(ids);
		for (OpertaionGroupEntity item : opertaionGroupEntityList) {
			item.setDeleteAt(new Date());
		}
		if (opertaionGroupEntityList.size() > 0) {
			this.updateAllColumnBatchById(opertaionGroupEntityList);
		}
	}

	@Override
	public void deleteByWrapper(Wrapper<OpertaionGroupEntity> wrapper) {
		List<OpertaionGroupEntity> opertaionGroupEntityList = this.selectList(wrapper);
		for (OpertaionGroupEntity item : opertaionGroupEntityList) {
			item.setDeleteAt(new Date());
		}
		if (opertaionGroupEntityList.size() > 0) {
			this.updateAllColumnBatchById(opertaionGroupEntityList);
		}
	}

	@Override
	public void updatePinAndDataById(OpertaionGroupEntity opertaionGroupEntity) {
		opertaionGroupEntity.setUpdateAt(new Date());
		this.updateById(opertaionGroupEntity);
	}

}