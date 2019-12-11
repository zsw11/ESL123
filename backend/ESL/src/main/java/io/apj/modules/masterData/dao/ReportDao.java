package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.ReportEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 报表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ReportDao extends BaseMapper<ReportEntity> {
    @Select("SELECT id from report WHERE name=#{name}")
    Integer selectByNameTest(@Param("name") String name);

    @Select("SELECT * from report_total where model_id=#{mid} AND phase_id = #{pid} AND stlst = #{stlst}")
    ReportEntity selectByEname(@Param("pid") int pid, @Param("mid")int mid, @Param("stlst") String stlst);
}
