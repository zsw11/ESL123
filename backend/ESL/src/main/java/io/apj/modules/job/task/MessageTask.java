package io.apj.modules.job.task;

import io.apj.common.utils.DateUtils;
import io.apj.modules.sys.entity.MessageEntity;
import io.apj.modules.sys.service.MessageService;
import io.apj.modules.sys.service.SysConfigService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * 消息通知定时任务
 *
 * MessageTask为spring bean的名称
 *
 * @author samchen
 * @since 1.2.0 2019-02-16
 */
@Component("messageTask")
public class MessageTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageService messageService;

	@Autowired
	private SysConfigService sysConfigService;

	public void batchUpdate(String params) {
		logger.info("正在执行待处理消息超期处理，参数为：" + params);

		// 默认设置一天
		int minutes = 1440;

		String value = sysConfigService.getValue("MessageExpire");

		if (value != null) {
			List<MessageEntity> list = messageService
					.selectList(new EntityWrapper<MessageEntity>().like("type", ":process").eq("status", "release"));
			try {
				minutes = Integer.parseInt(value);
			} catch (Exception e) {
				logger.error("请在参数设置管理正确配置待处理消息超期处理分钟数配置，默认1440分钟，即一天为超期期限!");
			}
			Date now = new Date();
			List<MessageEntity> updateList = new ArrayList<>();
			for (MessageEntity item : list) {
				Date date = DateUtils.addDateMinutes(item.getCreateAt(), minutes);
				// 超期处理
				if (date.getTime() < now.getTime()) {
					item.setType(item.getType().split(":")[0] + ":expire");
					updateList.add(item);
				}

			}
			if (updateList.size() > 0) {
				messageService.updateBatchById(updateList, updateList.size());
			}

		} else {
			logger.error("请在参数设置管理配置待处理消息超期处理天数配置!");
		}

	}

}
