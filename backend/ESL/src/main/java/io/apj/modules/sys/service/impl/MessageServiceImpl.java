package io.apj.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.basic.entity.MemberEntity;
import io.apj.modules.basic.service.MemberService;
import io.apj.modules.sys.dao.MessageDao;
import io.apj.modules.sys.entity.MessageEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.service.MessageService;
import io.apj.modules.sys.service.ShiroService;

@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ShiroService shiroService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {

		EntityWrapper<MessageEntity> wrapper = new EntityWrapper<MessageEntity>();
		List<Long> messageIds = new ArrayList<>();

		// 获取当前账号用户信息
		SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
		MemberEntity member = memberService.selectOne(new EntityWrapper<MemberEntity>().eq("user_id", user.getId()));

		// 区分未读
		if (member != null) {
			String sql = "SELECT message_id FROM sys_message_read WHERE member_id = " + member.getId()
					+ " group by message_id";
			messageIds = this.getIdsBySql(sql);
		}
		// 根据数据权限过滤部门
//		wrapper.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
//				.orderBy("create_at", false);

		// 根据来源类型
		Set<String> permissions = shiroService.getUserPermissions(user.getId());
		List<String> typeList = new ArrayList<>();
		for (String str : permissions) {
			String[] arr = str.split("\\:");
			if (arr.length > 2 && "message".equals(arr[2])) {
				typeList.add(arr[1]);
			}
		}
		if (typeList.size() > 0) {
			wrapper.in("source_type", typeList);
		}

		switch ((String) params.get("type")) {
		case "all": { // 所有
			wrapper.isNull("to_member_id");
			break;
		}
		case "unread": { // 未读（不包括待审批，待处理，超期）
			if (messageIds.size() > 0) {
				wrapper.notIn("id", messageIds);
			}
			wrapper.isNull("to_member_id");
			wrapper.notLike("type", ":approve");
			wrapper.notLike("type", ":process");
			wrapper.notLike("type", ":expire");
			wrapper.eq("status", "release");
			break;
		}
		case "effective": { // 未读、待审批，待处理，超期）
			if (messageIds.size() > 0) {
				wrapper.notIn("id", messageIds);
			}
			wrapper.isNull("to_member_id");
			wrapper.eq("status", "release");
			break;
		}
		case "approve": { // 待审批，状态status为release，之后会处理为fixed
			wrapper.like("type", ":approve");
			wrapper.eq("to_member_id", member.getId());
			wrapper.eq("status", "release");
			break;
		}
		case "process": { // 待处理，状态status为release，之后会处理为fixed
			wrapper.like("type", ":process");
			wrapper.isNull("to_member_id");
			wrapper.eq("status", "release");
			break;
		}
		case "expire": { // 超期，状态status为release，之后会处理为fixed
			wrapper.like("type", ":expire");
			wrapper.isNull("to_member_id");
			wrapper.eq("status", "release");
			break;
		}
		default:
			break;
		}

		// 查询指定用户
		if (member != null) {
			wrapper.or().eq("to_member_id", member.getId());
			switch ((String) params.get("type")) {
			case "all": {
				break;
			}
			case "unread": {
				if (messageIds.size() > 0) {
					wrapper.notIn("id", messageIds);
				}
				wrapper.notLike("type", ":approve");
				wrapper.notLike("type", ":process");
				wrapper.notLike("type", ":expire");
				wrapper.eq("status", "release");

				break;
			}
			case "effective": { // 未读、待审批，待处理，超期）
				if (messageIds.size() > 0) {
					wrapper.notIn("id", messageIds);
				}
				wrapper.eq("status", "release");
				break;
			}
			case "approve": {
				wrapper.like("type", ":approve");
				wrapper.eq("status", "release");
				break;
			}
			case "process": {
				wrapper.like("type", ":process");
				wrapper.eq("status", "release");
				break;
			}
			case "expire": {
				wrapper.like("type", ":expire");
				wrapper.eq("status", "release");
				break;
			}
			default: {
				break;
			}
			}
		}

		// 普通查询
		wrapper.eq(StringUtils.isNotEmpty((String) params.get("status")), "status", params.get("status"))
				.like(StringUtils.isNotEmpty((String) params.get("content")), "content", (String) params.get("content"))
				.like(StringUtils.isNotEmpty((String) params.get("title")), "title", (String) params.get("title"));

		Page<MessageEntity> page = this.selectPage(new Query<MessageEntity>(params).getPage(), wrapper);

		// 标记已读消息
		if (messageIds.size() > 0) {
			Set<Long> list = new HashSet<Long>(messageIds);
			for (MessageEntity entity : page.getRecords()) {
				if (!list.add(entity.getId())) {
					entity.setIfRead(true);
				}
			}
		}

		return new PageUtils(page);
	}

	@Override
	public void save(MessageEntity message) {
		message.setCreateAt(new Date());
		ValidatorUtils.validateEntity(message);
		this.insert(message);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(MessageEntity message) {
		this.updateAllColumnById(message);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {

		this.deleteBatch(ids);
	}

	@Override
	public List<Long> getIdsBySql(String sql) {
		// TODO Auto-generated method stub
		return baseMapper.getIdsBySql(sql);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteMessage(long sourceId, String sourceType) {

		EntityWrapper<MessageEntity> wrapper = new EntityWrapper<MessageEntity>();
		wrapper.eq("source_id", sourceId);
		wrapper.eq("source_type", sourceType);
		wrapper.eq("status", "release");
		wrapper.like("type", ":process");
		wrapper.or().like("type", ":expire");
		MessageEntity entity = this.selectOne(wrapper);
		if (entity != null) {
			this.deleteById(entity.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void fixedMessage(long sourceId, String sourceType) {

		EntityWrapper<MessageEntity> wrapper = new EntityWrapper<MessageEntity>();
		wrapper.eq("source_id", sourceId);
		wrapper.eq("source_type", sourceType);
		wrapper.eq("status", "release");
		wrapper.like("type", ":process");
		wrapper.or().like("type", ":expire");
		MessageEntity entity = this.selectOne(wrapper);
		if (entity != null) {
			entity.setStatus("fixed");
			this.updateById(entity);
		}
	}

}
