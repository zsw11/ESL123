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
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import io.apj.common.validator.group.AddGroup;
import io.apj.common.validator.group.UpdateGroup;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 部门管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 部门ID
	@TableId
	private Long id;
	// 上级部门ID，一级部门为0
	private Long parentId;
	// 部门名称
	private String name;
	// 上级部门名称
	@TableField(exist = false)
	private String parentName;
	// 排序
	private Integer orderNum;

	@TableLogic
	private Integer delFlag;
	private Long createBy;
	private Date createAt;
	private Long updateBy;
	private Date updateAt;
	private Date deleteAt;
	private String deptCode;
	/**
	 * 类别:总部：headquarters 分公司：branch
	 */
//	@NotNull(message = "类型不能为空！", groups = { AddGroup.class, UpdateGroup.class })
	private String deptType;
	/**
	 * 等级：集团bloc，公司：company，部门：dept
	 */
//	@NotNull(message = "等级不能为空！", groups = { AddGroup.class, UpdateGroup.class })
	private String deptLevel;
	/**
	 * ztree属性
	 */
	@TableField(exist = false)
	private Boolean open;

	@TableField(exist = false)
	private List<?> list;

	/**
	 * 是否拥有数据权限
	 */
	@TableField(exist = false)
	private Boolean permission;
	/**
	 * 部门权限列表名称
	 */
	@TableField(exist = false)
	private String[] perms;

	public String[] getPerms() {
		return perms;
	}

	public void setPerms(String[] perms) {
		this.perms = perms;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	/**
	 * 设置：上级部门ID，一级部门为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：上级部门ID，一级部门为0
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * 设置：部门名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：部门名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
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

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Boolean getPermission() {
		return permission;
	}

	public void setPermission(Boolean permission) {
		this.permission = permission;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getDeptLevel() {
		return deptLevel;
	}

	public void setDeptLevel(String deptLevel) {
		this.deptLevel = deptLevel;
	}

}
