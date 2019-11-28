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

import io.apj.modules.collection.entity.StationTimeItemEntity;
import io.apj.modules.collection.service.StationTimeItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Collection - 工位时间表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/stationtimeitem")
public class StationTimeItemController {
	@Autowired
	private StationTimeItemService stationTimeItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:stationtimeitem:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = stationTimeItemService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:stationtimeitem:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		StationTimeItemEntity stationTimeItem = stationTimeItemService.selectById(id);

		return RD.success(stationTimeItem);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:stationtimeitem:create")
	public ResponseEntity<Object> save(@RequestBody StationTimeItemEntity stationTimeItem) {
		stationTimeItemService.insert(stationTimeItem);

		return RD.success(stationTimeItem);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:stationtimeitem:update")
	public ResponseEntity<Object> update(@RequestBody StationTimeItemEntity stationTimeItem) {
		stationTimeItemService.updateById(stationTimeItem);

		return RD.success(stationTimeItem);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:stationtimeitem:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		stationTimeItemService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
