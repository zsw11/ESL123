package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.ModelEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 机种
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ModelDao extends BaseMapper<ModelEntity> {
	
}
