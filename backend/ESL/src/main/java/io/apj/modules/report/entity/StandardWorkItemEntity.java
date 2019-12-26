package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 标准工数表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Data
@TableName("report_standard_work_item")
public class StandardWorkItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 标准工数表ID
	 */
	private Integer reportStandardWorkId;
	/**
	 * 工程号
	 */
	private Integer processNo;
	/**
	 * 工程名
	 */
	private String processName;
	/**
	 * 第一个时间
	 */
	private BigDecimal firstTime;

	@TableField(exist = false)
	private BigDecimal hfTime;
	/**
	 * 第二个时间
	 */
	private BigDecimal secondTime;
	/**
	 * 第三个时间
	 */
	private BigDecimal thirdTime;
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



}
