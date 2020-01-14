package io.apj.modules.report.controller;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;
import io.apj.modules.report.service.ReportDeptRelaService;
import io.apj.modules.report.service.ReportGroupDeptRelaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 报表组部门关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-26 17:32:34
 */
@RestController
@RequestMapping("/api/v1/reportgroupdeptrela")
public class ReportGroupDeptRelaController {
    @Autowired
    private ReportGroupDeptRelaService reportGroupDeptRelaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:reportgroupdeptrela:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = reportGroupDeptRelaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("report:reportdeptrela:info")
    public R info(@PathVariable("id") Integer id){
		ReportGroupDeptRelaEntity reportDeptRela = reportGroupDeptRelaService.selectById(id);

        return R.ok().put("reportDeptRela", reportDeptRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("report:reportdeptrela:save")
    public R save(@RequestBody ReportGroupDeptRelaEntity reportDeptRela){
        reportGroupDeptRelaService.insert(reportDeptRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:reportdeptrela:update")
    public R update(@RequestBody ReportGroupDeptRelaEntity reportDeptRela){
        reportGroupDeptRelaService.updateById(reportDeptRela);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:reportdeptrela:delete")
    public R delete(@RequestBody Integer[] ids){
        reportGroupDeptRelaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
