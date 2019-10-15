/**
 * Copyright 2019 爱浦京产品
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.apj.modules.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.Constant;
import io.apj.common.utils.RD;
import io.apj.modules.sys.entity.SysMenuEntity;
import io.apj.modules.sys.service.ShiroService;
import io.apj.modules.sys.service.SysMenuService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.vo.SysMenuEntityVo;

/**
 * 系统菜单
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:58:15
 */
@RestController
//@RequestMapping("/sys/menu")
@RequestMapping("/api/v1/menu")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;
	@Autowired
	private SysUserService sysUserService;

	/**
	 * 导航菜单
	 */
//	@GetMapping("/nav")
	@GetMapping("/navtree")
	public ResponseEntity<Object> nav() {
//		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		// 过滤审批管理菜单
//		for (SysMenuEntity menu : menuList) {
//			if (!(StringUtils.isNotBlank(menu.getUrl()) && "approve".equals(menu.getUrl()))) {
//				SysMenuEntityVo vo = SysMenuEntityVo.makeVo(menu);
//				List<SysMenuEntityVo> sons = new ArrayList<>();
//				EntityWrapper<SysMenuEntity> entityWrapper = new EntityWrapper<>();
//				entityWrapper.eq("parent_id", menu.getMenuId());
//				for(SysMenuEntity m : sysMenuService.selectList(entityWrapper)) {
//					sons.add(SysMenuEntityVo.makeVo(m));
//				}
//				vo.setSubMenus(sons);
//				list.add(vo);
//			}
//		}
		List<SysMenuEntityVo> list = sysMenuService.getUserMenusList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return RD.success(RD.build().put("status", 200).put("data", list).put("permissions", permissions));
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public ResponseEntity<Object> list() {
		EntityWrapper<SysMenuEntity> wrapper = new EntityWrapper<>();
		// 用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(getUserId());
		List<SysMenuEntity> menuList = sysMenuService.selectList(wrapper.in("id", menuIdList));
//		for (SysMenuEntity sysMenuEntity : menuList) {
//			SysMenuEntity parentMenuEntity = sysMenuService.selectById(sysMenuEntity.getParentId());
//			if (parentMenuEntity != null) {
//				sysMenuEntity.setParentName(parentMenuEntity.getName());
//			}
//		}
		List<SysMenuEntityVo> list = new ArrayList<>();
		for (SysMenuEntity menu : menuList) {
			SysMenuEntityVo vo = SysMenuEntityVo.makeVo(menu);
			list.add(vo);
		}
		return RD.listReturn(list, list.size());
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public ResponseEntity<Object> select() {
		// 查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		// 添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		List<Object> list = new ArrayList<>();
		for (SysMenuEntity menu : menuList) {
			SysMenuEntityVo vo = SysMenuEntityVo.makeVo(menu);
			list.add(vo);
		}
		return RD.listReturn(list, list.size());
	}

	/**
	 * 菜单信息
	 */
	@GetMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public ResponseEntity<Object> info(@PathVariable("menuId") Long menuId) {
		SysMenuEntity menu = sysMenuService.selectById(menuId);
		return RD.ok(SysMenuEntityVo.makeVo(menu));
	}

	/**
	 * 菜单信息
	 */
	@GetMapping("/detail")
	@RequiresPermissions("sys:menu:info")
	public ResponseEntity<Object> detail(@RequestParam Long id) {
		SysMenuEntity menu = sysMenuService.selectById(id);
		return RD.ok(SysMenuEntityVo.makeVo(menu));
	}

	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@PostMapping("/create")
	@RequiresPermissions("sys:menu:create")
	public ResponseEntity<Object> save(@RequestBody SysMenuEntityVo vo) {

		SysMenuEntity menu = SysMenuEntityVo.makeEntity(vo);

		menu.setCreateBy(getUserId());
		menu.setCreateAt(new Date());
		menu.setUpdateAt(new Date());

		// 数据校验
		verifyForm(menu);

		sysMenuService.insert(menu);

		return RD.success(RD.build().put("status", 200));
	}

	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@PutMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public ResponseEntity<Object> update(@RequestBody SysMenuEntityVo vo) {

		SysMenuEntity m = sysMenuService.selectById(vo.getId());
		SysMenuEntity menu = SysMenuEntityVo.makeEntity(vo);
		if (vo.getParentId() == null)
			menu.setParentId(m.getParentId());
		menu.setCreateAt(vo.getCreatedAt());
		menu.setCreateBy(m.getCreateBy());
		menu.setUpdateAt(new Date());
		menu.setUpdateBy(getUserId());
		// 数据校验
		verifyForm(menu);

		sysMenuService.updateById(menu);

		return RD.success(RD.build().put("status", 200));
	}

	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public ResponseEntity<Object> delete(@RequestParam Long id) {
		if (id <= 31) {
			return RD.FORBIDDEN("CAN_NOT_DELETE_SYSTEM_MEUN", "系统菜单，不能删除");
		}

		// 判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(id);
		if (menuList.size() > 0) {
			return RD.FORBIDDEN("DELETE_SUBMENU_OR_BUTTON_FIRST", "请先删除子菜单或按钮");
		}

//		sysMenuService.delete(menuId);
		SysMenuEntity menu = sysMenuService.selectById(id);
		menu.setDeleteAt(new Date());
		sysMenuService.updateById(menu);

		return RD.NO_CONTENT(SysMenuEntityVo.makeVo(menu));
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu) {
		if (StringUtils.isBlank(menu.getName())) {
			throw new RRException("菜单名称不能为空", 403);
		}

		if (menu.getParentId() == null) {
			throw new RRException("上级菜单不能为空", 403);
		}

		// 菜单
		if (menu.getType() == Constant.MenuType.MENU.getValue()) {
			if (StringUtils.isBlank(menu.getUrl())) {
				throw new RRException("菜单URL不能为空", 403);
			}
		}

		// 上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if (menu.getParentId() != 0) {
			SysMenuEntity parentMenu = sysMenuService.selectById(menu.getParentId());
			parentType = parentMenu.getType();
		}

		// 目录、菜单
		if (menu.getType() == Constant.MenuType.CATALOG.getValue()
				|| menu.getType() == Constant.MenuType.MENU.getValue()) {
			if (parentType != Constant.MenuType.CATALOG.getValue()) {
				throw new RRException("上级菜单只能为目录类型", 403);
			}
			return;
		}

		// 按钮
		if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
			if (parentType != Constant.MenuType.MENU.getValue()) {
				throw new RRException("上级菜单只能为菜单类型", 403);
			}
			return;
		}
	}
}
