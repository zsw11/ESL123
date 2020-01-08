package io.apj.modules.masterData.controller;

import java.util.*;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.common.utils.PageUtils;

/**
 * 报表组
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/reportgroup")
public class ReportGroupController extends AbstractController {
	@Autowired
	private ReportGroupService reportGroupService;
	@Autowired
	private ReportGroupReportRelaService reportGroupReportRelaService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:reportgroup:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = reportGroupService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 报表组详情下的报表
	 */
	@RequestMapping("/detailreport/{id}")
	@RequiresPermissions("masterData:reportgroup:info")
	public RD info(@PathVariable("id") Integer id) {
		HashMap<Object, Object> data = new HashMap<>();
		data.put("data",reportGroupService.reportGroupRelaList(id));

		return RD.build().put("data", data);

	}
	/**
	 * 报表组详情
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:reportgroup:info")
	public RD infoReport(@PathVariable("id") Integer id) {
		ReportGroupEntity reportGroupEntity= reportGroupService.selectById(id);
		return RD.build().put("data", reportGroupEntity);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:reportgroup:create")
	public RD save(@RequestBody ReportGroupEntity reportGroup) {
		reportGroup.setPinyin(PinyinUtil.getPinYin(reportGroup.getName()));
		reportGroup.setCreateBy(getUserId().intValue());
		reportGroupService.insert(reportGroup);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:reportgroup:update")
	public RD update(@RequestBody ReportGroupEntity reportGroup) {
		reportGroup.setUpdateBy(getUserId().intValue());
		reportGroupService.updatePinAndDataById(reportGroup);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:reportgroup:delete")
	public RD delete(@RequestBody Integer[] ids) {
		// 判断报表组下是否有报表
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("reportGroup", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.build().put("msg", reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("reportGroup", ids[i].longValue());
			}
		}

		reportGroupService.deleteByIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

}
