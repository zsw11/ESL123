package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.ToolService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;



/**
 * 治工具
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/tool")
public class ToolController extends AbstractController {
    @Autowired
    private ToolService toolService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:tool:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = toolService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:tool:info")
    public RD info(@PathVariable("id") Integer id){
		ToolEntity tool = toolService.selectById(id);

        return RD.build().put("data", tool);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:tool:save")
    public RD save(@RequestBody ToolEntity tool){
        tool.setCreateBy(getUserId().intValue());
		toolService.insert(tool);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:tool:update")
    public RD update(@RequestBody ToolEntity tool){
		toolService.updateById(tool);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:tool:delete")
    public RD delete(@RequestBody Integer[] ids){
		toolService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
