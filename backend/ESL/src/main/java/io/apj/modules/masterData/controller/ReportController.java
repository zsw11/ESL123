package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.common.utils.RD;
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
public class ReportController extends AbstractController {
    @Autowired
    private ReportService reportService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:report:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = reportService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:report:info")
    public R info(@PathVariable("id") Integer id){
		ReportEntity report = reportService.selectById(id);

        return R.ok().put("report", report);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:report:save")
    public R save(@RequestBody ReportEntity report){
        report.setCreateBy(getUserId().intValue());
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
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:report:delete")
    public RD delete(@RequestBody Integer[] ids){
		reportService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
