package io.apj.modules.sys.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.common.annotation.SysLog;
import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.sys.entity.SysRoleEntity;
import io.apj.modules.sys.service.SysRoleDeptService;
import io.apj.modules.sys.service.SysRoleMenuService;
import io.apj.modules.sys.service.SysRoleService;
import io.apj.modules.sys.vo.SysRoleEntityVo;

/**
 * 角色管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月8日 下午2:18:33
 */
@RestController
//@RequestMapping("/sys/role")
@RequestMapping("/api/v1/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;

	/**
	 * 角色列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		// 如果不是超级管理员，则只查询自己创建的角色列表
		if (getUserId() != Constant.SUPER_ADMIN) {
			params.put("createBy", getUserId());
		}

		PageUtils page = sysRoleService.queryPage(params);

		return RD.listReturn(page.getData(), page.getTotalCount());
	}

	/**
	 * 角色列表
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select() {

		EntityWrapper<SysRoleEntity> roleWrapper = new EntityWrapper<>();

		// 如果不是超级管理员，则只查询自己所拥有的角色列表
		if (getUserId() != Constant.SUPER_ADMIN) {
			roleWrapper.eq("create_by", getUserId());
		}
		List<SysRoleEntity> list = sysRoleService.selectList(roleWrapper);

		return R.ok().put("list", list);
	}

	/**
	 * 角色信息
	 */
//	@GetMapping("/info/{roleId}")
//	@RequiresPermissions("sys:role:info")
//	public R info(@PathVariable("roleId") Long roleId) {
//		SysRoleEntity role = sysRoleService.selectById(roleId);
//
//		// 查询角色对应的菜单
//		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
//		role.setMenuIdList(menuIdList);
//
//		// 查询角色对应的部门
//		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[] { roleId });
//		role.setDeptIdList(deptIdList);
//
//		return R.ok().put("role", role);
//	}

	/**
	 * 角色信息
	 */
	@GetMapping("/detail")
	@RequiresPermissions("sys:role:info")
	public ResponseEntity<Object> detail(@RequestParam Long id) {
		return RD.ok(info(id));
	}

	/**
	 * 通过id构造返回结果vo
	 * 
	 * @param id
	 * @return SysRoleEntityVo
	 */
	private SysRoleEntityVo info(Long id) {
		SysRoleEntity role = sysRoleService.selectById(id);

		// 查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(id);
		role.setMenuIdList(menuIdList);

		// 查询角色对应的部门
		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[] { id });
		role.setDeptIdList(deptIdList);

		return SysRoleEntityVo.makeVo(role).setMenusThis(sysRoleMenuService.queryMenuVoList(id));
	}

	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@PostMapping("/create")
	@RequiresPermissions("sys:role:create")
	public ResponseEntity<Object> save(@RequestBody SysRoleEntityVo vo) {
		SysRoleEntity role = SysRoleEntityVo.makeEntity(vo);
		role.setDeptId(getUserDeptId());
		ValidatorUtils.validateEntity(role);
		role.setCreateBy(getUserId());
		role.setCreateAt(new Date());
		role.setUpdateAt(new Date());
		sysRoleService.save(role);

		return RD.ok(RD.build());
	}

	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@PutMapping("/update")
	@RequiresPermissions("sys:role:update")
	public ResponseEntity<Object> update(@RequestBody SysRoleEntityVo vo) {
		SysRoleEntity role = SysRoleEntityVo.makeEntity(vo);
		SysRoleEntity r = sysRoleService.selectById(vo.getId());
		role.setCreateBy(r.getCreateBy());
		ValidatorUtils.validateEntity(role);
		role.setUpdateBy(getUserId());
		role.setUpdateAt(new Date());
//		role.setCreateBy(getUserId());
		sysRoleService.update(role);

		return RD.ok(info(role.getId()));
	}

	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@PostMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public ResponseEntity<Object> delete(@RequestBody Long[] id) {
		sysRoleService.deleteBatch(id);
		return RD.NO_CONTENT(RD.build());
	}
}
