package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 报表组部门关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2020-01-15 18:11:00
 */
@Data
@TableName("node_model_workstation_rela")
public class NodeModelWorkstationRelaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 工位结构节点ID
	 */
	private Integer workstationTypeNodeId;
	/**
	 * 机种ID
	 */
	private Integer modelId;
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
	 * 工位IDs
	 */
	private String workstationIds;
	/**
	 * 工位名称集合
	 */
	@TableField(exist=false)
	private String workstationName;
	/**
	 * 工位id集合
	 */
	@TableField(exist=false)
	private List<Integer> workstationIdList;
	/**
	 * 工位实体集合
	 */
	@TableField(exist=false)
	private List<WorkstationEntity> workstationList;
	/**
	 * 机种名称
	 */
	@TableField(exist=false)
	private String modelName;
	/**
	 * 机种名称
	 */
	@TableField(exist=false)
	private ModelEntity modelEntity;
}
