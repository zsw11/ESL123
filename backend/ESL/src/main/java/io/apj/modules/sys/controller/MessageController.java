package io.apj.modules.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.annotation.SysLog;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.sys.entity.MessageEntity;
import io.apj.modules.sys.entity.MessageReadEntity;
import io.apj.modules.sys.service.MessageReadService;
import io.apj.modules.sys.service.MessageService;

/**
 * 消息通知
 *
 * @author samchen
 *
 * @date 2019-01-18 14:52:02
 */
@RestController
@RequestMapping("/api/v1/message")
public class MessageController extends AbstractController {
	@Autowired
	private MessageService messageService;

	@Autowired
	private MessageReadService messageReadService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("sys:message:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		if (StringUtils.isEmpty((String) params.get("type"))) {
			return RD.UNAUTHORIZED("TYPE_IS_NOT", "类型不能为空！");
		}

		PageUtils page = messageService.queryPage(params);

		return RD.success(RD.build().put("page", page));
	}

	/**
	 * 返回消息总数
	 *
	 */
	@RequestMapping("/count")
	public R count() {
		int count = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "effective");
		count += messageService.queryPage(map).getTotalCount();

		return R.ok().put("count", count);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:message:info")
	public R info(@PathVariable("id") Long id) {
		MessageEntity message = messageService.selectById(id);

		return R.ok().put("message", message);
	}

	/**
	 * 已读消息通知
	 */
	@SysLog("删除消息通知")
	@RequestMapping("/read")
//	@RequiresPermissions("sys:message:read")
	public R batchRead(@RequestBody Long[] ids) {
		List<MessageReadEntity> readList = new ArrayList<>();
		if (ids.length == 0) {
			return R.error(400, "消息ID不能为空！");
		}
		for (Long id : ids) {
			MessageEntity entity = messageService.selectById(id);
			// 只有提示类型消息才设置已读属性
			if (entity != null && !entity.getType().contains(":approve") && !entity.getType().contains(":process")
					&& !entity.getType().contains(":expire")) {

				MessageReadEntity message = messageReadService.selectOne(
						new EntityWrapper<MessageReadEntity>().eq("message_id", id).eq("staff_id", getStaffId()));
				if (message == null) {
					MessageReadEntity read = new MessageReadEntity();
					read.setCreateAt(new Date());
					read.setStaffId(getStaffId());
					read.setMessageId(id);
					readList.add(read);
				}
			}
		}
		if (readList.size() > 0) {
			messageReadService.insertBatch(readList, readList.size());
		}

		return R.ok();
	}

	/**
	 * 保存
	 */
	@SysLog("保存消息通知")
	@RequestMapping("/save")
//	@RequiresPermissions("sys:message:save")
	public R save(@RequestBody MessageEntity message) {

		ValidatorUtils.validateEntity(message);
		messageService.save(message);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改消息通知")
	@RequestMapping("/update")
	@RequiresPermissions("sys:message:update")
	public R update(@RequestBody MessageEntity message) {
		ValidatorUtils.validateEntity(message);
		messageService.update(message);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除消息通知")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:message:delete")
	public R delete(@RequestBody Long[] ids) {
		messageService.deleteBatch(ids);

		return R.ok();
	}

}
