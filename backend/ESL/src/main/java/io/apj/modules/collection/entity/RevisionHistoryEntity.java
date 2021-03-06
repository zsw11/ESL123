package io.apj.modules.collection.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * Collection - Revision History 表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Data
@TableName("collection_revision_history")
public class RevisionHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 组织机构ID
	 */
	private Integer deptId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * Sheet名称
	 */
	private String sheetName;
	/**
	 * 机种ID
	 */
	private Integer modelId;
	@TableField(exist = false)
	private String modelName;
	@TableField(exist = false)
	private String phaseName;
	private Integer phaseId;
	private String stlst;
	/**
	 * 仕向
	 */
	private String destinations;
	/**
	 * 版本号
	 */
	private String versionNumber;
	/**
	 * 工位结构
	 */
	private String workstationType;
	/**
	 * 确认ID
	 */
	private Integer comfirmBy;
	/**
	 * 承认ID
	 */
	private Integer inChargeBy;
	/**
	 * 制造工厂
	 */
	private String factory;
	/**
	 * 发行日
	 */
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date monthResult;
	/**
	 * 版本号
	 */
	private String revNo;
	/**
	 * 上一版本ST名称
	 */
	private String lastStName;
	/**
	 * 当前版本ST名称
	 */
	private String currentStName;
	/**
	 * 上一版本LST名称
	 */
	private String lastLstName;
	/**
	 * 当前版本LST名称
	 */
	private String currentLstName;
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
	private List<RevisionHistoryItemEntity> items;

	@TableField(exist = false)
	private boolean Exist;

}
