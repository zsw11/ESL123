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

import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.OpertaionGroupService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 手顺组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/opertaiongroup")
public class OpertaionGroupController {
    @Autowired
    private OpertaionGroupService opertaionGroupService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:opertaiongroup:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = opertaionGroupService.queryPage(params);
        return RD.ok(RD.build().put("data",page));
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:opertaiongroup:info")
    public R info(@PathVariable("id") Integer id){
		OpertaionGroupEntity opertaionGroup = opertaionGroupService.selectById(id);

        return R.ok().put("opertaionGroup", opertaionGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:opertaiongroup:save")
    public R save(@RequestBody OpertaionGroupEntity opertaionGroup){
		opertaionGroupService.insert(opertaionGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:opertaiongroup:update")
    public R update(@RequestBody OpertaionGroupEntity opertaionGroup){
		opertaionGroupService.updateById(opertaionGroup);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:opertaiongroup:delete")
    public RD delete(@RequestBody Integer[] ids){
		opertaionGroupService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
