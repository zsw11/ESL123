package io.apj.modules.report.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.apj.modules.report.entity.AshcraftTableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
@Mapper
public interface AshcraftTableDao extends BaseMapper<AshcraftTableEntity> {

}
