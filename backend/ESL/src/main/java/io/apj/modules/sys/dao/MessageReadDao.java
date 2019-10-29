package io.apj.modules.sys.dao;

import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.MessageReadEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

/**
 * 已读消息用户
 * 
 * @author samchen
 * 
 * @date 2019-01-18 14:52:01
 */
@Mapper
public interface MessageReadDao extends BaseMapper<MessageReadEntity> {
	List<MessageReadEntity> advancedSerach(Map<String, Object> params);
}
