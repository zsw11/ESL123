package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 工位类型节点
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Data
@TableName("workstation_type_node")
public class WorkstationTypeNodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 拼音
	 */
	private String pinyin;
	/**
	 * 工位类型ID
	 */
	private Integer workstationTypeId;
	/**
	 * 备注
	 */
	private String remark;

	/*
	 * 父id
	 */
	private Integer parentId;
	/**
	 * 是否是工位
	 */
	private Boolean ifWorkstation;
	/**
	 * 是否展开
	 */
	private Boolean ifOpen;
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
	private List<WorkstationTypeNodeEntity> child;

	@TableField(exist = false)
	private NodeModelWorkstationRelaEntity modelWorkstation;

}
