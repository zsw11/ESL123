package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Report - Total表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@Data
@TableName("ashcraft_table")
public class AshcraftTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;

	/**
	 * N(2-6)
	 */
	private String n;

	private BigDecimal p;

	private BigDecimal ou;

	private BigDecimal mu;

	private BigDecimal sa;
}
