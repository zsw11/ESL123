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

package io.apj.modules.sys.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.apj.common.utils.Constant;
import io.apj.common.utils.MapUtils;
import io.apj.modules.sys.dao.SysMenuDao;
import io.apj.modules.sys.entity.SysMenuEntity;
import io.apj.modules.sys.service.SysMenuService;
import io.apj.modules.sys.service.SysRoleMenuService;
import io.apj.modules.sys.service.SysUserService;
import io.apj.modules.sys.vo.SysMenuEntityVo;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysMenuService sysMenuService;

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}

		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}

		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.deleteById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.deleteByMap(new MapUtils().put("id", menuId));
	}

	// 递归菜单
	private List<SysMenuEntityVo> getAllMenuSList(Long parentId, List<Long> menuIdList){
		// 通过parent_id获取父菜单
		List<SysMenuEntity> menuList = sysMenuService.selectByMap(new MapUtils().put("parent_id", parentId));
		List<SysMenuEntityVo> voList = new ArrayList<>();
		// type=2 为按钮
		if(menuList.size() > 0 && menuList.get(0).getType() == 2) {
//			for(SysMenuEntity menu : menuList) {
//				if(menuIdList.contains(menu.getMenuId()) &&
//						!(StringUtils.isNotBlank(menu.getUrl()) &&
//								"approve".equals(menu.getUrl()))) {
//					SysMenuEntityVo vo = SysMenuEntityVo.makeVo(menu);
//					vo.setSubMenus(getAllMenuSList(vo.getId(), menuIdList));
//					voList.add(vo);
//				}
//			}
			return voList;
		}
		// 递归
		for(SysMenuEntity menu : menuList) {
			// 过滤菜单，过滤审批管理菜单
			if(menuIdList.contains(menu.getId()) &&
					!(StringUtils.isNotBlank(menu.getUrl()) &&
							"approve".equals(menu.getUrl()))) {
				SysMenuEntityVo vo = SysMenuEntityVo.makeVo(menu);
				vo.setSubMenus(getAllMenuSList(vo.getId(), menuIdList));
				voList.add(vo);
			}
		}
		return voList;
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);

		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}

		return subMenuList;
	}

	@Override
	public List<SysMenuEntityVo> getUserMenusList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			List<SysMenuEntityVo> list = new ArrayList<SysMenuEntityVo>();
			// 过滤审批管理菜单
			for (SysMenuEntity menu : getAllMenuList(null)) {
				if (!(StringUtils.isNotBlank(menu.getUrl()) && "approve".equals(menu.getUrl()))) {
					SysMenuEntityVo vo = SysMenuEntityVo.makeVo(menu);
					List<SysMenuEntityVo> sons = new ArrayList<>();
					EntityWrapper<SysMenuEntity> entityWrapper = new EntityWrapper<>();
					entityWrapper.eq("parent_id", menu.getId()).ne("type", 2);
					for(SysMenuEntity m : sysMenuService.selectList(entityWrapper)) {
						sons.add(SysMenuEntityVo.makeVo(m));
					}
					vo.setSubMenus(sons);
					list.add(vo);
				}
			}
			return list;
		}

		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);

		// 添加parent_id
		List<Long> menusList = new ArrayList<>();
		menusList.addAll(menuIdList);
		for (Long id : menuIdList) {
			Long praentId = sysMenuService.selectById(id).getParentId();
			if(!menusList.contains(praentId)) {
				menusList.add(praentId);
			}
		}

		// 递归构建菜单结构
		return getAllMenuSList(0L,menusList);
	}
}
