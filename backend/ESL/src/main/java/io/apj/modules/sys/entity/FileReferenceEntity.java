package io.apj.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件引用关系表
 *
 * @author samchen
 *
 * @date 2019-02-22 16:10:49
 */
@TableName("sys_file_reference")
public class FileReferenceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 原文件名
	 */
	private String name;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 来源类型
	 */
	private String sourceType;
	/**
	 * 来源ID
	 */
	private Long sourceId;
	/**
	 * 是否引用
	 */
	private Boolean ifReference;
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
	 * 设置：原文件名
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置：原文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取：文件名
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置：文件路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 获取：文件路径
	 */
	public String getFilePath() {
		return filePath;
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
	 * 设置：是否引用
	 */
	public void setIfReference(Boolean ifReference) {
		this.ifReference = ifReference;
	}

	/**
	 * 获取：是否引用
	 */
	public Boolean getIfReference() {
		return ifReference;
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
