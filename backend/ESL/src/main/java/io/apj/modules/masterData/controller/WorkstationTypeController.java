package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.WorkstationTypeService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 工位类型
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstationtype")
public class WorkstationTypeController {
    @Autowired
    private WorkstationTypeService workstationTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:workstationtype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = workstationTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:workstationtype:info")
    public R info(@PathVariable("id") Integer id){
		WorkstationTypeEntity workstationType = workstationTypeService.selectById(id);

        return R.ok().put("workstationType", workstationType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:workstationtype:save")
    public R save(@RequestBody WorkstationTypeEntity workstationType){
		workstationTypeService.insert(workstationType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:workstationtype:update")
    public R update(@RequestBody WorkstationTypeEntity workstationType){
		workstationTypeService.updateById(workstationType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:workstationtype:delete")
    public R delete(@RequestBody Integer[] ids){
		workstationTypeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}