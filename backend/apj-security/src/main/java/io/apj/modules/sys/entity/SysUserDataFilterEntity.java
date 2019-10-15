package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户Token
 */
@TableName("sys_user_data_filter")
public class SysUserDataFilterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 用户ID
	@TableId(type = IdType.INPUT)
	private Long userId;
	// 过滤条件
	private String filter;
	// 创建时间
	private Date createAt;
	// 更新时间
	private Date updateAt;

	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 获取：过滤条件
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * 设置：过滤条件
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateAt() {
		return createAt;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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
}
