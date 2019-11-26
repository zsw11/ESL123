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

import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.service.StandardWorkService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 标准工数表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/standardwork")
public class StandardWorkController {
    @Autowired
    private StandardWorkService standardWorkService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:standardwork:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = standardWorkService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:standardwork:detail")
    public R info(@PathVariable("id") Integer id){
		StandardWorkEntity standardWork = standardWorkService.selectById(id);

        return R.ok().put("standardWork", standardWork);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:standardwork:create")
    public R save(@RequestBody StandardWorkEntity standardWork){
		standardWorkService.insert(standardWork);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:standardwork:update")
    public R update(@RequestBody StandardWorkEntity standardWork){
		standardWorkService.updateById(standardWork);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:standardwork:delete")
    public R delete(@RequestBody Integer[] ids){
		standardWorkService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
