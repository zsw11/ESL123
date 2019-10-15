package io.apj.modules.sys.dao;

import io.apj.modules.sys.entity.CodeRuleItemEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

/**
 * 编码规则子项
 *
 * @author Sam
 *
 * @date 2018-12-18 11:34:01
 */
@Mapper
public interface CodeRuleItemDao extends BaseMapper<CodeRuleItemEntity> {
	List<CodeRuleItemEntity> advancedSearch(Map<String, Object> params);
}
