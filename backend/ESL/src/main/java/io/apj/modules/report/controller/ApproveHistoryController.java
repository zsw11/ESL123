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

import io.apj.modules.report.entity.ApproveHistoryEntity;
import io.apj.modules.report.service.ApproveHistoryService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/approvehistory")
public class ApproveHistoryController {
    @Autowired
    private ApproveHistoryService approveHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:approvehistory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = approveHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:approvehistory:detail")
    public R info(@PathVariable("id") Integer id){
		ApproveHistoryEntity approveHistory = approveHistoryService.selectById(id);

        return R.ok().put("approveHistory", approveHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:approvehistory:create")
    public R save(@RequestBody ApproveHistoryEntity approveHistory){
		approveHistoryService.insert(approveHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:approvehistory:update")
    public R update(@RequestBody ApproveHistoryEntity approveHistory){
		approveHistoryService.updateById(approveHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:approvehistory:delete")
    public R delete(@RequestBody Integer[] ids){
		approveHistoryService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
