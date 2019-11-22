package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 机种治工具关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ModelToolRelaDao extends BaseMapper<ModelToolRelaEntity> {
    List<Map<String,Object>> selectModelByToolId(Integer id, Map<String, Object> params);
	
}
