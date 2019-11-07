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

import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 机种部品关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/modelpartrela")
public class ModelPartRelaController {
    @Autowired
    private ModelPartRelaService modelPartRelaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:modelpartrela:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = modelPartRelaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:modelpartrela:info")
    public R info(@PathVariable("id") Integer id){
		ModelPartRelaEntity modelPartRela = modelPartRelaService.selectById(id);

        return R.ok().put("modelPartRela", modelPartRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:modelpartrela:save")
    public R save(@RequestBody ModelPartRelaEntity modelPartRela){
		modelPartRelaService.insert(modelPartRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:modelpartrela:update")
    public R update(@RequestBody ModelPartRelaEntity modelPartRela){
		modelPartRelaService.updateById(modelPartRela);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:modelpartrela:delete")
    public R delete(@RequestBody Integer[] ids){
		modelPartRelaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
