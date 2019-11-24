package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
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
	 * A0
	 */
	private Integer a0;
	/**
	 * B0
	 */
	private Integer b0;
	/**
	 * G0
	 */
	private Integer g0;
	/**
	 * A1
	 */
	private Integer a1;
	/**
	 * B1
	 */
	private Integer b1;
	/**
	 * P0
	 */
	private Integer p0;
	/**
	 * M0
	 */
	private Integer m0;
	/**
	 * X0
	 */
	private Integer x0;
	/**
	 * I0
	 */
	private Integer i0;
	/**
	 * A2
	 */
	private Integer a2;
	/**
	 * B2
	 */
	private Integer b2;
	/**
	 * P1
	 */
	private Integer p1;
	/**
	 * A3
	 */
	private Integer a3;
	/**
	 * 组织机构ID
	 */
	private Integer deptId;
	/**
	 * 使用次数统计
	 */
	private Integer usedCount;
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
