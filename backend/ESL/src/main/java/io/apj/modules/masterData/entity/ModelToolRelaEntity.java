package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 机种治工具关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Data
@TableName("model_tool_rela")
public class ModelToolRelaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 机种ID
	 */
	private Integer modelId;
	/**
	 * 机种
	 */
	@TableField(exist = false)
	private ModelEntity modelEntity;
	/**
	 * 治工具
	 */
	@TableField(exist = false)
	private ToolEntity toolEntity;
	/**
	 * 机种系列对象
	 */
	@TableField(exist = false)
	private ModelSeriesEntity modelSeriesEntity;
	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String deptName;

	/**
	 * 治工具ID
	 */
	private Integer toolId;
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
