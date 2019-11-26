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

import io.apj.modules.report.entity.StandardWorkItemEntity;
import io.apj.modules.report.service.StandardWorkItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 标准时间表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/standardworkitem")
public class StandardWorkItemController {
    @Autowired
    private StandardWorkItemService standardWorkItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:standardworkitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = standardWorkItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:standardworkitem:detail")
    public R info(@PathVariable("id") Integer id){
		StandardWorkItemEntity standardWorkItem = standardWorkItemService.selectById(id);

        return R.ok().put("standardWorkItem", standardWorkItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:standardworkitem:create")
    public R save(@RequestBody StandardWorkItemEntity standardWorkItem){
		standardWorkItemService.insert(standardWorkItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:standardworkitem:update")
    public R update(@RequestBody StandardWorkItemEntity standardWorkItem){
		standardWorkItemService.updateById(standardWorkItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:standardworkitem:delete")
    public R delete(@RequestBody Integer[] ids){
		standardWorkItemService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
