package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 常用指标组合
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@Data
@TableName("measure_group")
public class MeasureGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 类型
	 */
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
	/**
	 * 组织机构ID
	 */
	private Integer deptId;
	/**
	 * 机构名称
	 */
	@TableField(exist = false)
	private String  deptName;
	/**
	 * 使用次数统计
	 */
	private Integer usedCount;
	/**
	 * 频率
	 */
	private Float frequency;
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
	@TableField(exist = false)
	private String createName;
	@TableField(exist = false)
	private String updateName;

}
