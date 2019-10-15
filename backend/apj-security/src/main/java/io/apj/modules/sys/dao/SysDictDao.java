package io.apj.modules.sys.dao;

import io.apj.modules.sys.entity.SysDictEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典项
 * 
 * @author samchen
 * 
 * @date 2018-12-05 14:16:20
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {
	
}
