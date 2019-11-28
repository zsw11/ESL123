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

import io.apj.modules.report.entity.ChangeRecordItemEntity;
import io.apj.modules.report.service.ChangeRecordItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 履历表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/changerecorditem")
public class ChangeRecordItemController {
	@Autowired
	private ChangeRecordItemService changeRecordItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:changerecorditem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = changeRecordItemService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:changerecorditem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		ChangeRecordItemEntity changeRecordItem = changeRecordItemService.selectById(id);

		return RD.success(changeRecordItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:changerecorditem:create")
	public ResponseEntity<Object> save(@RequestBody ChangeRecordItemEntity changeRecordItem) {
		changeRecordItemService.insert(changeRecordItem);

		return RD.success(changeRecordItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:changerecorditem:update")
	public ResponseEntity<Object> update(@RequestBody ChangeRecordItemEntity changeRecordItem) {
		changeRecordItemService.updateById(changeRecordItem);

		return RD.success(changeRecordItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:changerecorditem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		changeRecordItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
