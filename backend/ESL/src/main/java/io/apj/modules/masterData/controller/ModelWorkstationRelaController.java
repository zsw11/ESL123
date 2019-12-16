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

import io.apj.modules.masterData.entity.ModelWorkstationRelaEntity;
import io.apj.modules.masterData.service.ModelWorkstationRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 机种工位关系表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-16 17:37:03
 */
@RestController
@RequestMapping("/api/v1/modelworkstationrela")
public class ModelWorkstationRelaController {
    @Autowired
    private ModelWorkstationRelaService modelWorkstationRelaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:modelworkstationrela:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = modelWorkstationRelaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:modelworkstationrela:info")
    public R info(@PathVariable("id") Integer id){
		ModelWorkstationRelaEntity modelWorkstationRela = modelWorkstationRelaService.selectById(id);

        return R.ok().put("modelWorkstationRela", modelWorkstationRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:modelworkstationrela:save")
    public R save(@RequestBody ModelWorkstationRelaEntity modelWorkstationRela){
		modelWorkstationRelaService.insert(modelWorkstationRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:modelworkstationrela:update")
    public R update(@RequestBody ModelWorkstationRelaEntity modelWorkstationRela){
		modelWorkstationRelaService.updateById(modelWorkstationRela);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:modelworkstationrela:delete")
    public R delete(@RequestBody Integer[] ids){
		modelWorkstationRelaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
