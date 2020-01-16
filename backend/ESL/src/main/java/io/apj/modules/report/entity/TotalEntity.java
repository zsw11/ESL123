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
 * Report - Total表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@Data
@TableName("report_total")
public class TotalEntity implements Serializable {
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
	/**
	 * 标题
	 */
	private String title;
	/**
	 * Sheet名称
	 */
	private String sheetName;
	/**
	 * 机种ID
	 */
	private Integer modelId;
	private Integer phaseId;
	private String stlst;

	@TableField(exist = false)
	private String phaseName;

	@TableField(exist = false)
	private boolean Exist;
	/**
	 * 机种
	 */
	@TableField(exist = false)
	private String modelName;
	/**
	 * 发行日
	 */
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date monthResult;
	/**
	 * 仕向
	 */
	private String destinations;
	/**
	 * 版本号
	 */
	private String versionNumber;
	/**
	 * 工位结构
	 */
	private String workstationType;
	/**
	 * 类别
	 */
	private String cotegory;
	/**
	 * mecha
	 */
	private String mecha;
	/**
	 * R_code
	 */
	private String rCode;
	/**
	 * ST版本号
	 */
	private String stRev;
	/**
	 * LST版本号
	 */
	private String lstRev;
	/**
	 * 津贴
	 */
	private BigDecimal allowanceRate;
	/**
	 * 确认ID
	 */
	private Integer comfirmBy;
	/**
	 * 承认ID
	 */
	private Integer inChargeBy;
	/**
	 * 拖机或不拖机
	 */
	private String type;
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
