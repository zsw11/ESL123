package io.apj.modules.sys.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import io.apj.modules.basic.entity.StaffEntity;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.entity.SysUserDataFilterEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.service.ReferenceService;
import io.apj.modules.sys.service.SysUserService;

/**
 * Controller公共组件
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
	@Autowired
	ReferenceService referenceService;
	@Autowired
	StaffService staffService;
//	@Autowired
//	private SysDeptService sysDeptService;
//	@Autowired
//	private SysUserRoleService sysUserRoleService;
//	@Autowired
//	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserService sysUserService;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getId();
	}

	protected Long getUserDeptId() {
		return getUser().getDeptId();
	}

	protected StaffEntity getStaff() {
		StaffEntity staff = staffService.selectOne(
				new EntityWrapper<StaffEntity>().eq("user_id", getUser().getId()));

		return staff;
	}

	protected Long getStaffId() {
		return getStaff().getId();
	}

	protected Long getStaffDeptId() {
		return getStaff().getDeptId();
	}

	/**
	 * 引用表保存和更新
	 *
	 * @param mainEntity
	 * @param mainId
	 * @param byEntity
	 * @param byId
	 * @param ifUpdate
	 */
	public void insertTableReference(String mainEntity, Long mainId, String byEntity, Long byId, Boolean ifUpdate) {
		if (ifUpdate) {
			// 先删除对应主表的引用数据，再保存引用数据
			Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>()
					.eq("main_entity", mainEntity).eq("main_id", mainId);
			referenceService.delete(referenceEntityWrapper);
		}
		if (byId != null) {
			ReferenceEntity reference = new ReferenceEntity();
			reference.setMainEntity(mainEntity);
			reference.setMainId(mainId);
			reference.setByEntity(byEntity);
			reference.setById(byId);
			referenceService.save(reference);
		}
	}

	/**
	 * 查询引用表数据
	 *
	 * @param byEntity
	 * @param byId
	 * @return
	 */
	public List<ReferenceEntity> deleteCheckReference(String byEntity, Long byId) {
		// 查询引用表数据
		Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>().eq("by_entity", byEntity)
				.eq("by_id", byId);
		List<ReferenceEntity> referenceEntities = referenceService.selectList(referenceEntityWrapper);
		return referenceEntities;
	}

	/**
	 * 删除此表的引用数据
	 *
	 * @param mainEntity
	 * @param mainId
	 */
	protected void deleteTableReference(String mainEntity, Long mainId) {
		// 删除对应主表的引用数据
		Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>()
				.eq("main_entity", mainEntity).eq("main_id", mainId);
		referenceService.delete(referenceEntityWrapper);
	}

	/**
	 * 准确的删除一条
	 *
	 * @param mainEntity
	 * @param mainId
	 * @param byEntity
	 * @param byId
	 */
	public void deleteOneReference(String mainEntity, Long mainId, String byEntity, Long byId) {

		// 先删除对应主表的引用数据，再保存引用数据
		Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>()
				.eq("main_entity", mainEntity).eq("main_id", mainId).eq("by_entity", byEntity).eq("by_id", byId);
		referenceService.delete(referenceEntityWrapper);

	}

	/**
	 * 高级查询数据过滤权限
	 */
	public String getSqlFilter(SysUserEntity user) {

		SysUserDataFilterEntity filter = sysUserService.queryByUserDataFilter(user.getId());
		return filter.getFilter();
	}
//		Set<Long> deptIdList = new HashSet<>();
//		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
//		if (roleIdList.size() > 0) {
//			List<Long> userDeptIdList = sysRoleDeptService
//					.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
//			deptIdList.addAll(userDeptIdList);
//		}
//
//		// 用户子部门ID列表
//		List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
//		deptIdList.addAll(subDeptIdList);
//
//		StringBuilder sqlFilter = new StringBuilder();
//		sqlFilter.append(" (");
//
//		if (deptIdList.size() > 0) {
//			sqlFilter.append("dept_id").append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
//		}
//		// 没有本部门数据权限，也能查询本人数据
//		if (deptIdList.size() > 0) {
//			sqlFilter.append(" or ");
//			sqlFilter.append("create_by").append("=").append(user.getUserId());
//		}
//		sqlFilter.append(")");
//		return sqlFilter.toString();
//	}

}
