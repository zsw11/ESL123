package io.apj.modules.basic.dao;

import io.apj.modules.basic.entity.JobEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 岗位信息
 * 
 * @author Royluo
 * 
 * @date 2018-12-07 17:39:37
 */
@Mapper
public interface JobDao extends BaseMapper<JobEntity> {
	
}
