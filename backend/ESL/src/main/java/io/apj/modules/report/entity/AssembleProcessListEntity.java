package io.apj.modules.report.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * Report - Process List Table
 * 
 * @author inka cui
 * @email inkaCui@apjcorp.com
 * @date 2020-01-21 09:30
 */
@Data
@TableName("assemble_process_list")
public class AssembleProcessListEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	
	/**
	 * ST/LST
	 */
	private String stlst;
	
	/**
	 * 机种ID
	 */
	private Integer modelId;
	/**
	 * 仕向
	 */
	private String destinations;
	/**
	 * 组织机构ID
	 */
	private Integer phaseId;
	/**
	 * 版本号
	 */
	private String versionNumber;
	
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
