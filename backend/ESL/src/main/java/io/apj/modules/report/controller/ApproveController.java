package io.apj.modules.report.controller;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.entity.ApproveHistoryEntity;
import io.apj.modules.report.service.*;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
@RestController
@RequestMapping("/api/v1/approve")
public class ApproveController extends AbstractController {
    @Autowired
    private ApproveService approveService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ReportGroupService reportGroupService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ApproveHistoryService approveHistoryService;



    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:approve:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
        PageUtils page = approveService.queryPage(params);

        return RD.success(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("report:approve:detail")
    public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
        ApproveEntity approve = approveService.selectById(id);
        if (approve.getModelId() != null) {
            approve.setModelName(modelService.selectById(approve.getModelId()).getName());
        }
        if (approve.getPhaseId() != null) {
            approve.setPhaseName(phaseService.selectById(approve.getPhaseId()).getName());
        }
        if (approve.getReportGroupId() != null) {
            approve.setReportGroupName(reportGroupService.selectById(approve.getReportGroupId()).getName());
        }
        //报表审批详情，符合三个字段所在的报表组里的报表
        List<ReportEntity> reportEntity = reportService.selectApproveList(id);
        Map<String, Object> page = new HashMap<>();
        page.put("data", reportEntity);
        page.put("approve", approve);
        return RD.success(page);
    }

    /**
     * 从具体的报表来保存
     *
     * @return
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:approve:create")
    public List<Object> save(@RequestBody ApproveEntity approve) {
        //提交审批，点击确定时
        List<Object> data = approveService.insertApprove(approve);

        return data;

    }

    @RequestMapping("/createview")
//    @RequiresPermissions("report:approve:create")
    public ResponseEntity<Object> saveView(Integer id, @RequestBody Map<String,Object> data) {
        ApproveEntity approveEntity = approveService.selectById(id);
        approveEntity.setDeptId(getUserDeptId().intValue());
        ResponseEntity<Object> approveHistoryEntity = approveService.saveView(approveEntity,data);
        return RD.success(approveHistoryEntity);

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:approve:update")
    public ResponseEntity<Object> update(@RequestBody ApproveEntity approve) {
        approveService.updateById(approve);
        approveHistoryService.insertApproveHisttory(approve);

        return RD.success(approve);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:approve:delete")
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        approveService.deleteBatchIds(Arrays.asList(ids));

        return RD.NO_CONTENT(RD.build());
    }

    @RequestMapping("/download")
//    @RequiresPermissions("report:approve:list")
    public void download(@RequestBody Map<String, Object> params, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        approveService.download(params, response);

    }
}
