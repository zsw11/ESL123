package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.SysUserDataFilterEntity;
import io.apj.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 查询用户的所有权限
	 * 
	 * @param userId 用户ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户对应部门所有权限
	 * 
	 * @param userId 用户ID
	 * @param type   权限类型
	 */
	String[] getDeptAllPerms(Long deptId, String type);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 保存用户
	 */
	void save(SysUserEntity user);

	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * 
	 * @param userId      用户ID
	 * @param password    原密码
	 * @param newPassword 新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

	/**
	 * 根据用户Id，查询用户权限数据
	 */
	SysUserDataFilterEntity queryByUserDataFilter(long userId);

	/**
	 * 新增用户权限数据
	 */
	int insertUserDataFilter(SysUserDataFilterEntity userDataFilter);

	/**
	 * 更新用户权限数据
	 */
	int updatetUserDataFilter(SysUserDataFilterEntity userDataFilter);

	/**
	 * 登录时初始化用户数据权限
	 */
	void initUserDataFilter(Map<String, Object> params);
}
