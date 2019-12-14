package io.apj.modules.report.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
@RestController
@RequestMapping("/api/v1/approve")
public class ApproveController {
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
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;
    @Autowired
    private TotalService totalService;
    @Autowired
    private WorkBookService workBookService;
    @Autowired
    private StationTimeService stationTimeService;
    @Autowired
    private CompareService compareService;
    @Autowired
    private MostValueService mostValueService;
    @Autowired
    private RevisionHistoryService revisionHistoryService;
    @Autowired
    private TimeContactService timeContactService;
    @Autowired
    private StandardTimeService standardTimeService;
    @Autowired
    private StandardWorkService standardWorkService;
    @Autowired
    private ChangeRecordService changeRecordService;


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
     * 保存
     *
     * @return
     */
    @RequestMapping("/create")
    @RequiresPermissions("report:approve:create")
    public List<Object> save(@RequestBody ApproveEntity approve) {
        //判断所选报表组里的报表是否生成了
        int pid = approve.getPhaseId();
        int mid = approve.getModelId();
        String stlst = approve.getStlst();
        int reportGroupId = approve.getReportGroupId();
        List<ReportEntity> reportEntityList = reportGroupReportRelaService.selectReportNameByReportGroupId(reportGroupId);
        List<Object> reportItemList = new ArrayList<>();
        for (ReportEntity item : reportEntityList) {
            //判断是否符合3个字段
            ReportEntity reportEntity = reportService.selectById(item.getId());
            String reportName = reportEntity.getEname();
            switch (reportName) {
                case "report_total":
                    List<TotalEntity> total = (List<TotalEntity>) totalService.selectList(new EntityWrapper<TotalEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (total.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "work_book":
                    List<WorkBookEntity> workBookEntity = (List<WorkBookEntity>) workBookService.selectList(new EntityWrapper<WorkBookEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (workBookEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "collection_station_time":
                    List<StationTimeEntity> stationTimeEntity = (List<StationTimeEntity>) stationTimeService.selectList(new EntityWrapper<StationTimeEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (stationTimeEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "collection_compare":
                    List<CompareEntity> compareEntity = (List<CompareEntity>) compareService.selectList(new EntityWrapper<CompareEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    reportItemList.add(reportEntity);
                    break;
                case "collection_most_value":
                    List<MostValueEntity> mostValueEntity = (List<MostValueEntity>) mostValueService.selectList(new EntityWrapper<MostValueEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (mostValueEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "collection_revision_history":
                    List<RevisionHistoryEntity> revisionHistoryEntity = (List<RevisionHistoryEntity>) revisionHistoryService.selectList(new EntityWrapper<RevisionHistoryEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (!revisionHistoryEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "report_time_contact":
                    List<TimeContactEntity> timeContactEntity = (List<TimeContactEntity>) timeContactService.selectList(new EntityWrapper<TimeContactEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (timeContactEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "report_standard_time":
                    List<StandardTimeEntity> standardTimeEntity = (List<StandardTimeEntity>) standardTimeService.selectList(new EntityWrapper<StandardTimeEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (standardTimeEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "report_standard_work":
                    List<StandardWorkEntity> standardWorkEntity = (List<StandardWorkEntity>) standardWorkService.selectList(new EntityWrapper<StandardWorkEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (standardWorkEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "report_change_record":
                    List<ChangeRecordEntity> changeRecordEntity = (List<ChangeRecordEntity>) changeRecordService.selectList(new EntityWrapper<ChangeRecordEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (changeRecordEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
            }
        }
        if (!reportItemList.isEmpty()) {
            return reportItemList;
        } else {
            approveService.insert(approve);
        }
        return null;

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
