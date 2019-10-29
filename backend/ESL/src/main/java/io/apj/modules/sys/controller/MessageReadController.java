package io.apj.modules.sys.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.modules.sys.service.MessageReadService;

/**
 * 已读消息用户
 *
 * @author samchen
 * 
 * @date 2019-01-18 14:52:01
 */
@RestController
@RequestMapping("sys/messageread")
public class MessageReadController extends AbstractController {
	@Autowired
	private MessageReadService messageReadService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = messageReadService.queryPage(params);

		return R.ok().put("page", page);
	}

}
