package io.apj.modules.sys.dao;

import io.apj.modules.sys.entity.CodeRuleEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

/**
 * 编码规则
 *
 * @author Sam
 *
 * @date 2018-12-18 11:34:00
 */
@Mapper
public interface CodeRuleDao extends BaseMapper<CodeRuleEntity> {
	List<CodeRuleEntity> advancedSearch(Map<String, Object> params);
}
