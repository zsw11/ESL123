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

import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.service.StandardTimeItemService;
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
@RequestMapping("/api/v1/standardtimeitem")
public class StandardTimeItemController {
	@Autowired
	private StandardTimeItemService standardTimeItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:standardtimeitem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = standardTimeItemService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:standardtimeitem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		StandardTimeItemEntity standardTimeItem = standardTimeItemService.selectById(id);

		return RD.ok(standardTimeItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:standardtimeitem:create")
	public ResponseEntity<Object> save(@RequestBody StandardTimeItemEntity standardTimeItem) {
		standardTimeItemService.insert(standardTimeItem);

		return RD.ok(standardTimeItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:standardtimeitem:update")
	public ResponseEntity<Object> update(@RequestBody StandardTimeItemEntity standardTimeItem) {
		standardTimeItemService.updateById(standardTimeItem);

		return RD.ok(standardTimeItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:standardtimeitem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		standardTimeItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
