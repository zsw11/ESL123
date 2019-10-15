package io.apj.modules.sys.dao;

import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.ReferenceEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

/**
 * 引用表
 * 
 * @author henry
 * 
 * @date 2019-01-14 14:55:47
 */
@Mapper
public interface ReferenceDao extends BaseMapper<ReferenceEntity> {
	List<ReferenceEntity> advancedSerach(Map<String, Object> params);
}
