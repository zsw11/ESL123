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
	private String a0;
	/**
	 * B0
	 */
	private String b0;
	/**
	 * G0
	 */
	private String g0;
	/**
	 * A1
	 */
	private String a1;
	/**
	 * B1
	 */
	private String b1;
	/**
	 * P0
	 */
	private String p0;
	/**
	 * M0
	 */
	private String m0;
	/**
	 * X0
	 */
	private String x0;
	/**
	 * I0
	 */
	private String i0;
	/**
	 * A2
	 */
	private String a2;
	/**
	 * B2
	 */
	private String b2;
	/**
	 * P1
	 */
	private String p1;
	/**
	 * A3
	 */
	private String a3;
	/**
	 * 组织机构ID
	 */
	private Integer deptid;
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
