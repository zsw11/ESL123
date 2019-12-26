package io.apj.modules.collection.dao;

import io.apj.modules.collection.entity.StationTimeItemEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Collection - 工位时间表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Mapper
public interface StationTimeItemDao extends BaseMapper<StationTimeItemEntity> {

    List<StationTimeItemEntity> generateDataByWorkBook(@Param("workBookIds")List<Integer> workBookIds);
}
