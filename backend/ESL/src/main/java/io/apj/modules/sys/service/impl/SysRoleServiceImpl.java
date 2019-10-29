package io.apj.modules.sys.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.apj.common.annotation.DataFilter;
import io.apj.common.exception.RRException;
import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.sys.dao.SysRoleDao;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.entity.SysRoleEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysRoleMenuService;
import io.apj.modules.sys.service.SysRoleService;
import io.apj.modules.sys.service.SysUserRoleService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.vo.SysRoleEntityVo;

/**
 * 角色
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:45:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	@DataFilter(subDept = false, user = true)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");
		Long createBy = (Long) params.get("createBy");

		Page<SysRoleEntity> page = this.selectPage(new Query<SysRoleEntity>(params).getPage(),
				new EntityWrapper<SysRoleEntity>().like(StringUtils.isNotBlank(roleName), "role_name", roleName)
						.eq(createBy != null, "create_by", createBy));

		// 数据过滤
		for (SysRoleEntity sysRoleEntity : page.getRecords()) {
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
			sysRoleEntity.setPerms(sysUserService.getDeptAllPerms(sysRoleEntity.getDeptId(), "sys:role:"));
			if (sysDeptEntity != null) {
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
		}

		return new PageUtils(page, new SysRoleEntityVo());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {

		this.insert(role);

		// 检查权限是否越权
		checkPrems(role);

		// 保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());

		// 保存角色与部门关系
//		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {

		SysRoleEntity s = this.selectById(role.getId());
		role.setDeptId(s.getDeptId());
		role.setCreateAt(s.getCreateAt());
		role.setCreateBy(s.getCreateBy());
		role.setDeleteAt(s.getDeleteAt());

		this.updateById(role);

		// 检查权限是否越权
		checkPrems(role);

		// 更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());

		// 保存角色与部门关系
//		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 删除角色
		this.deleteBatchIds(Arrays.asList(ids));

		// 删除角色与菜单关联
		sysRoleMenuService.deleteBatch(ids);

		// 删除角色与用户关联
		sysUserRoleService.deleteBatch(ids);

		// 删除角色与部门关联
//      	sysRoleDeptService.deleteBatch(roleIds);

//		// 逻辑删除
//		List<SysRoleEntity> list = this.selectBatchIds(Arrays.asList(roleIds));
//		for (SysRoleEntity item : list) {
//			item.setDeleteAt(new Date());
//		}
//		this.updateBatchById(list, list.size());

	}

	@Override
	public List<Long> queryRoleIdList(Long createBy) {
		return baseMapper.queryRoleIdList(createBy);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role) {
		// 如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if (role.getCreateBy() == Constant.SUPER_ADMIN) {
			return;
		}

		// 查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateBy());

		// 判断是否越权
		if (!menuIdList.containsAll(role.getMenuIdList())) {
			throw new RRException("新增角色的权限，已超出你的权限范围");
		}
	}
}
