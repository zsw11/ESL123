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

import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 手顺
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/operationgroupoperation")
public class OperationGroupOperationController {
    @Autowired
    private OperationGroupOperationService operationGroupOperationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:operationgroupoperation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = operationGroupOperationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:operationgroupoperation:info")
    public R info(@PathVariable("id") Integer id){
		OperationGroupOperationEntity operationGroupOperation = operationGroupOperationService.selectById(id);

        return R.ok().put("operationGroupOperation", operationGroupOperation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:operationgroupoperation:save")
    public R save(@RequestBody OperationGroupOperationEntity operationGroupOperation){
		operationGroupOperationService.insert(operationGroupOperation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:operationgroupoperation:update")
    public R update(@RequestBody OperationGroupOperationEntity operationGroupOperation){
		operationGroupOperationService.updateById(operationGroupOperation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:operationgroupoperation:delete")
    public R delete(@RequestBody Integer[] ids){
		operationGroupOperationService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}