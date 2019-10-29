package io.apj.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.sys.dao.MessageReadDao;
import io.apj.modules.sys.entity.MessageReadEntity;
import io.apj.modules.sys.service.MessageReadService;

@Service("messageReadService")
public class MessageReadServiceImpl extends ServiceImpl<MessageReadDao, MessageReadEntity>
		implements MessageReadService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<MessageReadEntity> page = this.selectPage(new Query<MessageReadEntity>(params).getPage(),
				new EntityWrapper<MessageReadEntity>().orderBy("create_at", false));

		return new PageUtils(page);
	}

	@Override
	public void save(MessageReadEntity messageRead) {
		messageRead.setCreateAt(new Date());
		ValidatorUtils.validateEntity(messageRead);
		this.insert(messageRead);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(MessageReadEntity messageRead) {

		this.updateAllColumnById(messageRead);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {

		this.deleteBatch(ids);
	}

}
