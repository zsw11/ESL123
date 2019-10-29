package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.ReferenceEntity;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.List;

/**
 * 引用表
 *
 * @author henry
 * 
 * @date 2019-01-14 14:55:47
 */
public interface ReferenceService extends IService<ReferenceEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
	 * 保存配置信息
	 */
    public void save(ReferenceEntity reference);
    
    /**
	 * 更新配置信息
	 */
	public void update(ReferenceEntity reference);
	
	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);
	
	/**
	 * 高级查询
	 */
	public List<ReferenceEntity> advancedSerach(Map<String, Object> params);

	/**
	 * 保存时调用
	 */
	public void insertTableReference(Object model);

	/**
	 * 引用表保存和更新
	 *
	 * @param mainEntity
	 * @param mainId
	 * @param byEntity
	 * @param byId
	 * @param ifUpdate
	 */
	public void insertTableReference(String mainEntity, Long mainId, String byEntity, Long byId, Boolean ifUpdate);

	/**
	 * 查询引用表数据
	 *
	 * @param byEntity
	 * @param byId
	 * @return
	 */
	public List<ReferenceEntity> deleteCheckReference(String byEntity, Long byId);

	/**
	 * 删除此表的引用数据
	 *
	 * @param mainEntity
	 * @param mainId
	 */
	public void deleteTableReference(String mainEntity, Long mainId);

	/**
	 * 准确的删除一条
	 * @param mainEntity
	 * @param mainId
	 * @param byEntity
	 * @param byId
	 */
	public void deleteOneReference(String mainEntity, Long mainId, String byEntity, Long byId);
}

