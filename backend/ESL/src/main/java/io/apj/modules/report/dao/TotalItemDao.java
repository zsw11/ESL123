package io.apj.modules.report.dao;

import io.apj.modules.report.entity.TotalItemEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Report - Total表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@Mapper
public interface TotalItemDao extends BaseMapper<TotalItemEntity> {

    List<TotalItemEntity> generateDataByWorkBook(@Param("workBookIds") List<Integer> workBookIds);

}
