package io.apj.modules.sys.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.R;
import io.apj.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);
	
	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	String createTokenRD(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);
	
	
	/**
	 * 登录时初始化用户数据权限
	 */
	void initUserDF(Map<String, Object> params);

}
