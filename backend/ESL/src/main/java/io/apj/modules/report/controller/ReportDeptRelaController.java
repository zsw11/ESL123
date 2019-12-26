package io.apj.modules.report.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.service.ReportDeptRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 报表部门关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-26 17:32:34
 */
@RestController
@RequestMapping("/api/v1/reportdeptrela")
public class ReportDeptRelaController {
    @Autowired
    private ReportDeptRelaService reportDeptRelaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:reportdeptrela:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = reportDeptRelaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("report:reportdeptrela:info")
    public R info(@PathVariable("id") Integer id){
		ReportDeptRelaEntity reportDeptRela = reportDeptRelaService.selectById(id);

        return R.ok().put("reportDeptRela", reportDeptRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("report:reportdeptrela:save")
    public R save(@RequestBody ReportDeptRelaEntity reportDeptRela){
		reportDeptRelaService.insert(reportDeptRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:reportdeptrela:update")
    public R update(@RequestBody ReportDeptRelaEntity reportDeptRela){
		reportDeptRelaService.updateById(reportDeptRela);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:reportdeptrela:delete")
    public R delete(@RequestBody Integer[] ids){
		reportDeptRelaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
