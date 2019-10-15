package io.apj.modules.sys.dao;

import io.apj.modules.sys.entity.MessageEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 消息通知
 * 
 * @author samchen
 * 
 * @date 2019-01-18 14:52:02
 */
@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {
	public List<Long> getIdsBySql(String sql);
}
