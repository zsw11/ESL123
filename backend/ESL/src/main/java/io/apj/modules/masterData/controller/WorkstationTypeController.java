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

import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.WorkstationTypeService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;



/**
 * 工位类型
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstationtype")
public class WorkstationTypeController extends AbstractController {
    @Autowired
    private WorkstationTypeService workstationTypeService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:workstationtype:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = workstationTypeService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:workstationtype:info")
    public RD info(@PathVariable("id") Integer id){
		WorkstationTypeEntity workstationType = workstationTypeService.selectById(id);

        return RD.build().put("data", workstationType);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:workstationtype:save")
    public RD save(@RequestBody WorkstationTypeEntity workstationType){
        workstationType.setCreateBy(getUserId().intValue());
		workstationTypeService.insert(workstationType);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:workstationtype:update")
    public RD update(@RequestBody WorkstationTypeEntity workstationType){
		workstationTypeService.updateById(workstationType);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:workstationtype:delete")
    public RD delete(@RequestBody Integer[] ids){
		workstationTypeService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
