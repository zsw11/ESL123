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

import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 机种
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/model")
public class ModelController {
    @Autowired
    private ModelService modelService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:model:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = modelService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:model:info")
    public R info(@PathVariable("id") Integer id){
		ModelEntity model = modelService.selectById(id);

        return R.ok().put("model", model);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:model:save")
    public R save(@RequestBody ModelEntity model){
		modelService.insert(model);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:model:update")
    public R update(@RequestBody ModelEntity model){
		modelService.updateById(model);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:model:delete")
    public RD delete(@RequestBody Integer[] ids){
		modelService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
