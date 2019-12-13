package io.apj.modules.collection.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * Collection - 工位时间表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/stationtime")
public class StationTimeController extends AbstractController {
	@Autowired
	private StationTimeService stationTimeService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("collection:stationtime:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = stationTimeService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("collection:stationtime:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		StationTimeEntity stationTime = stationTimeService.selectById(id);

		return RD.success(stationTime);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("collection:stationtime:create")
	public ResponseEntity<Object> save(@RequestBody StationTimeEntity stationTime) {
		stationTime.setDeptId(getUserDeptId().intValue());
		stationTimeService.insert(stationTime);

		return RD.success(stationTime);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("collection:stationtime:update")
	public ResponseEntity<Object> update(@RequestBody StationTimeEntity stationTime) {
		stationTimeService.updateById(stationTime);

		return RD.success(stationTime);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("collection:stationtime:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		stationTimeService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
