package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 报表组报表关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/reportgroupreportrela")
public class ReportGroupReportRelaController {
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:reportgroupreportrela:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = reportGroupReportRelaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:reportgroupreportrela:info")
    public R info(@PathVariable("id") Integer id){
		ReportGroupReportRelaEntity reportGroupReportRela = reportGroupReportRelaService.selectById(id);

        return R.ok().put("reportGroupReportRela", reportGroupReportRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:reportgroupreportrela:save")
    public R save(@RequestBody ReportGroupReportRelaEntity reportGroupReportRela){
		reportGroupReportRelaService.insert(reportGroupReportRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:reportgroupreportrela:update")
    public R update(@RequestBody ReportGroupReportRelaEntity reportGroupReportRela){
		reportGroupReportRelaService.updateById(reportGroupReportRela);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:reportgroupreportrela:delete")
    public R delete(@RequestBody Integer[] ids){
		reportGroupReportRelaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}