package io.apj.modules.file.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
 * @author HarryHua
 * @date 2019年5月1日 上午10:24:38 
 * @Description:  文件管理实体类
 * @throws
 */
@TableName("sys_file")
public class SysFileEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 文件名称
	 */
	@NotEmpty(message = "文件名称不能为空")
	private String name;
	/**
	 * 路径名称
	 */
	@NotEmpty(message = "路径名称不能为空")
	private String filePath;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 创建者ID
	 */
	@NotEmpty(message = "创建者ID不能为空")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@NotEmpty(message = "创建时间不能为空")
	private Date createAt;
	/**
	 * 更新者ID
	 */
	private Long updateBy;
	/**
	 * 更新时间
	 */
	private Date updateAt;
	/**
	 * 删除时间
	 */
	private Date deleteAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	public Date getDeleteAt() {
		return deleteAt;
	}
	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

}
