package io.apj.modules.basic.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.apj.common.validator.group.AddGroup;
import io.apj.common.validator.group.UpdateGroup;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.entity.SysUserEntity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 人员信息
 *
 * @author Royluo
 *
 * @date 2018-12-10 17:12:16
 */
@TableName("basic_member")
public class MemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 工作岗位
	 */
	@NotNull(message = "工作岗位不能为空")
	private Long jobId;

	@TableField(exist = false)
	private JobEntity job;
	/**
	 * 所属组织集团
	 */
	@NotEmpty(message = "部门不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Long deptId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 部门
	 */
	@TableField(exist = false)
	private SysDeptEntity dept;
	/**
	 * 人员编码
	 */
	@NotEmpty(message = "人员编码不能为空")
	private String code;
	/**
	 * 人员姓名
	 */
	@NotEmpty(message = "人员姓名不能为空")
	private String name;
	/**
	 * 姓名拼音
	 */
	@NotEmpty(message = "姓名拼音不能为空")
	private String pinyin;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 手机号码
	 */
	private String mobilephone;
	/**
	 * 在职状态
	 */
	@NotEmpty(message = "在职状态不能为空")
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 入职日期
	 */
	@NotNull(message = "入职日期不能为空")
	private Date employmentDate;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 创建者ID
	 */
	private Long createBy;
	/**
	 * 创建时间
	 */
	private Date createAt;
	/**
	 * 更新者ID
	 */
	private Long updateBy;
	/**
	 * 更新时间
	 */
	private Date updateAt;
	/**
	 * 删除时间
	 */
	private Date deleteAt;
	/**
	 * 用户实体
	 */
	@TableField(exist = false)
	private SysUserEntity userEntity;
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
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：工作岗位
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	/**
	 * 获取：工作岗位
	 */
	public Long getJobId() {
		return jobId;
	}

	/**
	 * 设置：所属组织集团
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取：所属组织集团
	 */
	public Long getDeptId() {
		return deptId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 设置：人员编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取：人员编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置：人员姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：人员姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：姓名拼音
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	/**
	 * 获取：姓名拼音
	 */
	public String getPinyin() {
		return pinyin;
	}

	/**
	 * 设置：性别
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 获取：性别
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 设置：手机号码
	 */
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	/**
	 * 获取：手机号码
	 */
	public String getMobilephone() {
		return mobilephone;
	}

	/**
	 * 设置：在职状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取：在职状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置：入职日期
	 */
	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}

	/**
	 * 获取：入职日期
	 */
	public Date getEmploymentDate() {
		return employmentDate;
	}

	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置：创建者ID
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * 获取：创建者ID
	 */
	public Long getCreateBy() {
		return createBy;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateAt() {
		return createAt;
	}

	/**
	 * 设置：更新者ID
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 获取：更新者ID
	 */
	public Long getUpdateBy() {
		return updateBy;
	}

	/**
	 * 设置：更新时间
	 */
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	/**
	 * 获取：更新时间
	 */
	public Date getUpdateAt() {
		return updateAt;
	}

	/**
	 * 设置：删除时间
	 */
	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	/**
	 * 获取：删除时间
	 */
	public Date getDeleteAt() {
		return deleteAt;
	}

	public JobEntity getJob() {
		return job;
	}

	public void setJob(JobEntity job) {
		this.job = job;
	}

	public SysDeptEntity getDept() {
		return dept;
	}

	public SysUserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(SysUserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public void setDept(SysDeptEntity dept) {
		this.dept = dept;
	}
}
