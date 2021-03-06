package io.apj.modules.masterData.dao;

import com.baomidou.mybatisplus.plugins.Page;
import io.apj.modules.masterData.entity.ModelEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 机种
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ModelDao extends BaseMapper<ModelEntity> {
    List<Map<String,Object>> selectmodelPart(@Param("id") Integer id, Page<Map<String,Object>> page, @Param("name")String name, @Param("remark")String remark, @Param("common") Integer common);

    List<Map<String,Object>> selectmodelTool(@Param("id") Integer id, Page<Map<String,Object>> page, @Param("name")String name,@Param("remark")String remark, @Param("common") Integer common);
	
}
