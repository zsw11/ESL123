package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 手顺
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@Data
@TableName("operation_group_operation")
public class OperationGroupOperationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 手顺组合ID
	 */
	private Integer operationGroupId;
	/**
	 * 序号
	 */
	private Integer seqNumber;
	/**
	 * 手顺
	 */
	private String operation;
	/**
	 * 指标
	 */
	private String measures;
	/**
	 * 频度
	 */
	private Integer frequency;
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