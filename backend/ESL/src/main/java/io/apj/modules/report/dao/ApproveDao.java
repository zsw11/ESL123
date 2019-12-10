package io.apj.modules.report.dao;

import io.apj.modules.report.entity.ApproveEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 报表审批表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
@Mapper
public interface ApproveDao extends BaseMapper<ApproveEntity> {
	
}
