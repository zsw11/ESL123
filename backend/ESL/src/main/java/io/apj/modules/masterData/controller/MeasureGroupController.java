package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.common.utils.RD;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.MeasureGroupEntity;
import io.apj.modules.masterData.service.MeasureGroupService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 常用指标组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/measuregroup")
public class MeasureGroupController {
    @Autowired
    private MeasureGroupService measureGroupService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:measuregroup:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = measureGroupService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:measuregroup:info")
    public R info(@PathVariable("id") Integer id){
		MeasureGroupEntity measureGroup = measureGroupService.selectById(id);

        return R.ok().put("measureGroup", measureGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:measuregroup:save")
    public R save(@RequestBody MeasureGroupEntity measureGroup){
		measureGroupService.insert(measureGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:measuregroup:update")
    public R update(@RequestBody MeasureGroupEntity measureGroup){
		measureGroupService.updateById(measureGroup);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:measuregroup:delete")
    public RD delete(@RequestBody Integer[] ids){
		measureGroupService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
