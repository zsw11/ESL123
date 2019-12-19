package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.PartEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

/**
 * 部品
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface PartService extends IService<PartEntity> {

    PageUtils queryPage(Map<String, Object> params);

	PageUtils partModeRelaList(Integer id, Map<String, Object> params);

	/**
	 * 软删除实体对象
	 * @param partList
	 */
	void deleteList(List<PartEntity> partList);

	/**
	 * 通过ids软删除
	 * @param ids
	 */
	void deleteByIds(Collection<? extends Serializable> ids);

	/**
	 * 根据条件软删除
	 * @param wrapper
	 */
	void deleteByWrapper(Wrapper<PartEntity> wrapper);

	/**
	 * update时更新拼音和日期
	 * @return
	 */
	void updatePinAndDataById(PartEntity partEntity);
	
	/**
	 * 导入
	 * @return 
	 */
	ResponseEntity<Object> partImport(Map<String, Object> map); 
}

