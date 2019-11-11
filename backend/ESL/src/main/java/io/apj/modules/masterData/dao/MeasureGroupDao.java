package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.MeasureGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 常用指标组合
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@Mapper
public interface MeasureGroupDao extends BaseMapper<MeasureGroupEntity> {
	
}
