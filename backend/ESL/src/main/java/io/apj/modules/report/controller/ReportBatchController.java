package io.apj.modules.report.controller;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.modules.report.entity.ReportBatchEntity;
import io.apj.modules.report.service.ReportBatchService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
@RestController
@RequestMapping("/api/v1/reportbatch")
public class ReportBatchController {
    @Autowired
    private ReportBatchService reportBatchService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:reportbatch:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = reportBatchService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     * @return
     */
    @RequestMapping("/detail/{id}")
    //@RequiresPermissions("report:reportbatch:info")
    public ResponseEntity<Object> info(@PathVariable("id") Integer id){
		List list = reportBatchService.selectAllWorkBook(id);

        return RD.success(list);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("report:reportbatch:save")
    public R save(@RequestBody ReportBatchEntity reportBatchEntity){
        reportBatchService.insert(reportBatchEntity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:reportbatch:update")
    public R update(@RequestBody ReportBatchEntity reportBatchEntity){
        reportBatchService.updateById(reportBatchEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:reportbatch:delete")
    public R delete(@RequestBody Integer[] ids){
        reportBatchService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
