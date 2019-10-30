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
 * 文件清除定时任务
 *
 * fileReferenceTask为spring bean的名称
 *
 * @author samchen
 * @since 1.2.0 2019-02-16
 */
@Component("fileReferenceTask")
public class FileReferenceTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageService messageService;

	@Autowired
	private SysConfigService sysConfigService;

	private static int EXPIRE_DAYS = 7;

	public void batchDelete(String params) {
		logger.info("正在执行未被引用文件删除处理，参数为：" + params);

		

	}

}
