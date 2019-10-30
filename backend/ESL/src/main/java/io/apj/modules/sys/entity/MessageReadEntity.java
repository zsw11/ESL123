package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.apj.common.validator.group.AddGroup;
import io.apj.common.validator.group.UpdateGroup;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

/**
 * 已读消息用户
 *
 * @author samchen
 *
 * @date 2019-01-18 14:52:01
 */
@TableName("sys_message_read")
public class MessageReadEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Long memberId;
	/**
	 * 消息通知ID
	 */
	@NotBlank(message = "消息通知ID不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private Long messageId;
	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：用户ID
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * 获取：用户ID
	 */
	public Long getMemberId() {
		return memberId;
	}

	/**
	 * 设置：消息通知ID
	 */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	/**
	 * 获取：消息通知ID
	 */
	public Long getMessageId() {
		return messageId;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateAt() {
		return createAt;
	}
}
