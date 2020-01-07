package io.apj.modules.basic.dao;

import io.apj.modules.basic.entity.StaffEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 人员信息
 * 
 * @author Royluo
 * 
 * @date 2018-12-10 17:12:16
 */
@Mapper
public interface StaffDao extends BaseMapper<StaffEntity> {

    @Select("SELECT name from basic_staff WHERE user_id=#{id}")
    String selectNameByUserId(Integer id);

    List<StaffEntity> executeSql(String sql);

	Integer executeSqlCount(String sql);
}
