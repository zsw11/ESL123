package io.apj.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.MessageEntity;

/**
 * 消息通知
 *
 * @author samchen
 * 
 * @date 2019-01-18 14:52:02
 */
public interface MessageService extends IService<MessageEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 保存配置信息
	 */
	public void save(MessageEntity message);

	/**
	 * 更新配置信息
	 */
	public void update(MessageEntity message);

	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);

	/**
	 * 根据sql返回列表
	 */
	public List<Long> getIdsBySql(String sql);

	/**
	 * 删除待处理/已超期消息通知
	 */
	public void deleteMessage(long sourceId, String sourceType);

	/**
	 * 设置待处理/已超期消息通知完成
	 */
	public void fixedMessage(long sourceId, String sourceType);

}
