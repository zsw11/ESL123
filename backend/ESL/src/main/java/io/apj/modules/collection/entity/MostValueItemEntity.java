package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
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
@TableName("collection_most_value_item")
public class MostValueItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * Most Value表ID
	 */
	private Integer collectionMostValueId;
	/**
	 * 类型:Sub/Man/测试、检查/捆包
	 */
	private String type;
	/**
	 * 作业名
	 */
	private String workName;
	/**
	 * 耗时（Min）
	 */
	private BigDecimal total;
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
