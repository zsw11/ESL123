package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.SysDictEntity;
import io.apj.modules.sys.entity.SysDictTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 字典类型
 *
 * @author samchen
 * 
 * @date 2018-12-05 14:16:20
 */
public interface SysDictTypeService extends IService<SysDictTypeEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 根据字典类型和子项编码查询子项名称
	 * 
	 * @param type
	 * @param code
	 * @return
	 */
	String getNameByTypeAndCode(String type, String code);

	/**
	 * 根据字典类型和查询子项列表
	 * 
	 * @param type
	 * @return
	 */
	List<SysDictEntity> getListByType(String type);
}
