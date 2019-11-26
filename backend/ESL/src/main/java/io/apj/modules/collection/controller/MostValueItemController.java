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

import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.service.MostValueItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Collection - MOST Value 表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/mostvalueitem")
public class MostValueItemController {
    @Autowired
    private MostValueItemService mostValueItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("collection:mostvalueitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = mostValueItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("collection:mostvalueitem:detail")
    public R info(@PathVariable("id") Integer id){
		MostValueItemEntity mostValueItem = mostValueItemService.selectById(id);

        return R.ok().put("mostValueItem", mostValueItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("collection:mostvalueitem:create")
    public R save(@RequestBody MostValueItemEntity mostValueItem){
		mostValueItemService.insert(mostValueItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("collection:mostvalueitem:update")
    public R update(@RequestBody MostValueItemEntity mostValueItem){
		mostValueItemService.updateById(mostValueItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("collection:mostvalueitem:delete")
    public R delete(@RequestBody Integer[] ids){
		mostValueItemService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
