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

import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Collection - Revision History 表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/revisionhistory")
public class RevisionHistoryController {
	@Autowired
	private RevisionHistoryService revisionHistoryService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:revisionhistory:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = revisionHistoryService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:revisionhistory:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		RevisionHistoryEntity revisionHistory = revisionHistoryService.selectById(id);

		return RD.ok(revisionHistory);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:revisionhistory:create")
	public ResponseEntity<Object> save(@RequestBody RevisionHistoryEntity revisionHistory) {
		revisionHistoryService.insert(revisionHistory);

		return RD.ok(revisionHistory);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:revisionhistory:update")
	public ResponseEntity<Object> update(@RequestBody RevisionHistoryEntity revisionHistory) {
		revisionHistoryService.updateById(revisionHistory);

		return RD.ok(revisionHistory);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:revisionhistory:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		revisionHistoryService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
