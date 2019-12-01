package io.apj.modules.masterData.dao;

import com.baomidou.mybatisplus.plugins.Page;
import io.apj.modules.masterData.entity.ToolEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 治工具
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ToolDao extends BaseMapper<ToolEntity> {
    List<Map<String,Object>> selecttoolModel(@Param("id") Integer id, Page<Map<String,Object>> page,
                                             @Param("modelName")String modelName, @Param("deptId")Integer deptId,
                                             @Param("modelSeriesId")Integer modelSeriesId, @Param("code")String code);
	
}
