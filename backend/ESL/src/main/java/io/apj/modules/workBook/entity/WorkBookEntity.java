package io.apj.modules.workBook.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 分析表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:03
 */
@Data
@TableName("work_book")
public class WorkBookEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 组织机构ID
	 */
	private Integer deptId;
	/*
	 * 部门名称
	 */
	@TableField(exist = false)
	private String DeptName;
	/*
	 * model名称
	 */
	@TableField(exist = false)
	private String ModelName;
	@TableField(exist = false)
	private ModelEntity modelEntity;
	/*
	 * Phase名称
	 */
	@TableField(exist = false)
	private String PhaseName;
	/**
	 * 手顺列表
	 */
	@TableField(exist = false)
	private List<WorkOperationsEntity> workOperationsList;
	/**
	 * 工位名称
	 */
	@TableField(exist = false)
	private String WorkstationName;
	/**
	 * 其他工位列表
	 */
	@TableField(exist = false)
	private List<WorkBookEntity> otherWorkBookEnties;
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
	 * 生产阶段ID
	 */
	private Integer phaseId;
	/**
	 * 工位ID
	 */
	private Integer workstationId;
	/**
	 * 作业名
	 */
	private String workName;
	/**
	 * 版本号
	 */
	private String versionNumber;
	/**
	 * 制表人ID
	 */
	private Integer makerId;
	/**
	 * 制表日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date makedAt;
	/**
	 * 沿用来源ID
	 */
	private Integer continueFromId;
	/**
	 * 时间值
	 */
	private BigDecimal timeValue;
	/**
	 * TMU
	 */
	private BigDecimal tmu;
	/**
	 * 秒换算
	 */
	private BigDecimal secondConvert;
	/**
	 * 备注
	 */
	private String remarks;
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
	/**
	 * 是否修订
	 */
	private boolean ifAlter;


}
