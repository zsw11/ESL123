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

import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ReportService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 报表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:report:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = reportService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:report:info")
    public R info(@PathVariable("id") Integer id){
		ReportEntity report = reportService.selectById(id);

        return R.ok().put("report", report);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:report:save")
    public R save(@RequestBody ReportEntity report){
		reportService.insert(report);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:report:update")
    public R update(@RequestBody ReportEntity report){
		reportService.updateById(report);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:report:delete")
    public R delete(@RequestBody Integer[] ids){
		reportService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
