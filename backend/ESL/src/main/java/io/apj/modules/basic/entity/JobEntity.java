package io.apj.modules.basic.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 岗位信息
 *
 * @author Royluo
 *
 * @date 2018-12-07 17:39:37
 */
@TableName("basic_job")
public class JobEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 岗位编码
	 */
	@NotBlank(message = "岗位编码不能为空")
	private String code;
	/**
	 * 岗位名称
	 */
	@NotBlank(message = "岗位名称不能为空")
	private String name;
	/**
	 * 岗位名称拼音
	 */
	@NotBlank(message = "岗位名称拼音不能为空")
	private String pinyin;
	/**
	 * 工程师等级
	 */
	@NotBlank(message = "工程师等级不能为空")
	private String grade;
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
	 * 设置：岗位编码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：岗位编码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：岗位名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：岗位名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：岗位名称拼音
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	/**
	 * 获取：岗位名称拼音
	 */
	public String getPinyin() {
		return pinyin;
	}
	/**
	 * 设置：工程师等级
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * 获取：工程师等级
	 */
	public String getGrade() {
		return grade;
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
}
