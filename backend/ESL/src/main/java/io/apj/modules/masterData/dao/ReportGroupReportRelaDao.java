package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * 报表组报表关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ReportGroupReportRelaDao extends BaseMapper<ReportGroupReportRelaEntity> {
    List<ReportEntity> selectReportNameByReportGroupId(int id);
	
}
