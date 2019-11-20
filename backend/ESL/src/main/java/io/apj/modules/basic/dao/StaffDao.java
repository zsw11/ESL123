package io.apj.modules.basic.dao;

import io.apj.modules.basic.entity.StaffEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员信息
 * 
 * @author Royluo
 * 
 * @date 2018-12-10 17:12:16
 */
@Mapper
public interface StaffDao extends BaseMapper<StaffEntity> {

	List<StaffEntity> executeSql(String sql);

	Integer executeSqlCount(String sql);
}
