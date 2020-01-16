package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Collection - 工位时间表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Data
@TableName("collection_station_time")
public class StationTimeEntity implements Serializable {
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

	@TableField(exist = false)
	private String phaseName;
	@TableField(exist = false)
	private String modelName;
	@TableField(exist = false)
	private boolean Exist;
	/**
	 * 标题
	 */
	private String title;
	private String stlst;
	/**
	 * Sheet名称
	 */
	private String sheetName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 机种ID
	 */
	private Integer modelId;
	/**
	 * 生产阶段ID
	 */
	private Integer phaseId;
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
