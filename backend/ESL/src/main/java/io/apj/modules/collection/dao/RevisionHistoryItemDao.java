package io.apj.modules.collection.dao;

import io.apj.modules.collection.entity.RevisionHistoryItemEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	List<RevisionHistoryItemEntity> generateDataByWorkBook(@Param("workBookIds")List<Integer> workBookIds);
	
}
