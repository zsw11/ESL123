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
 * @date 2019-11-26 13:23:56
 */
@Data
@TableName("report_approve")
public class ApproveEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.comments
     */
    @TableId
    private Integer id;
    /**
     * 所属部门
     */
    private Integer deptId;
    private Integer phaseId;
    @TableField(exist = false)
    private String phaseName;
	/**
	 * 机种
	 */
	@TableField(exist = false)
    private String modelName;
    private Integer modelId;
    /**
     * 所属部门名称
     */
    @TableField(exist = false)
    private String deptName;
    /**
     * 报表组ID
     */
    private Integer reportGroupId;
    /**
     * 报表组名称
     */
    @TableField(exist = false)
    private String reportGroupName;
    /**
     * 下一审批者ID
     */
    private Integer nextApproverId;
    /**
     * 状态
     */
    private String status;
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
    private String stlst;

}
