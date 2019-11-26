package io.apj.modules.report.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.service.ChangeRecordService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 履历表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/changerecord")
public class ChangeRecordController {
    @Autowired
    private ChangeRecordService changeRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:changerecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = changeRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:changerecord:detail")
    public R info(@PathVariable("id") Integer id){
		ChangeRecordEntity changeRecord = changeRecordService.selectById(id);

        return R.ok().put("changeRecord", changeRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:changerecord:create")
    public R save(@RequestBody ChangeRecordEntity changeRecord){
		changeRecordService.insert(changeRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:changerecord:update")
    public R update(@RequestBody ChangeRecordEntity changeRecord){
		changeRecordService.updateById(changeRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:changerecord:delete")
    public R delete(@RequestBody Integer[] ids){
		changeRecordService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
