package io.apj.modules.sys.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.common.utils.RD;
import io.apj.modules.basic.entity.StaffEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.form.SysLoginForm;
import io.apj.modules.sys.service.SysCaptchaService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.service.SysUserTokenService;
import io.apj.modules.sys.vo.SysUserEntityVo;

/**
 * 登录相关
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
//	@PostMapping("/sys/login")
	@PutMapping("/api/v1/passport/login")
	public ResponseEntity<Object> login(@RequestBody SysLoginForm form) throws IOException {
//			boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
//			if(!captcha){
//				return R.error("验证码不正确");
//			}
// 			账号不存在、密码错误
//			if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
//				return R.error("账号或密码不正确！");
//			}
// 			账号锁定
//			if (user.getStatus() == 0)
//				return R.error("账号已被锁定,请联系管理员！");
//			生成token，并保存到数据库
//			R r = sysUserTokenService.createToken(user.getUserId());
//			获取人员信息
//			MemberEntity memebr = memebrService.selectOne(
//				new EntityWrapper<MemberEntity>().eq("user_id", user.getUserId()));
//			if (memebr == null) {
//			return R.error("该账号人员信息不全,请联系管理员！");
//			}
//			r.put("member", memebr);
//			初始化用户数据权限
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("userId", user.getUserId());
//			sysUserService.initUserDataFilter(map);
		// 用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
		// 判断用户是否存在
		if (user == null) {
			return RD.UNAUTHORIZED("USER_NOT_EXIST", "用户不存在");
		}
//		获取人员信息
		StaffEntity staff = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("user_id", user.getId()));
		if (staff == null) {
			return RD.UNAUTHORIZED("USER_NOT_EXIST", "用户不存在");
		}

		// 判断密码
		if (!user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex()))
			return RD.UNAUTHORIZED("WRONG_PASSWORD", "密码错误");

		// 账号锁定
		if (user.getStatus() == 0)
			return RD.UNAUTHORIZED("USER_LOCK", "账号已被锁定");

		return RD.ok(SysUserEntityVo.makeVoToken(user, sysUserTokenService.createTokenRD(user.getId())));
	}

	/**
	 * 获取用户信息
	 * 
	 * @return RD
	 */
	@GetMapping("/api/v1/passport/userdetail")
	public ResponseEntity<Object> userDetail() {
		return RD.ok(SysUserEntityVo.makeVo(getUser()));
	}

	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public ResponseEntity<Object> logout() {
		sysUserTokenService.logout(getUserId());
		return RD.success(RD.build().put("status", 200));
	}

}
