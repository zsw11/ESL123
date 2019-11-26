package io.apj.modules.report.dao;

import io.apj.modules.report.entity.TimeContactEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * Report - 时间联络表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Mapper
public interface TimeContactDao extends BaseMapper<TimeContactEntity> {
	
}
