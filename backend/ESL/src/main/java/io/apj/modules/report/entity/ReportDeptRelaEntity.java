package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 报表部门关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-26 17:32:34
 */
@Data
@TableName("report_dept_rela")
public class ReportDeptRelaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 报表ID
	 */
	private Integer reportId;
	/**
	 * 部门ID
	 */
	private Integer deptId;
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
