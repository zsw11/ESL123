package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 标准时间表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Data
@TableName("report_standard_time_item")
public class StandardTimeItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 标准时间表ID
	 */
	private Integer reportStandardTimeId;
	/**
	 * 工程NO
	 */
	private Integer processNo;
	/**
	 * 工程名
	 */
	private String processName;
	/**
	 * most-HT值
	 */
	private BigDecimal mostHt;
	/**
	 * most-MT值
	 */
	private BigDecimal mostMt;
	/**
	 * most-MH值
	 */
	private BigDecimal mostMh;
	/**
	 * 时间total值
	 */
	private BigDecimal timeTotal;
	/**
	 * 时间sample1值
	 */
	private BigDecimal timeSample1;
	/**
	 * 时间sampleSize值
	 */
	private BigDecimal timeSampleSize;
	/**
	 * 创建者ID
	 */
	private Integer createBy;
	/**
	 * 创建时间
	 */
	private Date createAt;
	/**
	 * 更新者ID
	 */
	private Integer updateBy;
	/**
	 * 更新时间
	 */
	private Date updateAt;
	/**
	 * 删除时间
	 */
	private Date deleteAt;

	private Integer workBookId;

	@TableField(exist = false)
	private BigDecimal conv;
}
