package io.apj.modules.report.dao;

import io.apj.modules.report.entity.ChangeRecordItemEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 履历表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Mapper
public interface ChangeRecordItemDao extends BaseMapper<ChangeRecordItemEntity> {
	
	List<ChangeRecordItemEntity> generateDataByWorkBook(@Param("workBookIds")List<Integer> workBookIds);
}
