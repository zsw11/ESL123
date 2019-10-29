package io.apj.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;

import io.apj.modules.sys.dao.FileReferenceDao;
import io.apj.modules.sys.entity.FileReferenceEntity;
import io.apj.modules.sys.service.FileReferenceService;

@Service("fileReferenceService")
public class FileReferenceServiceImpl extends ServiceImpl<FileReferenceDao, FileReferenceEntity>
		implements FileReferenceService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<FileReferenceEntity> page = this.selectPage(new Query<FileReferenceEntity>(params).getPage(),
				new EntityWrapper<FileReferenceEntity>());

		return new PageUtils(page);
	}

	@Override
	public void save(FileReferenceEntity fileReference) {
		this.insert(fileReference);
	}

	/**
	 * 插入文件信息
	 *
	 * @param fileName
	 * @param filePath
	 * @param sourceType
	 * @param sourceId
	 */
	@Override
	public void insertFile(String fileName, String filePath, String sourceType, long sourceId) {

		Wrapper<FileReferenceEntity> wrapper = new EntityWrapper<FileReferenceEntity>().eq("file_name", fileName)
				.eq("source_type", sourceType).eq("source_id", sourceId);

		if (this.selectCount(wrapper) < 0) {
			FileReferenceEntity fileReference = new FileReferenceEntity();
			fileReference.setCreateAt(new Date());
			fileReference.setFileName(fileName);
			fileReference.setFilePath(filePath);
			fileReference.setIfReference(false);
			this.insert(fileReference);
		}

	}

	/**
	 * 设置为已引用
	 *
	 * @param fileName
	 */
	@Override
	public void reference(String fileName) {
		Wrapper<FileReferenceEntity> wrapper = new EntityWrapper<FileReferenceEntity>().eq("file_name", fileName);
		FileReferenceEntity entity = this.selectOne(wrapper);
		if (entity != null) {
			entity.setIfReference(true);
			this.updateById(entity);
		}
	}

	/**
	 * 删除
	 *
	 * @param fileName
	 */
	@Override
	public void delete(String fileName) {
		Wrapper<FileReferenceEntity> wrapper = new EntityWrapper<FileReferenceEntity>().eq("file_name", fileName);
		FileReferenceEntity entity = this.selectOne(wrapper);
		if (entity != null) {
			this.deleteById(entity.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(FileReferenceEntity fileReference) {
		FileReferenceEntity entity = this.selectById(fileReference.getId());
		fileReference.setCreateAt(entity.getCreateAt());
		this.updateAllColumnById(fileReference);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		this.deleteBatch(ids);
	}

}
