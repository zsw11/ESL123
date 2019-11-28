package io.apj.modules.report.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.service.TimeContactService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Report - 时间联络表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/timecontact")
public class TimeContactController {
	@Autowired
	private TimeContactService timeContactService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:timecontact:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = timeContactService.queryPage(params);
		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:timecontact:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		TimeContactEntity timeContact = timeContactService.selectById(id);

		return RD.success(timeContact);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:timecontact:create")
	public ResponseEntity<Object> save(@RequestBody TimeContactEntity timeContact) {
		timeContactService.insert(timeContact);

		return RD.success(timeContact);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:timecontact:update")
	public ResponseEntity<Object> update(@RequestBody TimeContactEntity timeContact) {
		timeContactService.updateById(timeContact);

		return RD.success(timeContact);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:timecontact:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		timeContactService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
