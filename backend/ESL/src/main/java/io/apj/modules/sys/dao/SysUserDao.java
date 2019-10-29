package io.apj.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.apj.modules.sys.entity.SysUserDataFilterEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

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
	 * @param deptId 部门ID
	 * @param type   权限类型
	 */
	String getDeptAllPerms(Long userId, Long deptId, String type);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

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

}
