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

package io.apj.modules.sys.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:26:39
 */
@TableName("sys_menu")
public class SysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@TableId
	private Long id;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private Long parentId;

	/**
	 * 父菜单名称
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;

	private Long createBy;
	private Date createAt;
	private Long updateBy;
	private Date updateAt;
	private Date deleteAt;

	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;

	@TableField(exist=false)
	private List<?> list;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	/**
	 * 设置：父菜单ID，一级菜单为0
	 * @param parentId 父菜单ID，一级菜单为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：父菜单ID，一级菜单为0
	 * @return Long
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * 设置：菜单名称
	 * @param name 菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：菜单名称
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：菜单URL
	 * @param url 菜单URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取：菜单URL
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 设置：菜单图标
	 * @param icon 菜单图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取：菜单图标
	 * @return String
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置：排序
	 * @param orderNum 排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：排序
	 * @return Integer
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
