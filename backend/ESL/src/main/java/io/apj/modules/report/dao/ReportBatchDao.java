package io.apj.modules.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.apj.modules.report.entity.ReportBatchEntity;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${comments}
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
@Mapper
public interface ReportBatchDao extends BaseMapper<ReportBatchEntity> {
	
}
