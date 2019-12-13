package io.apj.modules.collection.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;
import io.apj.modules.sys.controller.AbstractController;
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
public class RevisionHistoryController extends AbstractController {
	@Autowired
	private RevisionHistoryService revisionHistoryService;

	@Autowired
	private RevisionHistoryItemService revisionHistoryItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:revisionhistory:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = revisionHistoryService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:revisionhistory:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		RevisionHistoryEntity revisionHistory = revisionHistoryService.selectById(id);
		EntityWrapper<RevisionHistoryItemEntity> ew = new EntityWrapper<>();
		ew.eq("collection_revision_history_id",id);
		revisionHistory.setItems(revisionHistoryItemService.selectList(ew));
		return RD.success(revisionHistory);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:revisionhistory:create")
	public ResponseEntity<Object> save(@RequestBody RevisionHistoryEntity revisionHistory) {
		revisionHistory.setDeptId(getUserDeptId().intValue());
		revisionHistoryService.insert(revisionHistory);

		return RD.success(revisionHistory);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:revisionhistory:update")
	public ResponseEntity<Object> update(@RequestBody RevisionHistoryEntity revisionHistory) {
		revisionHistoryService.updateEntity(revisionHistory);

		return RD.success(revisionHistory);
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
