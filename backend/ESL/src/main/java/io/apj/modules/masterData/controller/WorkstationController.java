package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
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
import io.apj.common.utils.RD;



/**
 * 工位
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstation")
public class WorkstationController extends AbstractController {
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
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:workstation:info")
    public RD info(@PathVariable("id") Integer id){
		WorkstationEntity workstation = workstationService.selectById(id);

        return RD.build().put("data", workstation);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:workstation:save")
    public RD save(@RequestBody WorkstationEntity workstation){
        workstation.setCreateBy(getUserId().intValue());
		workstationService.insert(workstation);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:workstation:update")
    public RD update(@RequestBody WorkstationEntity workstation){
		workstationService.updateById(workstation);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:workstation:delete")
    public RD delete(@RequestBody Integer[] ids){
		workstationService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
