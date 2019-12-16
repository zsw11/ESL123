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

import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.service.StandardWorkService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 标准工数表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/standardwork")
public class StandardWorkController {
	@Autowired
	private StandardWorkService standardWorkService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private ModelService modelService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:standardwork:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = standardWorkService.selectListTest(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:standardwork:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		StandardWorkEntity standardWork = standardWorkService.selectById(id);
		standardWork.setModelName(modelService.selectById(standardWork.getModelId()).getName());
		standardWork.setPhaseName(phaseService.selectById(standardWork.getPhaseId()).getName());

		return RD.success(standardWork);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:standardwork:create")
	public ResponseEntity<Object> save(@RequestBody StandardWorkEntity standardWork) {
		standardWorkService.insert(standardWork);

		return RD.success(standardWork);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:standardwork:update")
	public ResponseEntity<Object> update(@RequestBody StandardWorkEntity standardWork) {
		standardWorkService.updateById(standardWork);


		return RD.success(standardWork);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:standardwork:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		standardWorkService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
