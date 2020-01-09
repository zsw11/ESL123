package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 履历表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Data
@TableName("report_change_record_item")
public class ChangeRecordItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 履历表ID
	 */
	private Integer reportChangeRecordId;
	/**
	 * 制定日
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date makedAt;
	/**
	 * 版本号
	 */
	private String revNo;
	/**
	 * 工程名
	 */
	private String processName;
	/**
	 * 修改内容
	 */
	private String content;
	/**
	 * 当前耗时值
	 */
	private BigDecimal currentValue;
	/**
	 * 变更前耗时值
	 */
	private BigDecimal lastValue;

	@TableField(exist = false)
	private BigDecimal subValue;
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
