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

import io.apj.modules.report.entity.StandardWorkItemEntity;
import io.apj.modules.report.service.StandardWorkItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 标准时间表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/standardworkitem")
public class StandardWorkItemController {
	@Autowired
	private StandardWorkItemService standardWorkItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:standardworkitem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = standardWorkItemService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:standardworkitem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		StandardWorkItemEntity standardWorkItem = standardWorkItemService.selectById(id);

		return RD.ok(standardWorkItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:standardworkitem:create")
	public ResponseEntity<Object> save(@RequestBody StandardWorkItemEntity standardWorkItem) {
		standardWorkItemService.insert(standardWorkItem);

		return RD.ok(standardWorkItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:standardworkitem:update")
	public ResponseEntity<Object> update(@RequestBody StandardWorkItemEntity standardWorkItem) {
		standardWorkItemService.updateById(standardWorkItem);

		return RD.ok(standardWorkItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:standardworkitem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		standardWorkItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
