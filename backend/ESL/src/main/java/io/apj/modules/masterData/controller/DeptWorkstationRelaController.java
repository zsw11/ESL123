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

import io.apj.modules.masterData.entity.DeptWorkstationRelaEntity;
import io.apj.modules.masterData.service.DeptWorkstationRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 组织机构工位关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/deptworkstationrela")
public class DeptWorkstationRelaController {
    @Autowired
    private DeptWorkstationRelaService deptWorkstationRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:deptworkstationrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = deptWorkstationRelaService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:deptworkstationrela:info")
    public R info(@PathVariable("id") Integer id){
		DeptWorkstationRelaEntity deptWorkstationRela = deptWorkstationRelaService.selectById(id);

        return R.ok().put("deptWorkstationRela", deptWorkstationRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:deptworkstationrela:save")
    public R save(@RequestBody DeptWorkstationRelaEntity deptWorkstationRela){
		deptWorkstationRelaService.insert(deptWorkstationRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:deptworkstationrela:update")
    public R update(@RequestBody DeptWorkstationRelaEntity deptWorkstationRela){
		deptWorkstationRelaService.updateById(deptWorkstationRela);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:deptworkstationrela:delete")
    public RD delete(@RequestBody Integer[] ids){
		deptWorkstationRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
