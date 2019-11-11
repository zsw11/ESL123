package io.apj.modules.workBook.dao;

import io.apj.modules.workBook.entity.WorkOperationsEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 分析表明细
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:02
 */
@Mapper
public interface WorkOperationsDao extends BaseMapper<WorkOperationsEntity> {
	
}
