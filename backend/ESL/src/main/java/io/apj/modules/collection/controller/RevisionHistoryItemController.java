package io.apj.modules.collection.controller;

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

import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Collection - Revision History 表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/revisionhistoryitem")
public class RevisionHistoryItemController {
	@Autowired
	private RevisionHistoryItemService revisionHistoryItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:revisionhistoryitem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = revisionHistoryItemService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:revisionhistoryitem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		RevisionHistoryItemEntity revisionHistoryItem = revisionHistoryItemService.selectById(id);

		return RD.ok(revisionHistoryItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:revisionhistoryitem:create")
	public ResponseEntity<Object> save(@RequestBody RevisionHistoryItemEntity revisionHistoryItem) {
		revisionHistoryItemService.insert(revisionHistoryItem);

		return RD.ok(revisionHistoryItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:revisionhistoryitem:update")
	public ResponseEntity<Object> update(@RequestBody RevisionHistoryItemEntity revisionHistoryItem) {
		revisionHistoryItemService.updateById(revisionHistoryItem);

		return RD.ok(revisionHistoryItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:revisionhistoryitem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		revisionHistoryItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
