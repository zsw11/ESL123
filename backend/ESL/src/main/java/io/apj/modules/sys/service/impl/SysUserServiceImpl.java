package io.apj.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
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
import io.apj.modules.basic.entity.StaffEntity;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.sys.dao.SysUserDao;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.entity.SysUserDataFilterEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysRoleService;
import io.apj.modules.sys.service.SysUserRoleService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.vo.SysUserEntityVo;

/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private StaffService staffService;

	@Override
	@DataFilter(subDept = true, user = true)
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<SysUserEntity> entityWrapper = new EntityWrapper<SysUserEntity>();
		entityWrapper
				.like(StringUtils.isNotEmpty((String) params.get("username")), "username",
						(String) params.get("username"))
				.like(StringUtils.isNotEmpty((String) params.get("email")), "email", (String) params.get("email"))
				.like(StringUtils.isNotEmpty((String) params.get("mobile")), "mobile", (String) params.get("mobile"))
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
		EntityWrapper<StaffEntity> staffrWrapper = new EntityWrapper<>();
		List<StaffEntity> staffList = staffService.selectList(staffrWrapper);
		List<Long> userIds = new ArrayList<Long>();
		for (StaffEntity item : staffList) {
			if (item.getUserId() != null) {
				userIds.add(item.getUserId());
			}
		}
		if (StringUtils.isNotEmpty((String) params.get("keyword"))) {
			String name = (String) params.get("keyword");
			name = name.replace("'", "");
			entityWrapper.like("username", name).notIn(userIds.size() > 0, "id", userIds);
		}
		Page<SysUserEntity> page = this.selectPage(new Query<SysUserEntity>(params).getPage(), entityWrapper);
		for (SysUserEntity sysUserEntity : page.getRecords()) {
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName());
			sysUserEntity.setPerms(this.getDeptAllPerms(sysUserEntity.getDeptId(), "sys:user:"));
			if (userIds.contains(sysUserEntity.getId())) {
				sysUserEntity.setIfUsed(true);
			}
		}

		return new PageUtils(page, new SysUserEntityVo());
	}

	/**
	 * 登录时初始化用户数据权限
	 */
	@Override
	@DataFilter(tableAlias = "init", subDept = true, user = false)
	public void initUserDataFilter(Map<String, Object> params) {
		staffService.selectOne(new EntityWrapper<StaffEntity>().eq("user_id", params.get("userId"))
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateAt(new Date());
		// sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.insert(user);

		// 检查角色是否越权
		checkRole(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword(null);
		} else {
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);

		// 检查角色是否越权
		checkRole(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.deleteBatchIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity, new EntityWrapper<SysUserEntity>().eq("id", userId).eq("password", password));
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user) {
		if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
			return;
		}
		// 如果不是超级管理员，则需要判断用户的角色是否自己创建
		if (user.getCreateBy() == Constant.SUPER_ADMIN) {
			return;
		}

		// 查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateBy());

		// 判断是否越权
		if (!roleIdList.containsAll(user.getRoleIdList())) {
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}

	/**
	 * 新增用户权限数据
	 */
	@Override
	public int insertUserDataFilter(SysUserDataFilterEntity userDataFilter) {
		return baseMapper.insertUserDataFilter(userDataFilter);
	}

	/**
	 * 更新用户权限数据
	 */
	@Override
	public int updatetUserDataFilter(SysUserDataFilterEntity userDataFilter) {
		return baseMapper.updatetUserDataFilter(userDataFilter);
	}

	/**
	 * 根据用户Id，查询用户权限数据
	 */
	@Override
	public SysUserDataFilterEntity queryByUserDataFilter(long userId) {
		return baseMapper.queryByUserDataFilter(userId);
	}

	/**
	 * 查询用户对应部门所有权限
	 */
	@Override
	public String[] getDeptAllPerms(Long deptId, String type) {
		String[] list = new String[] {};

//		SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
//
//
//		if (deptId == null || type == null || user == null) {
//			return list;
//		}
//
//		if (user.getUserId() == Constant.SUPER_ADMIN) { // 超级管理员返回所有权限
//
//			return new String[] { "all" };
//
//		} else {
//
//			String perms = baseMapper.getDeptAllPerms(user.getUserId(), deptId, "%" + type + "%");
//			if (perms != null) {
//				list = perms.split(",");
//			}
//		}

		return list;
	}
}
