package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 工位
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface WorkstationService extends IService<WorkstationEntity> {

	PageUtils queryPage(Map<String, Object> params);

	Boolean wookStationIdIsSub(Integer wookStationId);

	/**
	 * 通过ids软删除
	 * 
	 * @param ids
	 */
	void deleteByIds(Collection<? extends Serializable> ids);

	/**
	 * 根据条件软删除
	 * 
	 * @param wrapper
	 */
	void deleteByWrapper(Wrapper<WorkstationEntity> wrapper);

	/**
	 * update时更新拼音和日期
	 * 
	 * @return
	 */
	void updatePinAndDataById(WorkstationEntity workstationEntity);

	/**
	 * 工位机种列表
	 * 
	 * @param id
	 * @param params
	 * @return
	 */
	PageUtils modelDetailList(Integer id, Map<String, Object> params);
	/**
	 * 导入
	 * @return
	 */
	ResponseEntity<Object> workstationImport(Map<String, Object> map);
}
