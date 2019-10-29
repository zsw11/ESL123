package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.MessageReadEntity;

import java.util.Map;
import java.util.List;

/**
 * 已读消息用户
 *
 * @author samchen
 * 
 * @date 2019-01-18 14:52:01
 */
public interface MessageReadService extends IService<MessageReadEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
	 * 保存配置信息
	 */
    public void save(MessageReadEntity messageRead);
    
    /**
	 * 更新配置信息
	 */
	public void update(MessageReadEntity messageRead);
	
	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);
	
}

