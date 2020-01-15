package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.NodeModelWorkstationRelaEntity;
import io.apj.modules.masterData.service.NodeModelWorkstationRelaService;



/**
 * 报表组部门关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2020-01-15 18:11:00
 */
@RestController
@RequestMapping("/api/v1/nodemodelworkstationrela")
public class NodeModelWorkstationRelaController {
    @Autowired
    private NodeModelWorkstationRelaService nodeModelWorkstationRelaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = nodeModelWorkstationRelaService.queryPage(params);

        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:info")
    public RD info(@PathVariable("id") Integer id){
		NodeModelWorkstationRelaEntity nodeModelWorkstationRela = nodeModelWorkstationRelaService.selectById(id);

        return RD.build().put("nodeModelWorkstationRela", nodeModelWorkstationRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:save")
    public RD save(@RequestBody NodeModelWorkstationRelaEntity nodeModelWorkstationRela){
		nodeModelWorkstationRelaService.insert(nodeModelWorkstationRela);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:update")
    public RD update(@RequestBody NodeModelWorkstationRelaEntity nodeModelWorkstationRela){
		nodeModelWorkstationRelaService.updateById(nodeModelWorkstationRela);

        return RD.build();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:delete")
    public RD delete(@RequestBody Integer[] ids){
		nodeModelWorkstationRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
