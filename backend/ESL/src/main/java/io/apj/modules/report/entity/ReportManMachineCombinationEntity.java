package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${comments}
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
@Data
@TableName("report_man_machine_combination")
public class ReportManMachineCombinationEntity implements Serializable {
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
	/**
	 * $column.comments
	 */
	private Integer phaseId;
	/**
	 * $column.comments
	 */
	private String stlst;
	/**
	 * 发行日
	 */
	private Date monthResult;
	/**
	 * 仕向
	 */
	private String destinations;
	/**
	 * MT 分析表totalRemark
	 */
	private BigDecimal mt;
	/**
	 * 输入数值
	 */
	private BigDecimal enter;
	/**
	 * 选择（N2-N6）
	 */
	private String selectnum;
	/**
	 * $column.comments
	 */
	private Integer comfirmBy;
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
