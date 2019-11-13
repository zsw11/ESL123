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

import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.service.ModelToolRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 机种治工具关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/modeltoolrela")
public class ModelToolRelaController {
    @Autowired
    private ModelToolRelaService modelToolRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:modeltoolrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = modelToolRelaService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:modeltoolrela:info")
    public R info(@PathVariable("id") Integer id){
		ModelToolRelaEntity modelToolRela = modelToolRelaService.selectById(id);

        return R.ok().put("modelToolRela", modelToolRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:modeltoolrela:save")
    public R save(@RequestBody ModelToolRelaEntity modelToolRela){
		modelToolRelaService.insert(modelToolRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:modeltoolrela:update")
    public R update(@RequestBody ModelToolRelaEntity modelToolRela){
		modelToolRelaService.updateById(modelToolRela);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:modeltoolrela:delete")
    public R delete(@RequestBody Integer[] ids){
		modelToolRelaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
