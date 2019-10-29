package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.apj.common.validator.group.AddGroup;
import io.apj.common.validator.group.UpdateGroup;

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
	private Long toMemberId;
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
	 * 流程图实例ID
	 */
	private Long processId;
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
	 * 设置：所属组织集团
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取：所属组织集团
	 */
	public Long getDeptId() {
		return deptId;
	}

	/**
	 * 设置：消息类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取：消息类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置：来源类型
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * 获取：来源类型
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * 设置：来源ID
	 */
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * 获取：来源ID
	 */
	public Long getSourceId() {
		return sourceId;
	}

	/**
	 * 设置：指定用户ID
	 */
	public void setToMemberId(Long toMemberId) {
		this.toMemberId = toMemberId;
	}

	/**
	 * 获取：指定用户ID
	 */
	public Long getToMemberId() {
		return toMemberId;
	}

	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置：状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取：状态
	 */
	public String getStatus() {
		return status;
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

	/**
	 * 获取：是否已读
	 */
	public boolean isIfRead() {
		return ifRead;
	}

	/**
	 * 设置：是否已读
	 */
	public void setIfRead(boolean ifRead) {
		this.ifRead = ifRead;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}
