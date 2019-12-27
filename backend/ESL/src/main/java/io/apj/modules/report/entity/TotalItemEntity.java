package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Report - Total表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@Data
@TableName("report_total_item")
public class TotalItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * Total表ID
	 */
	private Integer reportTotalId;
	/**
	 * 工位名称
	 */
	private String workName;
	/**
	 * 进程
	 */
	private String process;
	/**
	 * 部件版本号
	 */
	private String assemblyLayoutRev;
	/**
	 * 组装工厂
	 */
	private String assemblyWorkExecutionFirm;
	/**
	 * ST人工耗时
	 */
	private BigDecimal mostStHt;
	/**
	 * ST机器耗时
	 */
	private BigDecimal mostStMt;
	/**
	 * LST人工耗时
	 */
	private BigDecimal mostLstHt;
	/**
	 * LST机器耗时
	 */
	private BigDecimal mostLstMt;
	/**
	 * $column.comments
	 */
	private BigDecimal stComplement;
	/**
	 * $column.comments
	 */
	private BigDecimal stSampling;
	/**
	 * $column.comments
	 */
	private BigDecimal stSamplingSize;
	/**
	 * $column.comments
	 */
	private BigDecimal stSec;
	/**
	 * $column.comments
	 */
	private BigDecimal lstComplement;
	/**
	 * $column.comments
	 */
	private BigDecimal lstSampling;
	/**
	 * $column.comments
	 */
	private BigDecimal lstSamplingSize;
	/**
	 * $column.comments
	 */
	private BigDecimal lstMostValueUse;
	/**
	 * $column.comments
	 */
	private BigDecimal lstSec;
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
}
