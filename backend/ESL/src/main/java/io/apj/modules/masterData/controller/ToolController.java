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

import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.ToolService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 治工具
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/tool")
public class ToolController {
    @Autowired
    private ToolService toolService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:tool:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = toolService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("masterData:tool:info")
    public R info(@PathVariable("id") Integer id){
		ToolEntity tool = toolService.selectById(id);

        return R.ok().put("tool", tool);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("masterData:tool:save")
    public R save(@RequestBody ToolEntity tool){
		toolService.insert(tool);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:tool:update")
    public R update(@RequestBody ToolEntity tool){
		toolService.updateById(tool);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:tool:delete")
    public R delete(@RequestBody Integer[] ids){
		toolService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}