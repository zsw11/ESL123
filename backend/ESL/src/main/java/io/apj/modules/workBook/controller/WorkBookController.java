package io.apj.modules.workBook.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.apj.common.utils.RD;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.WorkOperationsService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;

/**
 * 分析表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:03
 */
@RestController
@RequestMapping("/api/v1/workbook")
public class WorkBookController {
	@Autowired
	private WorkBookService workBookService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("workBook:workbook:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = workBookService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("workBook:workbook:info")
	public R info(@PathVariable("id") Integer id) {
		WorkBookEntity workBook = workBookService.selectById(id);

		return R.ok().put("workBook", workBook);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("workBook:workbook:create")
	public R save(@RequestBody WorkBookEntity workBook) {
		workBookService.insert(workBook);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("workBook:workbook:update")
	public RD update(@RequestBody WorkBookEntity workBook) {
		workBookService.updateWorkBook((Map<String, Object>) workBook);

		return RD.build();
	}

	/**
	 * 批量更新
	 */
	@RequestMapping("/updateOperation")
	@RequiresPermissions("workBook:workbook:update")
	public ResponseEntity<Object> updateOperation(@RequestBody Map<String, Object> params) {
		workBookService.updateOperation(params);
		Map<String, String> data = new HashMap<String, String>();
		data.put("message", "success");
		return RD.success(data);

	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("workBook:workbook:delete")
	public R delete(@RequestBody Integer[] ids) {
		workBookService.deleteBatchIds(Arrays.asList(ids));

		return R.ok();
	}

}
