package io.apj.modules.report.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.service.StandardTimeService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 标准时间表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/standardtime")
public class StandardTimeController {
	@Autowired
	private StandardTimeService standardTimeService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:standardtime:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = standardTimeService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:standardtime:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		StandardTimeEntity standardTime = standardTimeService.selectById(id);
		standardTime.setModelName(modelService.selectById(standardTime.getModelId()).getName());
		standardTime.setPhaseName(phaseService.selectById(standardTime.getPhaseId()).getName());

		return RD.success(standardTime);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:standardtime:create")
	public ResponseEntity<Object> save(@RequestBody StandardTimeEntity standardTime) {
		standardTimeService.insert(standardTime);

		return RD.success(standardTime);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:standardtime:update")
	public ResponseEntity<Object> update(@RequestBody StandardTimeEntity standardTime) {
		standardTimeService.updateById(standardTime);

		return RD.success(standardTime);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:standardtime:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		standardTimeService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
