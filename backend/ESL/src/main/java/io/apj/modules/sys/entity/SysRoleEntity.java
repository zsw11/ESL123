package io.apj.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 角色
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:27:38
 */
@TableName("sys_role")
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableId
	private Long id;

	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String roleName;

	/**
	 * 部门ID
	 */
	// @NotNull(message="部门不能为空")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String deptName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建者ID
	 */
	private Long createBy;

	private Long updateBy;
	private Date updateAt;
	private Date deleteAt;

	@TableField(exist = false)
	private List<Long> menuIdList;
	@TableField(exist = false)
	private List<Long> deptIdList;
	@TableField(exist = false)
	private List<Map<String, Object>> menuDepts;

	public List<Map<String, Object>> getMenuDepts() {
		return menuDepts;
	}

	public void setMenuDepts(List<Map<String, Object>> menuDepts) {
		this.menuDepts = menuDepts;
	}

	/**
	 * 创建时间
	 */
	private Date createAt;
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

	/**
	 * 设置：
	 *
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：角色名称
	 *
	 * @param roleName 角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取：角色名称
	 *
	 * @return String
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 设置：备注
	 *
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 *
	 * @return String
	 */
	public String getRemark() {
		return remark;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<Long> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList) {
		this.menuIdList = menuIdList;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<Long> getDeptIdList() {
		return deptIdList;
	}

	public void setDeptIdList(List<Long> deptIdList) {
		this.deptIdList = deptIdList;
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

}
