package io.apj.modules.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表组部门关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-26 17:32:34
 */
@Mapper
public interface ReportGroupDeptRelaDao extends BaseMapper<ReportGroupDeptRelaEntity> {
	
}
