package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.FileReferenceEntity;

import java.util.Map;

/**
 * 文件引用关系表
 *
 * @author samchen
 * 
 * @date 2019-02-22 16:10:49
 */
public interface FileReferenceService extends IService<FileReferenceEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 保存配置信息
	 */
	public void save(FileReferenceEntity fileReference);

	/**
	 * 更新配置信息
	 */
	public void update(FileReferenceEntity fileReference);

	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);

	/**
	 * 设置为已引用
	 */
	public void reference(String fileName);

	/**
	 * 删除
	 */
	public void delete(String fileName);

	/**
	 * 插入文件信息
	 */
	public void insertFile(String fileName, String filePath, String sourceType, long sourceId);

}
