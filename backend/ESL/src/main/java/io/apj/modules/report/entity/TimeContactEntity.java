package io.apj.modules.report.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Report - 时间联络表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Data
@TableName("report_time_contact")
public class TimeContactEntity implements Serializable {
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
	 * 确认ID
	 */
	private Integer comfirmBy;
	/**
	 * 承认ID
	 */
	private Integer inChargeBy;
	/**
	 * 机种ID
	 */
	private Integer modelId;
	/**
	 * ES/AMP/MP
	 */
	private String stage;
	/**
	 * 发行类别：新规制定/修订
	 */
	private String publishType;
	/**
	 * 修订理由
	 */
	private String reviseReason;
	/**
	 * ST/LST
	 */
	private String stType;
	/**
	 * 版本号
	 */
	private String revNo;
	/**
	 * 全数sub工序用时
	 */
	private BigDecimal allCountSub;
	/**
	 * 全数main工序用时
	 */
	private BigDecimal allCountMain;
	/**
	 * 全数印字/检查/调整工序用时
	 */
	private BigDecimal allCountPrinting;
	/**
	 * 全数外装工序用时
	 */
	private BigDecimal allCountExternalInspection;
	/**
	 * 全数捆包工序用时
	 */
	private BigDecimal allCountPacking;
	/**
	 * 拖机上一版本sub工序用时
	 */
	private BigDecimal towingLastVersionSub;
	/**
	 * 拖机上一版本main工序用时
	 */
	private BigDecimal towingLastVersionMain;
	/**
	 * 拖机上一版本印字/检查/调整工序用时
	 */
	private BigDecimal towingLastVersionPrinting;
	/**
	 * 拖机上一版本外装工序用时
	 */
	private BigDecimal towingLastVersionExternalInspection;
	/**
	 * 拖机上一版本捆包工序用时
	 */
	private BigDecimal towingLastVersionPacking;
	/**
	 * $column.comments
	 */
	private String operationStandardNo;
	/**
	 * $column.comments
	 */
	private String operationInstruction;
	/**
	 * $column.comments
	 */
	private String exceptionOperation;
	/**
	 * 版本差异备注
	 */
	private String remarkVersionCopmare;
	/**
	 * sub差异备注
	 */
	private String remarkSub;
	/**
	 * main差异备注
	 */
	private String remarkMain;
	/**
	 * 印字/检查/调整差异备注
	 */
	private String remarkPrinting;
	/**
	 * 外装差异备注
	 */
	private String remarkExternalInspection;
	/**
	 * 捆包差异备注
	 */
	private String remarkPacking;
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
