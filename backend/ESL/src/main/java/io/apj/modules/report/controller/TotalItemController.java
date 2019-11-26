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

import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Report - Total表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
@RestController
@RequestMapping("/api/v1/totalitem")
public class TotalItemController {
    @Autowired
    private TotalItemService totalItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:totalitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = totalItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:totalitem:detail")
    public R info(@PathVariable("id") Integer id){
		TotalItemEntity totalItem = totalItemService.selectById(id);

        return R.ok().put("totalItem", totalItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:totalitem:create")
    public R save(@RequestBody TotalItemEntity totalItem){
		totalItemService.insert(totalItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:totalitem:update")
    public R update(@RequestBody TotalItemEntity totalItem){
		totalItemService.updateById(totalItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:totalitem:delete")
    public R delete(@RequestBody Integer[] ids){
		totalItemService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
