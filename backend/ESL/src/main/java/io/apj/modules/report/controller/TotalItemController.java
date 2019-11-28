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

import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Report - Total表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@RestController
@RequestMapping("/api/v1/totalitem")
public class TotalItemController {
	@Autowired
	private TotalItemService totalItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:totalitem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = totalItemService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:totalitem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		TotalItemEntity totalItem = totalItemService.selectById(id);

		return RD.ok(totalItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:totalitem:create")
	public ResponseEntity<Object> save(@RequestBody TotalItemEntity totalItem) {
		totalItemService.insert(totalItem);

		return RD.ok(totalItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:totalitem:update")
	public ResponseEntity<Object> update(@RequestBody TotalItemEntity totalItem) {
		totalItemService.updateById(totalItem);

		return RD.ok(totalItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:totalitem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		totalItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
