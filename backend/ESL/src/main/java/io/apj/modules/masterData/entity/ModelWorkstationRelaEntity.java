package io.apj.modules.masterData.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 机种工位关系表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-16 17:37:03
 */
@Data
@TableName("model_workstation_rela")
public class ModelWorkstationRelaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * 机种ID
	 */
	private Integer modelId;
	/**
	 * 工位ID
	 */
	private Integer workstationId;
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
