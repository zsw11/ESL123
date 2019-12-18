package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 报表审批表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Data
@TableName("report_approve_history")
public class ApproveHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * $column.comments
	 */
	private Integer deptId;
	/**
	 * 报表审批表ID
	 */
	private Integer reportApproveId;
	/**
	 * 审批结果
	 */
	private String result;
	/**
	 * 审批意见
	 */
	private String opinion;
	/**
	 * 报表组ID
	 */
	private Integer reportGroupId;
	/**
	 * 报表组
	 */
	@TableField(exist = false)
	private String reportGroupName;
	/**
	 * 下一审批者ID
	 */
	private Integer nextApproverId;
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

	private Integer modelId;
	private Integer phaseId;
	private String stlst;

	@TableField(exist = false)
	private String modelName;

	@TableField(exist = false)
	private String phaseName;

}
