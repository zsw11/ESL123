package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import org.jboss.logging.Field;

import lombok.Data;

/**
 * 工位
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Data
@TableName("workstation")
public class WorkstationEntity implements Serializable {
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
	 * 工位類型
	 */
	private int workstationTypeId;
	/**
	 * 机种工位关系表ID
	 */
	@TableField(exist = false)
	private int modelWorkStationRelaId;
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
	@TableField(exist = false)
	private String createName;
	@TableField(exist = false)
	private String updateName;
}
