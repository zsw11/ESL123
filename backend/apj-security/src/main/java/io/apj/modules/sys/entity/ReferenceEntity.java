package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 引用表
 * 
 * @author henry
 * 
 * @date 2019-01-14 14:55:47
 */
@TableName("sys_reference")
public class ReferenceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 主实体
	 */
	private String mainEntity;
	/**
	 * 主Id
	 */
	private Long mainId;
	/**
	 * 引用实体
	 */
	private String byEntity;
	/**
	 * 引用Id
	 */
	private Long byId;

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
	 * 设置：主实体
	 */
	public void setMainEntity(String mainEntity) {
		this.mainEntity = mainEntity;
	}
	/**
	 * 获取：主实体
	 */
	public String getMainEntity() {
		return mainEntity;
	}
	/**
	 * 设置：主Id
	 */
	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}
	/**
	 * 获取：主Id
	 */
	public Long getMainId() {
		return mainId;
	}
	/**
	 * 设置：引用实体
	 */
	public void setByEntity(String byEntity) {
		this.byEntity = byEntity;
	}
	/**
	 * 获取：引用实体
	 */
	public String getByEntity() {
		return byEntity;
	}
	/**
	 * 设置：引用Id
	 */
	public void setById(Long byId) {
		this.byId = byId;
	}
	/**
	 * 获取：引用Id
	 */
	public Long getById() {
		return byId;
	}

}
