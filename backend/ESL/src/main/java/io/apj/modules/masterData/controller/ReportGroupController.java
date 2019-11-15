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

import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 报表组
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/reportgroup")
public class ReportGroupController extends AbstractController {
    @Autowired
    private ReportGroupService reportGroupService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:reportgroup:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = reportGroupService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:reportgroup:info")
    public R info(@PathVariable("id") Integer id){
		ReportGroupEntity reportGroup = reportGroupService.selectById(id);

        return R.ok().put("data", reportGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:reportgroup:save")
    public R save(@RequestBody ReportGroupEntity reportGroup){
        reportGroup.setCreateBy(getUserId().intValue());
		reportGroupService.insert(reportGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:reportgroup:update")
    public R update(@RequestBody ReportGroupEntity reportGroup){
		reportGroupService.updateById(reportGroup);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:reportgroup:delete")
    public RD delete(@RequestBody Integer[] ids){
		reportGroupService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
