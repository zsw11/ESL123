package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.ModelWorkstationRelaEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 机种工位关系表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-16 17:37:03
 */
@Mapper
public interface ModelWorkstationRelaDao extends BaseMapper<ModelWorkstationRelaEntity> {
	
}
