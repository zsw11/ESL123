package io.apj.modules.collection.dao;

import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * Collection - Revision History 表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Mapper
public interface RevisionHistoryItemDao extends BaseMapper<RevisionHistoryItemEntity> {
	
}
