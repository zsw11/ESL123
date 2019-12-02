package io.apj.modules.masterData.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部品
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Data
public class PartVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
//	private Integer id;

	private Integer pmId;
	@TableId
	private Integer partId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 拼音
	 */
	private String pinyin;
	/**
	 * 是否通用
	 */
	private Boolean common;
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
}
