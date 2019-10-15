package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典项
 *
 * @author samchen
 *
 * @date 2018-12-05 14:16:20
 */
@TableName("sys_dict")
public class SysDictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 字典项编码
	 */
	private String code;
	/**
	 * 字典项名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 是否锁定
	 */
	private boolean ifLock;
	/**
	 * 关联字典类型ID
	 */
	private Long dictTypeId;
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
	 * 设置：字典项编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取：字典项编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置：字典项名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：字典项名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：描述
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置：排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * 获取：排序
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 获取：是否锁定
	 */
	public boolean isIfLock() {
		return ifLock;
	}

	/**
	 * 设置：是否锁定
	 */
	public void setIfLock(boolean ifLock) {
		this.ifLock = ifLock;
	}

	/**
	 * 设置：关联字典类型ID
	 */
	public void setDictTypeId(Long dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	/**
	 * 获取：关联字典类型ID
	 */
	public Long getDictTypeId() {
		return dictTypeId;
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
