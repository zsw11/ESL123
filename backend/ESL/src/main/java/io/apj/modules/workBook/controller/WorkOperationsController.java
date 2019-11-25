package io.apj.modules.workBook.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkOperationsService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 分析表明细
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:02
 */
@RestController
@RequestMapping("/api/v1/workoperations")
public class WorkOperationsController {
    @Autowired
    private WorkOperationsService workOperationsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("workBook:workoperations:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = workOperationsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("workBook:workoperations:info")
    public R info(@PathVariable("id") Integer id){
		WorkOperationsEntity workOperations = workOperationsService.selectById(id);

        return R.ok().put("workOperations", workOperations);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("workBook:workoperations:create")
    public R save(@RequestBody WorkOperationsEntity workOperations){
		workOperationsService.insert(workOperations);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("workBook:workoperations:update")
    public R update(@RequestBody WorkOperationsEntity workOperations){
		workOperationsService.updateById(workOperations);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("workBook:workoperations:delete")
    public R delete(@RequestBody Integer[] ids){
		workOperationsService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
