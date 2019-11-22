package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 工位类型节点
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface WorkstationTypeNodeDao extends BaseMapper<WorkstationTypeNodeEntity> {
    @Select("select * from workstation_type_node t")
    List<WorkstationTypeNodeEntity> findAll();
}
