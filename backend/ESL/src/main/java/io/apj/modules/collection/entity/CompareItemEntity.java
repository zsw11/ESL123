package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Collection - Compare表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Data
@TableName("collection_compare_item")
public class CompareItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * compare表ID
	 */
	private Integer collectionCompareId;
	/**
	 * 是否是Sub
	 */
	private Boolean sub;
	/**
	 * 工位名
	 */
	private String workName;
	/**
	 * 组合名称
	 */
	private String unitName;
	/**
	 * 上一版本耗时
	 */
	private BigDecimal lastValue;
	/**
	 * 当前版本耗时
	 */
	private BigDecimal currentValue;
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
