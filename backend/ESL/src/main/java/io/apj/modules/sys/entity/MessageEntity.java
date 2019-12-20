package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.apj.common.validator.group.AddGroup;
import io.apj.common.validator.group.UpdateGroup;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

/**
 * 消息通知
 *
 * @author samchen
 *
 * @date 2019-01-18 14:52:02
 */
@TableName("sys_message")
@Data
public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 所属组织集团ID
	 */
	@NotBlank(message = "所属组织集团ID不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Long deptId;
	/**
	 * 消息类型
	 */
	@NotBlank(message = "消息类型不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String type;
	/**
	 * 来源类型
	 */
	@NotBlank(message = "来源类型不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String sourceType;
	/**
	 * 来源ID
	 */
	@NotBlank(message = "来源ID不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Long sourceId;
	/**
	 * 指定用户ID
	 */
	private Long toStaffId;
	/**
	 * 标题
	 */
	@NotBlank(message = "标题不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String title;
	/**
	 * 内容
	 */
	@NotBlank(message = "内容不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String content;
	/**
	 * 状态
	 */
	@NotBlank(message = "状态不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String status;
	/**
	 * 是否已读
	 */
	@TableField(exist = false)
	private boolean ifRead;
	/**
	 * 创建时间
	 */
	@NotBlank(message = "创建时间不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Date createAt;

}
