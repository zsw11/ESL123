package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 手顺组合
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@Data
@TableName("opertaion_group")
public class OpertaionGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 组织机构ID
	 */
	private Integer deptId;
	@TableField(exist = false)
	private String deptName;
	/**
	 * 手顺数量
	 */
	@TableField(exist = false)
	private Integer count;
	/**
	 * 频率
	 */
	private Float frequency;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 使用次数统计
	 */
	private Integer usedCount;
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
