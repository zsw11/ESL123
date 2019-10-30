package io.apj.modules.sys.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.common.annotation.SysLog;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.common.validator.Assert;
import io.apj.common.validator.ValidatorUtils;
import io.apj.common.validator.group.AddGroup;
import io.apj.common.validator.group.UpdateGroup;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.form.PasswordForm;
import io.apj.modules.sys.service.SysUserRoleService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.vo.SysUserEntityVo;

/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		// 只有超级管理员，才能查看所有管理员列表
//		if(getUserId() != Constant.SUPER_ADMIN){
//			params.put("createBy", getUserId());
//		}
		PageUtils page = sysUserService.queryPage(params);

		List<?> list = page.getData();
		return RD.listReturn(list, list.size());
	}

	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public ResponseEntity<Object> info() {
		return RD.ok(getUser());
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	public ResponseEntity<Object> password(@RequestBody PasswordForm form) {
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");

		// sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		// sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

		// 更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if (!flag) {
			return RD.FORBIDDEN("OLD_PASSWORD_INVAILD", "原密码不正确");
		}

		return RD.ok(RD.build());
	}

	/**
	 * 重置用户密码
	 */
	@SysLog("重置用户密码")
	@RequestMapping("/reset")
	@RequiresPermissions("sys:user:reset")
	public ResponseEntity<Object> Reset(@RequestBody Map<String, Object> params) {

		if (params.get("userId") == null || params.get("password") == null) {
//			return R.error(400, "参数错误！");
			return RD.BAD_REQUEST();
		}

		SysUserEntity user = sysUserService.selectById(Integer.parseInt(params.get("userId").toString()));
		if (user == null) {
			return RD.FORBIDDEN("USER_IS_NOT_EXSIST", "该用户不存在！");
		}

		// sha256加密
		String newPassword = new Sha256Hash((String) params.get("password"), user.getSalt()).toHex();

		// 更新密码
		user.setPassword(newPassword);
		sysUserService.updateById(user);

		return RD.ok(SysUserEntityVo.makeVo(sysUserService.selectById(user)));
	}

	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public ResponseEntity<Object> info(@PathVariable("userId") Long userId) {
		SysUserEntity user = sysUserService.selectById(userId);

		// 获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);

		return RD.success(SysUserEntityVo.makeVo(user));
	}

	/**
	 * 用户信息
	 */
	@GetMapping("/detail")
	@RequiresPermissions("sys:user:info")
	public ResponseEntity<Object> detail(@RequestParam Long id) {
		SysUserEntity user = sysUserService.selectById(id);

		// 获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(id);
		user.setRoleIdList(roleIdList);

		return RD.ok(SysUserEntityVo.makeVo(user));
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/create")
	@RequiresPermissions("sys:user:create")
	public ResponseEntity<Object> save(@RequestBody SysUserEntityVo vo) {
		SysUserEntity user = SysUserEntityVo.makeEntity(vo);
		ValidatorUtils.validateEntity(user, AddGroup.class);

		user.setCreateBy(getUserId());
		user.setDeptId(Long.valueOf(1));
		sysUserService.save(user);

		return RD.ok(RD.build().put("status", 200));
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PutMapping("/update")
	@RequiresPermissions("sys:user:update")
	@Transactional
	public ResponseEntity<Object> update(@RequestBody SysUserEntityVo vo) {
		SysUserEntity user = SysUserEntityVo.makeEntity(vo);
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		if (user.getId().equals(null)) {
			return RD.FORBIDDEN("NUKNOWN_ERROR", "未知错误，请联系管理员");
		}
		user.setCreateBy(getUserId());
		sysUserService.update(user);
		return RD.ok(SysUserEntityVo.makeVo(sysUserService.selectById(user)));
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public ResponseEntity<Object> delete(@RequestBody Long[] userIds) {
		if (ArrayUtils.contains(userIds, 1L)) {
			return RD.FORBIDDEN("ADMIN_CAN_NOT_DELETE", "系统管理员不能删除");
		}

		if (ArrayUtils.contains(userIds, getUserId())) {
			return RD.FORBIDDEN("ACTIVE_USER_CAN_NOT_DELETE", "当前用户不能删除");
		}

		sysUserService.deleteBatch(userIds);

		return RD.NO_CONTENT(RD.build());
	}
}
