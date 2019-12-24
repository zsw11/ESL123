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

import io.apj.modules.report.entity.ReportManMachineCombinationEntity;
import io.apj.modules.report.service.ReportManMachineCombinationService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * ${comments}
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
@RestController
@RequestMapping("/api/v1/reportmanmachinecombination")
public class ReportManMachineCombinationController {
    @Autowired
    private ReportManMachineCombinationService reportManMachineCombinationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:reportmanmachinecombination:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = reportManMachineCombinationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("report:reportmanmachinecombination:info")
    public R info(@PathVariable("id") Integer id){
		ReportManMachineCombinationEntity reportManMachineCombination = reportManMachineCombinationService.selectById(id);

        return R.ok().put("reportManMachineCombination", reportManMachineCombination);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("report:reportmanmachinecombination:save")
    public R save(@RequestBody ReportManMachineCombinationEntity reportManMachineCombination){
		reportManMachineCombinationService.insert(reportManMachineCombination);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:reportmanmachinecombination:update")
    public R update(@RequestBody ReportManMachineCombinationEntity reportManMachineCombination){
		reportManMachineCombinationService.updateById(reportManMachineCombination);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:reportmanmachinecombination:delete")
    public R delete(@RequestBody Integer[] ids){
		reportManMachineCombinationService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
