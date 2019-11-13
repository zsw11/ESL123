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

import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.PartService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 部品
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/part")
public class PartController {
    @Autowired
    private PartService partService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:part:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = partService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:part:info")
    public R info(@PathVariable("id") Integer id){
		PartEntity part = partService.selectById(id);

        return R.ok().put("part", part);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:part:save")
    public R save(@RequestBody PartEntity part){
		partService.insert(part);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:part:update")
    public R update(@RequestBody PartEntity part){
		partService.updateById(part);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:part:delete")
    public R delete(@RequestBody Integer[] ids){
		partService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
