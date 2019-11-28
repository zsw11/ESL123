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

import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.service.MostValueService;
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
@RequestMapping("/api/v1/mostvalue")
public class MostValueController {
	@Autowired
	private MostValueService mostValueService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:mostvalue:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = mostValueService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:mostvalue:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		MostValueEntity mostValue = mostValueService.selectById(id);

		return RD.ok(mostValue);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:mostvalue:create")
	public ResponseEntity<Object> save(@RequestBody MostValueEntity mostValue) {
		mostValueService.insert(mostValue);

		return RD.ok(mostValue);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:mostvalue:update")
	public ResponseEntity<Object> update(@RequestBody MostValueEntity mostValue) {
		mostValueService.updateById(mostValue);

		return RD.ok(mostValue);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:mostvalue:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		mostValueService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
