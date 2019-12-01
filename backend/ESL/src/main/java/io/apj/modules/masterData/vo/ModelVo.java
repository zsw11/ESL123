package io.apj.modules.masterData.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机种VO
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Data
public class ModelVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	private Integer id;

	/**
	 * 机种系列ID
	 */
	private Integer modelSeriesId;

	/**
	 * 机种系列
	 */

	private String modelSeriesName;
	/**
	 * 部门名称
	 */

	private String deptName;
	/**
	 * 名称
	 */
	private String modelName;
	/**
	 * 拼音
	 */
	private String pinyin;
	/**
	 * 部门ID
	 */
	private Integer deptId;
	/**
	 * 型号
	 */
	private String code;
	/**
	 * WS时间
	 */
	private Date wsTime;
	/**
	 * ES时间
	 */
	private Date esTime;
	/**
	 * AMP时间
	 */
	private Date ampTime;
	/**
	 * MP时间
	 */
	private Date mpTime;
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
