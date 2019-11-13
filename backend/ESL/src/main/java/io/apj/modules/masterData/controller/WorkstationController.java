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

import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 工位
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstation")
public class WorkstationController {
    @Autowired
    private WorkstationService workstationService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:workstation:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = workstationService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:workstation:info")
    public R info(@PathVariable("id") Integer id){
		WorkstationEntity workstation = workstationService.selectById(id);

        return R.ok().put("workstation", workstation);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:workstation:save")
    public R save(@RequestBody WorkstationEntity workstation){
		workstationService.insert(workstation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:workstation:update")
    public R update(@RequestBody WorkstationEntity workstation){
		workstationService.updateById(workstation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:workstation:delete")
    public R delete(@RequestBody Integer[] ids){
		workstationService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
