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

import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.service.StandardTimeService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 标准时间表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/standardtime")
public class StandardTimeController {
    @Autowired
    private StandardTimeService standardTimeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:standardtime:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = standardTimeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:standardtime:detail")
    public R info(@PathVariable("id") Integer id){
		StandardTimeEntity standardTime = standardTimeService.selectById(id);

        return R.ok().put("standardTime", standardTime);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:standardtime:create")
    public R save(@RequestBody StandardTimeEntity standardTime){
		standardTimeService.insert(standardTime);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:standardtime:update")
    public R update(@RequestBody StandardTimeEntity standardTime){
		standardTimeService.updateById(standardTime);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:standardtime:delete")
    public R delete(@RequestBody Integer[] ids){
		standardTimeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
