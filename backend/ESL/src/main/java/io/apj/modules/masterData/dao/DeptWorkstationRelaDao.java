package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.DeptWorkstationRelaEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 组织机构工位关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface DeptWorkstationRelaDao extends BaseMapper<DeptWorkstationRelaEntity> {
	
}
