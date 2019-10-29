package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 字典类型
 *
 * @author samchen
 *
 * @date 2018-12-05 14:16:20
 */
@TableName("sys_dict_type")
public class SysDictTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 字典类型编码
	 */
	private String type;
	/**
	 * 字典类型名称
	 */
	private String name;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 是否锁定
	 */
	private Boolean ifLock;
	/**
	 * 字典项列表
	 */
	@TableField(exist = false)
	private List<SysDictEntity> dictList;
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
	 * 设置：字典类型编码
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取：字典类型编码
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置：字典类型名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：字典类型名称
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
	 * 获取：锁定
	 */
	public Boolean getIfLock() {
		return ifLock;
	}

	/**
	 * 设置：锁定
	 */
	public void setIfLock(Boolean ifLock) {
		this.ifLock = ifLock;
	}

	/**
	 * 获取：字典项列表
	 */
	public List<SysDictEntity> getDictList() {
		return dictList;
	}

	/**
	 * 设置：字典项列表
	 */
	public void setDictList(List<SysDictEntity> dictList) {
		this.dictList = dictList;
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
