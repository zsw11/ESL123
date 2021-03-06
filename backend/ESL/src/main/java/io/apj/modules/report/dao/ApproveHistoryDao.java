package io.apj.modules.report.dao;

import io.apj.modules.report.entity.ApproveHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 报表审批表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@Mapper
public interface ApproveHistoryDao extends BaseMapper<ApproveHistoryEntity> {
	
}
