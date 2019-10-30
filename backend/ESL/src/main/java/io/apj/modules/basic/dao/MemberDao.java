package io.apj.modules.basic.dao;

import io.apj.modules.basic.entity.MemberEntity;
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
public interface MemberDao extends BaseMapper<MemberEntity> {

	List<MemberEntity> executeSql(String sql);

	Integer executeSqlCount(String sql);
}
