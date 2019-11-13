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

import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.masterData.service.ModelSeriesService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 机种系列
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/modelseries")
public class ModelSeriesController {
    @Autowired
    private ModelSeriesService modelSeriesService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:modelseries:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = modelSeriesService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:modelseries:info")
    public R info(@PathVariable("id") Integer id){
		ModelSeriesEntity modelSeries = modelSeriesService.selectById(id);

        return R.ok().put("modelSeries", modelSeries);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:modelseries:save")
    public R save(@RequestBody ModelSeriesEntity modelSeries){
		modelSeriesService.insert(modelSeries);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:modelseries:update")
    public R update(@RequestBody ModelSeriesEntity modelSeries){
		modelSeriesService.updateById(modelSeries);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:modelseries:delete")
    public RD delete(@RequestBody Integer[] ids){
		modelSeriesService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
