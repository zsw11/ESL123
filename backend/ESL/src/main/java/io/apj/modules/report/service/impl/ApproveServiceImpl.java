package io.apj.modules.report.service.impl;

import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.lettuce.core.api.reactive.RedisGeoReactiveCommands;
import org.apache.poi.ss.usermodel.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.Constant;
import io.apj.common.utils.ExportExcelUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.common.utils.RD;
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
import io.apj.modules.report.dao.ApproveDao;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

import static io.apj.common.utils.ExportExcelUtils.copySheet;
import static io.apj.common.utils.ExportExcelUtils.exportExcel;


@Service("approveService")
public class ApproveServiceImpl extends ServiceImpl<ApproveDao, ApproveEntity> implements ApproveService {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private ReportGroupService reportGroupService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private StandardWorkService standardWorkService;
    @Autowired
    private StationTimeService stationTimeService;
    @Autowired
    private StandardTimeService standardTimeService;
    @Autowired
    private MostValueService mostValueService;
    @Autowired
    private TimeContactService timeContactService;
    @Autowired
    private ProcessListService processListService;
    @Autowired
    private CompareService compareService;
    @Autowired
    private ChangeRecordService changeRecordService;
    @Autowired
    private RevisionHistoryService revisionHistoryService;
    @Autowired
    private TotalService totalService;
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;
    @Autowired
    private WorkBookService workBookService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private ApproveHistoryService approveHistoryService;

    @Autowired
    private ReportManMachineCombinationService reportManMachineCombinationService;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("status")!=null&& params.get("status")!="","status", (String) params.get("status"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"));
        if(StringUtils.isNotEmpty((CharSequence) params.get("reportGroupId"))){
            entityWrapper.eq("report_group_id", Integer.parseInt((String) params.get("reportGroupId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
        Page<ApproveEntity> page = this.selectPage(new Query<ApproveEntity>(params).getPage(), entityWrapper);
        for(ApproveEntity entity: page.getRecords()){
            if(entity.getDeptId()!=null){
                entity.setDeptName(sysDeptService.selectById(entity.getDeptId()).getName());
            }
            if(entity.getReportGroupId()!=null){
                entity.setReportGroupName(reportGroupService.selectById(entity.getReportGroupId()).getName());
            }
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }

        }
        return new PageUtils(page);
    }

    @Override
    public List<Object> insertApprove(ApproveEntity approve) {
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
                case "report_man_machine_combination":
                    List<ReportManMachineCombinationEntity> reportManMachineCombinationEntityList = (List<ReportManMachineCombinationEntity>) reportManMachineCombinationService.selectList(new EntityWrapper<ReportManMachineCombinationEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (reportManMachineCombinationEntityList.isEmpty()) {
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
                    if(compareEntity.isEmpty()){
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "collection_most_value":
                    List<MostValueEntity> mostValueEntity = (List<MostValueEntity>) mostValueService.selectList(new EntityWrapper<MostValueEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (mostValueEntity.isEmpty()) {
                        reportItemList.add(reportEntity);
                    }
                    break;
                case "collection_revision_history":
                    List<RevisionHistoryEntity> revisionHistoryEntity = (List<RevisionHistoryEntity>) revisionHistoryService.selectList(new EntityWrapper<RevisionHistoryEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if (revisionHistoryEntity.isEmpty()) {
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
            approve.setStatus("03");
            approveService.insert(approve);
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> saveView(ApproveEntity approveEntity, Map<String, Object> data) {
        approveEntity.setNextApproverId((Integer) data.get("nextApproverId"));
//        if(data.get("nextApproverId") == null){
//            approveEntity.setStatus("01");
//        }else {
//            approveEntity.setStatus("02");
//        }
        if("01".equals((String) data.get("result"))){
        	approveEntity.setStatus(Constant.APPROVE_REPORT_STATUS_AGREE);
        }else{
        	approveEntity.setStatus(Constant.APPROVE_REPORT_STATUS_REJECT);
        }
        approveService.updateAllColumnById(approveEntity);
        //上面更新下面保存
        ApproveEntity approveEntity1 =approveService.selectById(approveEntity);
        int deptId = approveEntity1.getDeptId();
        int modelId = approveEntity1.getModelId();
        String stlst = approveEntity1.getStlst();
        ApproveHistoryEntity approveHistoryEntity = new ApproveHistoryEntity();
        approveHistoryEntity.setDeptId(deptId);
        approveHistoryEntity.setModelId(modelId);
        approveHistoryEntity.setStlst(stlst);
        approveHistoryEntity.setReportGroupId(approveEntity.getReportGroupId());
        approveHistoryEntity.setOpinion((String) data.get("opinion"));
        approveHistoryEntity.setResult((String) data.get("result"));
        approveHistoryEntity.setReportApproveId(approveEntity.getId());
        approveHistoryEntity.setNextApproverId(approveEntity.getNextApproverId());
        approveHistoryEntity.setDeptId(approveEntity.getDeptId());
        approveHistoryEntity.setCreateAt(new Date());
        approveHistoryService.insert(approveHistoryEntity);
        return RD.success(approveHistoryEntity);
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response)  throws IOException {
        List<Integer> reportIds = new ArrayList<>();
        String fileName = null;
        Boolean isReportGroup = false;
        if((Integer) params.get("reportId") != null){
            reportIds.add((Integer) params.get("reportId"));
        }
        if((Integer) params.get("reportGroupId") != null){
            isReportGroup = true;
            Integer reportGroupId = (Integer) params.get("reportGroupId");
            ReportGroupEntity reportGroupEntity = reportGroupService.selectById(reportGroupId);
            fileName = reportGroupEntity.getName() + new Date().getTime();
            List<ReportGroupReportRelaEntity> reportGroupReportRelaEntityList = reportGroupReportRelaService.selectList(new EntityWrapper<ReportGroupReportRelaEntity>().eq("report_group_id", reportGroupId));
            if (reportGroupReportRelaEntityList != null && reportGroupReportRelaEntityList.size() > 0) {
                for (ReportGroupReportRelaEntity reportGroupReportRelaEntity : reportGroupReportRelaEntityList) {
                    reportIds.add(reportGroupReportRelaEntity.getReportId());
                }
            }
        }
        if(reportIds.size() > 0){
            deleteExcel();
            for(Integer reportId : reportIds) {
                switch (reportId) {
                    case 1:
                        if(!isReportGroup){
                            fileName="workBook"+new Date().getTime();
                        }
                        workBookService.download(params, response);
                        break;
                    case 2:
                        //人机联合
                        if(!isReportGroup){
                            fileName="reportManMachineCombination"+new Date().getTime();
                        }
                        reportManMachineCombinationService.download(params, response);
                        break;
                    case 3:
                        // 工位时间报表
                        if(!isReportGroup){
                            fileName="stationTime"+new Date().getTime();
                        }
                        stationTimeService.download(params, response);
                        break;
                    case 4:
                        // Compare表
                        if(!isReportGroup){
                            fileName="compare"+new Date().getTime();
                        }
                        compareService.download(params, response);
                        break;
                    case 5:
                        // MOST Value表
                        if(!isReportGroup){
                            fileName="mostValue"+new Date().getTime();
                        }
                        mostValueService.download(params, response);
                        break;
                    case 6:
                        // Collection-Revision History表
                        if(!isReportGroup){
                            fileName="revisionHistory"+new Date().getTime();
                        }
                        revisionHistoryService.download(params, response);
                        break;
                    case 7:
                        // Total表
                        if(!isReportGroup){
                            fileName="total"+new Date().getTime();
                        }
                        totalService.download(params, response);
                        break;
                    case 8:
                        break;
                    case 9:
                        // 时间联络表
                        if(!isReportGroup){
                            fileName="timeContact"+new Date().getTime();
                        }
                        timeContactService.download(params, response);
                        break;
                    case 10:
                    	if(!isReportGroup) {
                    		fileName="assembleProcessList"+new Date().getTime();
                    	}
                    	processListService.download(params, response);
                        break;
                    case 11:
                        // 标准时间表
                        if(!isReportGroup){
                            fileName="standardTime"+new Date().getTime();
                        }
                        standardTimeService.download(params, response);
                        break;
                    case 12:
                        // 标准工数表
                        if(!isReportGroup){
                            fileName="standardWork"+new Date().getTime();
                        }
                        standardWorkService.download(params, response);
                        break;
                    case 13:
                        // 履历表
                        if(!isReportGroup){
                            fileName="changeRecord"+new Date().getTime();
                        }
                        changeRecordService.download(params, response);
                        break;
                }
            }
        }
        List<String> filePathList = getMergeExcelPath();
        if (filePathList != null && filePathList.size() > 0) {
            ExportExcelUtils.exportExcel(filePathList, response, fileName);
        }
    }

    private List<String> getMergeExcelPath(){
        String path = Constant.TEMPLATE_PATH + "template";
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        if(files.length > 0){
            List<String> filePahtList = new ArrayList<>();
            for(File file : files){
                if(file.isFile()){
                    filePahtList.add(file.toString());
                }
            }
            return filePahtList;
        }
        return null;
    }

    //清空文件夹excel或新建文件夹
    private void deleteExcel(){
        String templateStr = Constant.TEMPLATE_PATH + "template";
        File template = new File(templateStr);
        if(template.exists()){
            File[] files = template.listFiles();
            if(files.length > 0){
                for(File file : files){
                    file.delete();
                }
            }
        }else{
            template.mkdir();
        }
    }

}
