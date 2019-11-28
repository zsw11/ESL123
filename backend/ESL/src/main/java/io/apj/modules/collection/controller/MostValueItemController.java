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

import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.service.MostValueItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Collection - MOST Value 表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/mostvalueitem")
public class MostValueItemController {
	@Autowired
	private MostValueItemService mostValueItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:mostvalueitem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = mostValueItemService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:mostvalueitem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		MostValueItemEntity mostValueItem = mostValueItemService.selectById(id);

		return RD.success(mostValueItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:mostvalueitem:create")
	public ResponseEntity<Object> save(@RequestBody MostValueItemEntity mostValueItem) {
		mostValueItemService.insert(mostValueItem);

		return RD.success(mostValueItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:mostvalueitem:update")
	public ResponseEntity<Object> update(@RequestBody MostValueItemEntity mostValueItem) {
		mostValueItemService.updateById(mostValueItem);

		return RD.success(mostValueItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:mostvalueitem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		mostValueItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
