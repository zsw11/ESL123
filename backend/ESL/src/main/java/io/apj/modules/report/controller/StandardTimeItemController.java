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

import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.service.StandardTimeItemService;
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
@RequestMapping("/api/v1/standardtimeitem")
public class StandardTimeItemController {
    @Autowired
    private StandardTimeItemService standardTimeItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:standardtimeitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = standardTimeItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:standardtimeitem:detail")
    public R info(@PathVariable("id") Integer id){
		StandardTimeItemEntity standardTimeItem = standardTimeItemService.selectById(id);

        return R.ok().put("standardTimeItem", standardTimeItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:standardtimeitem:create")
    public R save(@RequestBody StandardTimeItemEntity standardTimeItem){
		standardTimeItemService.insert(standardTimeItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:standardtimeitem:update")
    public R update(@RequestBody StandardTimeItemEntity standardTimeItem){
		standardTimeItemService.updateById(standardTimeItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:standardtimeitem:delete")
    public R delete(@RequestBody Integer[] ids){
		standardTimeItemService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
