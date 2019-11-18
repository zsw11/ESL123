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

import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.service.ActionService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;



/**
 * 关键词
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/action")
public class ActionController extends AbstractController {
    @Autowired
    private ActionService actionService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:action:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = actionService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:action:info")
    public RD info(@PathVariable("id") Integer id){
    	ActionEntity action = actionService.selectById(id);

        return RD.build().put("data", action);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:action:save")
    public RD save(@RequestBody ActionEntity action){
    	action.setCreateBy(getUserId().intValue());
        actionService.insert(action);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:action:update")
    public RD update(@RequestBody ActionEntity action){
    	actionService.updateById(action);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:action:delete")
    public RD delete(@RequestBody Integer[] ids){
    	actionService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
