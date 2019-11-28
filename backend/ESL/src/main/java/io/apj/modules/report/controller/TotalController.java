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

import io.apj.modules.report.entity.TotalEntity;
import io.apj.modules.report.service.TotalService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Report - Total表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@RestController
@RequestMapping("/api/v1/total")
public class TotalController {
	@Autowired
	private TotalService totalService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:total:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = totalService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:total:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		TotalEntity total = totalService.selectById(id);

		return RD.success(total);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:total:create")
	public ResponseEntity<Object> save(@RequestBody TotalEntity total) {
		totalService.insert(total);

		return RD.success(total);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:total:update")
	public ResponseEntity<Object> update(@RequestBody TotalEntity total) {
		totalService.updateById(total);

		return RD.success(total);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:total:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		totalService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
