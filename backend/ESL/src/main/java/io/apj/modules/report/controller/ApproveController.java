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

import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.service.ApproveService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
@RestController
@RequestMapping("/api/v1/approve")
public class ApproveController {
	@Autowired
	private ApproveService approveService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:approve:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = approveService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:approve:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		ApproveEntity approve = approveService.selectById(id);

		return RD.success(approve);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:approve:create")
	public ResponseEntity<Object> save(@RequestBody ApproveEntity approve) {
		approveService.insert(approve);

		return RD.success(approve);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:approve:update")
	public ResponseEntity<Object> update(@RequestBody ApproveEntity approve) {
		approveService.updateById(approve);

		return RD.success(approve);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:approve:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		approveService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}