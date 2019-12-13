package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 标准工数表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Data
@TableName("report_standard_work")
public class StandardWorkEntity implements Serializable {
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
	 * 机种
	 */
	@TableField(exist = false)
	private String modelName;
	/**
	 * 型号
	 */
	private String modelType;

	private String stlst;
	/**
	 * 系数
	 */
	private BigDecimal coefficient;
	/**
	 * 生产阶段ID
	 */
	private Integer phaseId;
	/**
	 * 生产阶段
	 */
	@TableField(exist = false)
	private String phaseName;
	/**
	 * 技通No
	 */
	private String revNo;
	/**
	 * 发行日
	 */
	private Date monthResult;
	/**
	 * 首个标准工数title
	 */
	private String firstStandardWorkTitle;
	/**
	 * 第二个标准工数title
	 */
	private String secondStandardWorkTitle;
	/**
	 * 第三个标准工数title
	 */
	private String thirdStandardWorkTitle;
	/**
	 * 确认ID
	 */
	private Integer comfirmBy;
	/**
	 * 承认ID
	 */
	private Integer inChargeBy;
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
