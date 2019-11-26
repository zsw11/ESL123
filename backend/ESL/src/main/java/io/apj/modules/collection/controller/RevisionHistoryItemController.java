package io.apj.modules.collection.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Collection - Revision History 表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@RestController
@RequestMapping("/api/v1/revisionhistoryitem")
public class RevisionHistoryItemController {
    @Autowired
    private RevisionHistoryItemService revisionHistoryItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("collection:revisionhistoryitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = revisionHistoryItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("collection:revisionhistoryitem:detail")
    public R info(@PathVariable("id") Integer id){
		RevisionHistoryItemEntity revisionHistoryItem = revisionHistoryItemService.selectById(id);

        return R.ok().put("revisionHistoryItem", revisionHistoryItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("collection:revisionhistoryitem:create")
    public R save(@RequestBody RevisionHistoryItemEntity revisionHistoryItem){
		revisionHistoryItemService.insert(revisionHistoryItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("collection:revisionhistoryitem:update")
    public R update(@RequestBody RevisionHistoryItemEntity revisionHistoryItem){
		revisionHistoryItemService.updateById(revisionHistoryItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("collection:revisionhistoryitem:delete")
    public R delete(@RequestBody Integer[] ids){
		revisionHistoryItemService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
