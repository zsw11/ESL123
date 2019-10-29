package io.apj.modules.sys.dao;

import io.apj.modules.sys.entity.SysDictEntity;
import io.apj.modules.sys.entity.SysDictTypeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字典类型
 * 
 * @author samchen
 * 
 * @date 2018-12-05 14:16:20
 */
@Mapper
public interface SysDictTypeDao extends BaseMapper<SysDictTypeEntity> {
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
