package io.apj.modules.collection.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.collection.entity.CompareItemEntity;
import io.apj.modules.collection.service.CompareItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Collection - Compare表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/compareitem")
public class CompareItemController {
    @Autowired
    private CompareItemService compareItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("collection:compareitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = compareItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("collection:compareitem:detail")
    public R info(@PathVariable("id") Integer id){
		CompareItemEntity compareItem = compareItemService.selectById(id);

        return R.ok().put("compareItem", compareItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("collection:compareitem:create")
    public R save(@RequestBody CompareItemEntity compareItem){
		compareItemService.insert(compareItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("collection:compareitem:update")
    public R update(@RequestBody CompareItemEntity compareItem){
		compareItemService.updateById(compareItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("collection:compareitem:delete")
    public R delete(@RequestBody Integer[] ids){
		compareItemService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
