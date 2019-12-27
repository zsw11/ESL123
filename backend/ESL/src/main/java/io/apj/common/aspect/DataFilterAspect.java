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

package io.apj.common.aspect;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.common.annotation.DataFilter;
import io.apj.common.exception.RRException;
import io.apj.common.utils.Constant;
import io.apj.modules.basic.entity.StaffEntity;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.entity.SysUserDataFilterEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.shiro.ShiroUtils;

/**
 * 数据过滤，切面处理类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.0.0 2017-09-17
 */
@Aspect
@Component
public class DataFilterAspect {
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserService sysUserService;
//	@Autowired
//	private SysUserRoleService sysUserRoleService;
//	@Autowired
//	private SysRoleDeptService sysRoleDeptService;

	@Autowired
	private StaffService staffService;

	@Pointcut("@annotation(io.apj.common.annotation.DataFilter)")
	public void dataFilterCut() {

	}

	@Before("dataFilterCut()")
	public void dataFilter(JoinPoint point) throws Throwable {
		Object params = point.getArgs()[0];
		if (params != null && params instanceof Map) {
			SysUserEntity user = ShiroUtils.getUserEntity();

			// 如果不是超级管理员，则进行数据过滤
			if (user == null || user.getId() != Constant.SUPER_ADMIN) {
				Map map = (Map) params;
				if (user == null
						|| (user != null && map.get("userId") != null && user.getId() != (Long) map.get("userId"))) {
					user = sysUserService.selectById((Long) map.get("userId"));
				}
				if (user.getId() != Constant.SUPER_ADMIN) {
					map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
				}
			}

			return;
		}

		throw new RRException("数据权限接口，只能是Map类型参数，且不能为NULL");
	}

	/**
	 * 获取数据过滤的SQL
	 */
	@SuppressWarnings("unlikely-arg-type")
	private String getSQLFilter(SysUserEntity user, JoinPoint point) {

		// 超级管理员不需要过滤
		if (user.getId().equals(1)) {
			return "1=1";
		}

		SysUserDataFilterEntity filter = sysUserService.queryByUserDataFilter(user.getId());

		MethodSignature signature = (MethodSignature) point.getSignature();
		DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
		if ("init".equals(dataFilter.tableAlias())) {
			String filterSql = getSQLFilterFunc(user, point);
			if (filter == null) {
				SysUserDataFilterEntity item = new SysUserDataFilterEntity();
				item.setUserId(user.getId());
				item.setFilter(filterSql);
				item.setUpdateAt(new Date());
				item.setCreateAt(new Date());
				sysUserService.insertUserDataFilter(item);
			} else {
				filter.setFilter(filterSql);
				filter.setUpdateAt(new Date());
				sysUserService.updatetUserDataFilter(filter);
			}
			return filterSql;
		} else {
			return getSQLFilterFunc(user, point);
		}

	}

	private String getSQLFilterFunc(SysUserEntity user, JoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
		// 获取表的别名
		String tableAlias = dataFilter.tableAlias();
		if (StringUtils.isNotBlank(tableAlias)) {
			tableAlias += ".";

			if ("init.".equals(tableAlias)) {
				tableAlias = "";
			}
		}

		// 部门ID列表
		Set<Long> deptIdList = new HashSet<>();

		// 用户角色对应的部门ID列表
//		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
//		if (roleIdList.size() > 0) {
//			List<Long> userDeptIdList = sysRoleDeptService
//					.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
//			deptIdList.addAll(userDeptIdList);
//		}

		// 获取人员信息
		StaffEntity staff = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("user_id", user.getId()));

		// 用户子部门ID列表
		if (dataFilter.subDept() && staff != null) {

			SysDeptEntity dept = sysDeptService.selectById(staff.getDeptId());

			if (dept != null) { // 总部查询所有
				List<Long> subDeptIdList = sysDeptService.getSubDeptIdList((long) 1);
				subDeptIdList.add((long) 1);
				deptIdList.addAll(subDeptIdList);
			} else {
				deptIdList.add(staff.getDeptId());
				List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(staff.getDeptId());
				deptIdList.addAll(subDeptIdList);
			}
		}

		StringBuilder sqlFilter = new StringBuilder();
		sqlFilter.append(" (");

		if (deptIdList.size() > 0) {
			sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(")
					.append(StringUtils.join(deptIdList, ",")).append(")");
		}

		// 没有本部门数据权限，也能查询本人数据
		if (dataFilter.user()) {
			if (deptIdList.size() > 0) {
				sqlFilter.append(" or ");
			}
			sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getId());
			sqlFilter.append(" or ");
			sqlFilter.append(tableAlias).append(dataFilter.createBy()).append("=").append(user.getId());
		}

		sqlFilter.append(")");
		if (sqlFilter.toString().equals(" ()")) {
			sqlFilter = new StringBuilder();
		}
		return sqlFilter.toString();
	}
}
