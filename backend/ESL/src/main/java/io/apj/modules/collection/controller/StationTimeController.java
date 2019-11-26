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

import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Collection - 工位时间表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/stationtime")
public class StationTimeController {
    @Autowired
    private StationTimeService stationTimeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("collection:stationtime:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = stationTimeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("collection:stationtime:detail")
    public R info(@PathVariable("id") Integer id){
		StationTimeEntity stationTime = stationTimeService.selectById(id);

        return R.ok().put("stationTime", stationTime);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("collection:stationtime:create")
    public R save(@RequestBody StationTimeEntity stationTime){
		stationTimeService.insert(stationTime);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("collection:stationtime:update")
    public R update(@RequestBody StationTimeEntity stationTime){
		stationTimeService.updateById(stationTime);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("collection:stationtime:delete")
    public R delete(@RequestBody Integer[] ids){
		stationTimeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
