package io.apj.modules.collection.dao;

import io.apj.modules.collection.entity.StationTimeEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * Collection - 工位时间表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Mapper
public interface StationTimeDao extends BaseMapper<StationTimeEntity> {
	
}
