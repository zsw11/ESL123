package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 编码规则子项
 *
 * @author Sam
 *
 * @date 2018-12-18 11:34:01
 */
@TableName("sys_code_rule_item")
public class CodeRuleItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 编码规则ID
	 */
	private Long codeRuleId;
	/**
	 * 序号
	 */
	private Integer orderNumber;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 是否序号键
	 */
	private Boolean ifSerialKey;
	/**
	 * 位数
	 */
	private Integer numberBit;
	/**
	 * 起始编号
	 */
	private Integer beginNumber;

	/**
	 * 固定值
	 */
	private String fixedValue;

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
	 * 设置：编码规则ID
	 */
	public void setCodeRuleId(Long codeRuleId) {
		this.codeRuleId = codeRuleId;
	}
	/**
	 * 获取：编码规则ID
	 */
	public Long getCodeRuleId() {
		return codeRuleId;
	}
	/**
	 * 设置：序号
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * 获取：序号
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}
	/**
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：是否序号键
	 */
	public void setIfSerialKey(Boolean ifSerialKey) {
		this.ifSerialKey = ifSerialKey;
	}
	/**
	 * 获取：是否序号键
	 */
	public Boolean getIfSerialKey() {
		return ifSerialKey;
	}
	/**
	 * 设置：位数
	 */
	public void setNumberBit(Integer numberBit) {
		this.numberBit = numberBit;
	}
	/**
	 * 获取：位数
	 */
	public Integer getNumberBit() {
		return numberBit;
	}
	/**
	 * 设置：起始编号
	 */
	public void setBeginNumber(Integer beginNumber) {
		this.beginNumber = beginNumber;
	}
	/**
	 * 获取：起始编号
	 */
	public Integer getBeginNumber() {
		return beginNumber;
	}

	public String getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
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
