package io.apj.modules.sys.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.apj.modules.sys.entity.SysUserEntity;
import lombok.Data;

/**
 * 系统用户vo
 * 
 * @author lixinan
 *
 */
@Data
public class SysUserEntityVo implements BaseVo<SysUserEntityVo, SysUserEntity> {

	// 获取用户信息
	public static SysUserEntityVo makeVoToken(SysUserEntity user, String token) {
		SysUserEntityVo vo = new SysUserEntityVo();
		vo.id = user.getId();
		vo.username = user.getUsername();
		vo.mobile = user.getMobile();
		vo.email = user.getEmail();
		vo.createAt = user.getCreateAt();
		vo.token = token;
		return vo;
	};

	// vo 转 entity
	public static SysUserEntity makeEntity(SysUserEntityVo vo) {
		SysUserEntity entity = new SysUserEntity();
		entity.setId(vo.getId());
		entity.setUsername(vo.getUsername());
		entity.setEmail(vo.getEmail());
		entity.setMobile(vo.getMobile());
		entity.setStatus("new".equals(vo.getStatus()) ? 1 : 0);
		entity.setRoleIdList(vo.getRoleIds());
		entity.setPassword(vo.getPassword());
		return entity;
	}

	// entity 转 vo
	public static SysUserEntityVo makeVo(SysUserEntity user) {
		SysUserEntityVo vo = new SysUserEntityVo();
		vo.id = user.getId();
		vo.username = user.getUsername();
		vo.mobile = user.getMobile();
		vo.email = user.getEmail();
		vo.createAt = user.getCreateAt();
		vo.status = user.getStatus() == 0 ? "locked" : "new";
		vo.deptName = user.getDeptName();
		vo.setRoles(makeRoles(user.getRoleIdList()));
		vo.setRoleIds(user.getRoleIdList());
		return vo;
	}

	// elist 转 vlist
	@Override
	public List<SysUserEntityVo> makeVoList(List<?> elist) {
		List<SysUserEntityVo> vlist = new ArrayList<>();
		if (elist != null) {
			for (Object e : elist) {
				vlist.add(SysUserEntityVo.makeVo((SysUserEntity) e));
			}
		}
		return vlist;
	}

	// roleIds 转 roles
	private static List<Map<String, Object>> makeRoles(List<Long> roleIds) {
		List<Map<String, Object>> roles = new ArrayList<Map<String, Object>>();
		if (roleIds != null) {
			for (Long id : roleIds) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", id);
				roles.add(map);
			}
		}
		return roles;
	}

	/**
	 * id
	 */
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码（加密）
	 */
	private String password;

	/**
	 * 电话
	 */
	private String mobile;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 角色id
	 */
	private List<Long> roleIds;

	/**
	 * 角色
	 */
	private List<Map<String, Object>> roles;

	/**
	 * token
	 */
	private String token;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 状态
	 */
	private String status;
	/**
	 * 部门名称
	 */
	private String deptName;
	

}
