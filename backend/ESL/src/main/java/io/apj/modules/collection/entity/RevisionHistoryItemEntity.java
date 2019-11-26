package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Collection - Revision History 表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Data
@TableName("collection_revision_history_item")
public class RevisionHistoryItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * Revision History 表ID
	 */
	private Integer collectionRevisionHistoryId;
	/**
	 * 工程名
	 */
	private String name;
	/**
	 * 改訂履歴
	 */
	private String remark;
	/**
	 * 上一ST版本耗时
	 */
	private BigDecimal lastStTime;
	/**
	 * 当前版本ST耗时
	 */
	private BigDecimal currentStTime;
	/**
	 * ST版本耗时差异值
	 */
	private BigDecimal stInterval;
	/**
	 * 上一LST版本耗时
	 */
	private BigDecimal lastLstTime;
	/**
	 * 当前版本LST耗时
	 */
	private BigDecimal currentLstTime;
	/**
	 * LST版本耗时差异值
	 */
	private BigDecimal lstInterval;
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
