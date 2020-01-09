/**
 * Copyright 2019 爱浦京产品
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.apj.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.sys.dao.SysDeptDao;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {

	@Autowired
	private SysUserService sysUserService;

	@Override
	@DataFilter(subDept = true, user = false, deptId = "id")
	public List<SysDeptEntity> queryList(Map<String, Object> params) {
		EntityWrapper<SysDeptEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper
				.in(StringUtils.isNotEmpty((String) params.get("deptType")), "dept_type",
						(String) params.get("deptType"))
				.in(StringUtils.isNotEmpty((String) params.get("deptLevel")), "dept_level",
						(String) params.get("deptLevel"))
				.like(StringUtils.isNotEmpty((String) params.get("name")), "name", (String) params.get("name"));

		// 开启数据权限过滤
		if (params.get("filter") == null || !"false".equals(params.get("filter"))) {
			entityWrapper.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null,
					(String) params.get(Constant.SQL_FILTER));
		}
		List<SysDeptEntity> deptList = this.selectList(entityWrapper);
		for (SysDeptEntity sysDeptEntity : deptList) {
			sysDeptEntity.setPerms(sysUserService.getDeptAllPerms(sysDeptEntity.getId(), "sys:dept:"));
			SysDeptEntity parentDeptEntity = this.selectById(sysDeptEntity.getParentId());
			if (parentDeptEntity != null) {
				sysDeptEntity.setParentName(parentDeptEntity.getName());
			}
			if (params.get(Constant.SQL_FILTER) != null) {
				sysDeptEntity
						.setPermission(rtlStatus(params.get(Constant.SQL_FILTER).toString(), sysDeptEntity.getId()));
			} else {
				sysDeptEntity.setPermission(true);
			}

		}
		return deptList;
	}

	/**
	 * 根据数据权限返回拥有权限部门ID
	 *
	 * @param sql
	 * @param deptId
	 * @return
	 */
	private boolean rtlStatus(String sql, Long deptId) {
		if (sql != null && sql.length() > 14) {
			String str = sql.substring(13, sql.length() - 2);
			String[] list = str.split(",");
			boolean bool = false;
			for (String s : list) {
				if (s.equals(deptId.toString())) {
					bool = true;
					break;
				}
			}
			return bool;
		} else {
			return false;
		}

	}

	@Override
	@DataFilter(subDept = true, user = false, deptId = "id")
	public List<SysDeptEntity> queryListByDeptCode(Map<String, Object> params) {

		SysDeptEntity dept = this.selectOne(new EntityWrapper<SysDeptEntity>()
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("id")), "id", params.get("id"))
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("deptCode")), "dept_code",
						params.get("deptCode")));

		if (dept != null) {

			List<Long> list = this.getSubDeptIdList(dept.getId());

			list.add(dept.getId());

			List<SysDeptEntity> deptList = this.selectList(new EntityWrapper<SysDeptEntity>().in("id", list));

			for (SysDeptEntity sysDeptEntity : deptList) {
				SysDeptEntity parentDeptEntity = this.selectById(sysDeptEntity.getParentId());
				if (parentDeptEntity != null) {
					sysDeptEntity.setParentName(parentDeptEntity.getName());
				}
			}
			return deptList;

		} else {
			return null;
		}
	}

	@Override
	@DataFilter(subDept = true, user = false, deptId = "id")
	public List<SysDeptEntity> queryListByName(Map<String, Object> params) {
		List<Long> deptIds = new ArrayList<>();
		EntityWrapper<SysDeptEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.in(StringUtils.isNotEmpty((String) params.get("deptType")), "dept_type",
						(String) params.get("deptType"))
				.in(StringUtils.isNotEmpty((String) params.get("deptLevel")), "dept_level",
						(String) params.get("deptLevel"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("name")), "name", (String) params.get("name"))
				.isNull("delete_at");

		// 开启数据权限过滤
		if (params.get("filter") == null || !"false".equals(params.get("filter"))) {
			entityWrapper.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null,
					(String) params.get(Constant.SQL_FILTER));
		}
		List<SysDeptEntity> deptList = this.selectList(entityWrapper);
		if (deptList.size() > 0) {
			for (SysDeptEntity item : deptList) {
				List<Long> list = this.getSubDeptIdList(item.getId());
				list.add(item.getId());
				for (Long id : list) {
					if (!deptIds.contains(id)) {
						deptIds.add(id);
					}
				}
			}
			List<SysDeptEntity> allDeptList = this
					.selectList(new EntityWrapper<SysDeptEntity>().in("id", deptIds).isNull("delete_at"));

			for (SysDeptEntity sysDeptEntity : allDeptList) {
				SysDeptEntity parentDeptEntity = this.selectById(sysDeptEntity.getParentId());
				if (params.get(Constant.SQL_FILTER) != null) {
					sysDeptEntity.setPermission(
							rtlStatus(params.get(Constant.SQL_FILTER).toString(), sysDeptEntity.getId()));
				} else {
					sysDeptEntity.setPermission(true);
				}
				if (parentDeptEntity != null) {
					sysDeptEntity.setParentName(parentDeptEntity.getName());
				}
			}
			return allDeptList;

		} else {
			return deptList;
		}
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<Long> getSubDeptIdList(Long deptId) {
		// 部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		// 获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		return deptIdList;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList) {
		for (Long deptId : subIdList) {
			List<Long> list = queryDetpIdList(deptId);
			if (list.size() > 0) {
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}
}
