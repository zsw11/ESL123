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

import io.apj.modules.report.entity.ApproveHistoryEntity;
import io.apj.modules.report.service.ApproveHistoryService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 报表历史审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/approvehistory")
public class ApproveHistoryController {
	@Autowired
	private ApproveHistoryService approveHistoryService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:approvehistory:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = approveHistoryService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:approvehistory:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		ApproveHistoryEntity approveHistory = approveHistoryService.selectById(id);

		return RD.success(approveHistory);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:approvehistory:create")
	public ResponseEntity<Object> save(@RequestBody ApproveHistoryEntity approveHistory) {
		approveHistoryService.insert(approveHistory);

		return RD.success(approveHistory);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:approvehistory:update")
	public ResponseEntity<Object> update(@RequestBody ApproveHistoryEntity approveHistory) {
		approveHistoryService.updateById(approveHistory);

		return RD.success(approveHistory);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:approvehistory:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		approveHistoryService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
