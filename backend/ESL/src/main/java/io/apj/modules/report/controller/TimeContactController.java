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

import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.service.TimeContactService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * Report - 时间联络表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/timecontact")
public class TimeContactController {
    @Autowired
    private TimeContactService timeContactService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:timecontact:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = timeContactService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:timecontact:detail")
    public R info(@PathVariable("id") Integer id){
		TimeContactEntity timeContact = timeContactService.selectById(id);

        return R.ok().put("timeContact", timeContact);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:timecontact:create")
    public R save(@RequestBody TimeContactEntity timeContact){
		timeContactService.insert(timeContact);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:timecontact:update")
    public R update(@RequestBody TimeContactEntity timeContact){
		timeContactService.updateById(timeContact);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:timecontact:delete")
    public R delete(@RequestBody Integer[] ids){
		timeContactService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
