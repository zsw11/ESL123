package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonElement;
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

import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.common.utils.PageUtils;


/**
 * 报表组报表关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/reportgroupreportrela")
public class ReportGroupReportRelaController extends AbstractController {
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
//    @RequiresPermissions("masterData:reportgroupreportrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = reportGroupReportRelaService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
//    @RequiresPermissions("masterData:reportgroupreportrela:info")
    public RD<JsonElement> info(@PathVariable("id") Integer id){
		ReportGroupReportRelaEntity reportGroupReportRela = reportGroupReportRelaService.selectById(id);

        return RD.build().put("data", reportGroupReportRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
//    @RequiresPermissions("masterData:reportgroupreportrela:save")
    public RD<JsonElement> save(@RequestBody ReportGroupReportRelaEntity reportGroupReportRela){
        reportGroupReportRela.setCreateBy(getUserId().intValue());
		reportGroupReportRelaService.insert(reportGroupReportRela);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("masterData:reportgroupreportrela:update")
    public RD<JsonElement> update(@RequestBody ReportGroupReportRelaEntity reportGroupReportRela){
		reportGroupReportRelaService.updateById(reportGroupReportRela);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:reportgroupreportrela:delete")
    public RD<JsonElement> delete(@RequestBody Integer[] ids){
		reportGroupReportRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
