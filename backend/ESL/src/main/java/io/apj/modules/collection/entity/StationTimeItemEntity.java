package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Collection - 工位时间表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Data
@TableName("collection_station_time_item")
public class StationTimeItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 工作时间表ID
	 */
	private Integer collectionStationTimeId;
	/**
	 * 是否是sub
	 */
	private Boolean sub;
	/**
	 * 工位名称
	 */
	private String stationName;
	/**
	 * 作业名
	 */
	private String workName;
	/**
	 * LST时间（秒）
	 */
	private BigDecimal lstValue;
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
