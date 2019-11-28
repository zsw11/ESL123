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

import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Collection - Compare表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/compare")
public class CompareController {
	@Autowired
	private CompareService compareService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:compare:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = compareService.queryPage(params);

		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:compare:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		CompareEntity compare = compareService.selectById(id);

		return RD.ok(compare);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:compare:create")
	public ResponseEntity<Object> save(@RequestBody CompareEntity compare) {
		compareService.insert(compare);

		return RD.ok(compare);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:compare:update")
	public ResponseEntity<Object> update(@RequestBody CompareEntity compare) {
		compareService.updateById(compare);

		return RD.ok(compare);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:compare:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		compareService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
