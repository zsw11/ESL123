package io.apj.modules.workBook.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 分析表明细
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:02
 */
@Data
@TableName("work_operations")
public class WorkOperationsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 序号
	 */
	private Integer seqNumber;
	/**
	 * 分析表ID
	 */
	private Integer workBookId;
	/**
	 * 版本信息
	 */
	private String version;
	/**
	 * 手顺
	 */
	private String operation;
	/**
	 * 频度
	 */
	private Integer frequency;
	/**
	 * 时间值
	 */
	private BigDecimal timeValue;
	/**
	 * TMU
	 */
	private BigDecimal tmu;
	/**
	 * 秒换算
	 */
	private BigDecimal secondConvert;
	/**
	 * 备注
	 */
	private String remark;
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
	/**
	 * 修订类型
	 */
	private boolean alterType;
	private String alterInfo;
	/**
	 * 类型
	 */
	private String type;
	private String tool;
	private Integer a0;
	private Integer b0;
	private Integer g0;
	private Integer a1;
	private Integer b1;
	private Integer p0;
	private Integer m0;
	private Integer x0;
	private Integer i0;
	private Integer a2;
	private Integer b2;
	private Integer p1;
	private Integer a3;
	private Integer a4;
	private Integer b3;
	private Integer p2;
	private Integer a5;
}
