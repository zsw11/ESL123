package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PhaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ${comments}
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
@Data
@TableName("report_Batch")
public class ReportBatchEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * $column.comments
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
	 * $column.comments
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
	@TableField(exist = false)
	private ModelEntity  modelEntity;
	@TableField(exist = false)
	private PhaseEntity phaseEntity;


}
