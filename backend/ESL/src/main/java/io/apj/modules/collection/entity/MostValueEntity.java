package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Collection - MOST Value 表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Data
@TableName("collection_most_value")
public class MostValueEntity implements Serializable {
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
	private Integer modelId;
	@TableField(exist = false)
	private String modelName;
	@TableField(exist = false)
	private String  phaseName;
	private Integer phaseId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 组立职场名称
	 */
	private String firstColumnName;
	/**
	 * Sheet名称
	 */
	private String sheetName;
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
	 * 备注
	 */
	private String remark;
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

	@TableField(exist = false)
	private boolean Exist;

}
