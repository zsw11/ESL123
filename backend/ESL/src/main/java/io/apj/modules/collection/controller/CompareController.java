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

import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Collection - Compare表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/compare")
public class CompareController {
    @Autowired
    private CompareService compareService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("collection:compare:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = compareService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("collection:compare:detail")
    public R info(@PathVariable("id") Integer id){
		CompareEntity compare = compareService.selectById(id);

        return R.ok().put("compare", compare);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("collection:compare:create")
    public R save(@RequestBody CompareEntity compare){
		compareService.insert(compare);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("collection:compare:update")
    public R update(@RequestBody CompareEntity compare){
		compareService.updateById(compare);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("collection:compare:delete")
    public R delete(@RequestBody Integer[] ids){
		compareService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
