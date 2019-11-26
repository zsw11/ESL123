package io.apj.modules.report.dao;

import io.apj.modules.report.entity.StandardWorkItemEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 标准时间表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Mapper
public interface StandardWorkItemDao extends BaseMapper<StandardWorkItemEntity> {
	
}
