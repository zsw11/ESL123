package io.apj.modules.masterData.controller;

import java.io.Serializable;
import java.util.*;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.service.ApproveService;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ReportService;
import io.apj.common.utils.PageUtils;

/**
 * 报表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/report")
public class ReportController extends AbstractController {
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportGroupService reportGroupService;
	@Autowired
	private ReportGroupReportRelaService reportGroupReportRelaService;
	@Autowired
	private ApproveService approveService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:report:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = reportService.queryPage(params);
		page.getData();
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:report:info")
	public RD info(@PathVariable("id") Integer id) {
		ReportEntity report = reportService.selectById(id);

		return RD.build().put("data", report);
	}
	/**
	 * 报表属于哪些报表组并过滤
	 * @return
	 */
	@RequestMapping("/reportGroup")
	public ResponseEntity<Object> reportGroup(@RequestBody  Map<String,Object> data) {
		List<ReportGroupEntity> reportGroupEntityList = reportService.selectReportGroup(data);
		return RD.success(reportGroupEntityList);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:report:create")
	public RD save(@RequestBody ReportEntity report) {
		report.setPinyin(PinyinUtil.getPinYin(report.getName()));
		report.setCreateBy(getUserId().intValue());
		reportService.insert(report);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:report:update")
	public RD update(@RequestBody ReportEntity report) {
		report.setPinyin(PinyinUtil.getPinYin(report.getName()));
		reportService.updateById(report);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:report:delete")
	public RD delete(@RequestBody Integer[] ids) {
		reportService.deleteBatchIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

}
