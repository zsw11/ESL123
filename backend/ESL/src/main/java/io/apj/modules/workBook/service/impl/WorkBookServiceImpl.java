package io.apj.modules.workBook.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.DataUtils;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.common.utils.RD;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.report.entity.ReportBatchEntity;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;
import io.apj.modules.report.service.ChangeRecordService;
import io.apj.modules.report.service.ProcessListService;
import io.apj.modules.report.service.ReportBatchService;
import io.apj.modules.report.service.ReportDeptRelaService;
import io.apj.modules.report.service.ReportGroupDeptRelaService;
import io.apj.modules.report.service.ReportManMachineCombinationService;
import io.apj.modules.report.service.StandardTimeService;
import io.apj.modules.report.service.StandardWorkService;
import io.apj.modules.report.service.TimeContactService;
import io.apj.modules.report.service.TotalService;
import io.apj.modules.sys.service.SysConfigService;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.workBook.dao.WorkBookDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.WorkOperationsService;

@Service("workBookService")
public class WorkBookServiceImpl extends ServiceImpl<WorkBookDao, WorkBookEntity> implements WorkBookService {
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private WorkstationService workstationService;
    @Autowired
    private WorkBookService workBookService;
    @Autowired
    private WorkOperationsService workOperationService;
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
    private WorkOperationsService workOperationsService;

    @Autowired
    private ReportManMachineCombinationService reportManMachineCombinationService;
    @Autowired
    private ReportDeptRelaService reportDeptRelaService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;
    @Autowired
    private ReportGroupDeptRelaService reportGroupDeptRelaService;
    @Autowired
    private ReportBatchService reportBatchService;

    @Override
    @DataFilter(subDept = true, user = true, deptId = "dept_id")
    public PageUtils queryPage(Map<String, Object> params) throws ParseException {
        EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at", false)
                .like(params.get("keyWord") != null && params.get("keyWord") != "", "destinations",
                        (String) params.get("keyWord"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("workName") != null && params.get("workName") != "", "work_name",
                        (String) params.get("workName"))
                .like(params.get("remark") != null && params.get("remark") != "", "remark",
                        (String) params.get("remark"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations",
                        (String) params.get("destinations"))
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));

        Map<String, Object> map = (Map) JSON.parse((String) params.get("makedAt"));
        if (map != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date start = format.parse((String) map.get("createAtStart"));
            Date stop = format.parse((String) map.get("createAtStop"));
            entityWrapper.between("maked_at", start, stop);
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
            entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("workstationId"))) {
            entityWrapper.eq("workstation_id", Integer.parseInt((String) params.get("workstationId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("makerId"))) {
            entityWrapper.eq("maker_id", Integer.parseInt((String) params.get("makerId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
            entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
        }
        Page<WorkBookEntity> page = this.selectPage(new Query<WorkBookEntity>(params).getPage(), entityWrapper);
        for (WorkBookEntity entity : page.getRecords()) {
            entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
            entity.setMakerName(staffService.selectNameByUserId(entity.getCreateBy()));
            if (entity.getDeptId() != null) {
                entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
            }
            if (entity.getModelId() != null) {
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if (entity.getPhaseId() != null) {
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
            if (entity.getWorkstationId() != null) {
                entity.setWorkstationName(workstationService.selectById(entity.getWorkstationId()).getName());
            }
        }
        return new PageUtils(page);

    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateWorkBook(Map<String, Object> workBook) {
        // delete
        int[] ids = (int[]) workBook.get("delete");
        workBookService.deleteBatchIds(Collections.singleton(ids));
        // updata
        WorkBookEntity[] workBookEntities = (WorkBookEntity[]) workBook.get("update");
        List<WorkBookEntity> bookList = new ArrayList<>(workBookEntities.length);
        Collections.addAll(bookList, workBookEntities);
        workBookService.updateBatchById(bookList);
        // new
        WorkBookEntity[] workBookEntities1 = (WorkBookEntity[]) workBook.get("new");
        List<WorkBookEntity> bookListNew = new ArrayList<>(workBookEntities1.length);
        Collections.addAll(bookListNew, workBookEntities1);
        workBookService.insertBatch(bookListNew);

        return RD.ok(workBookEntities1);
    }

    @Override
    @Transactional
    public List<Integer> updateOperation(Map<String, Object> params) {
        // 新增的手顺ID列表
        List<Integer> newIDList = new ArrayList<>();
        String type = (String) params.get("type");
        Integer seqNumber = (Integer) params.get("seqNumber") + 1;
        Integer workBookId = (Integer) params.get("workBookId");
        EntityWrapper<WorkOperationsEntity> workOperationsWrapper = new EntityWrapper<>();
        if (type.equals("delete")) {
            ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("list");
            workOperationService.deleteBatchIds(deleteList);
            Integer deleteNumber = deleteList.size();
            workOperationsWrapper.eq("work_book_id", workBookId).isNull("delete_at").ge("seq_number", seqNumber);
            List<WorkOperationsEntity> workOperationsEntityList = workOperationService
                    .selectList(workOperationsWrapper);
            for (WorkOperationsEntity item : workOperationsEntityList) {
                item.setSeqNumber(item.getSeqNumber() - deleteNumber);
            }
            workOperationService.updateBatchById(workOperationsEntityList);
        } else {
            List<WorkOperationsEntity> workOperationsList = new ArrayList<>();
            List<Map<String, Object>> operateList = (List<Map<String, Object>>) params.get("list");
            for (int i = 0; i < operateList.size(); i++) {
                WorkOperationsEntity workOperationsEntity = new WorkOperationsEntity();
                DataUtils.transMap2Bean2(operateList.get(i), workOperationsEntity);
                workOperationsList.add(workOperationsEntity);
            }
            if (type.equals("update")) {
                workOperationService.updateBatchById(workOperationsList);
            } else {
                // 更新seqNumber大于所要新增的最大值的所有记录
                workOperationsWrapper.eq("work_book_id", workBookId).isNull("delete_at").ge("seq_number", seqNumber);
                List<WorkOperationsEntity> workOperationsEntityList = workOperationService
                        .selectList(workOperationsWrapper);
                Integer operateNumber = operateList.size();
                for (WorkOperationsEntity item : workOperationsEntityList) {
                    item.setSeqNumber(item.getSeqNumber() + operateNumber);
                }
                workOperationService.insertOrUpdateBatch(workOperationsEntityList);
                // 批量插入新增的手顺
                workOperationService.insertBatch(workOperationsList);
                for (WorkOperationsEntity item : workOperationsList) {
                    newIDList.add(item.getId());
                }
            }
        }

        return newIDList;
    }

    @Override
    public void createReports(Map<String, Object> params) {
        Integer deptId = (Integer)params.get("deptId");
        Integer userId =Integer.parseInt(String.valueOf(params.get("userId")));
        List<ReportGroupDeptRelaEntity> reportGroupDeptRelaEntityList = reportGroupDeptRelaService.selectList(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("dept_id", deptId));
        if(reportGroupDeptRelaEntityList != null && reportGroupDeptRelaEntityList.size() > 0){
            HashSet<Integer> reportSet = new HashSet<>();
            for(ReportGroupDeptRelaEntity reportGroupDeptRelaEntity : reportGroupDeptRelaEntityList){
                Integer reportGroupId = reportGroupDeptRelaEntity.getReportGroupId();
                List<ReportGroupReportRelaEntity> reportGroupReportRelaEntities = reportGroupReportRelaService.selectList(new EntityWrapper<ReportGroupReportRelaEntity>().eq("report_group_id", reportGroupId));
                if(reportGroupReportRelaEntities != null && reportGroupReportRelaEntities.size() > 0){
                    reportGroupReportRelaEntities.forEach(i->{
                        reportSet.add(i.getReportId());
                    });
                }
            }
            if(reportSet != null && reportSet.size() > 0){
                List<Integer> workBookIds = new ArrayList<>();
                List<Integer> workBookIdsGet = (List<Integer>) params.get("workBookIds");
                if(workBookIdsGet != null && workBookIdsGet.size() > 0){
                    workBookIds.addAll(workBookIdsGet);
                }else{
                    Map<String,Object> workBook = (Map<String, Object>) params.get("workBook");
                    EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
                    entityWrapper.eq("stlst", workBook.get("stlst")).eq("version_number", workBook.get("versionNumber"))
                            .eq("destinations", workBook.get("destinations")).eq("model_id", workBook.get("modelId"))
                            .eq("phase_id", workBook.get("phaseId"));
                    List<WorkBookEntity> workBookEntityList = workBookService.selectList(entityWrapper);
                    if(workBookEntityList != null && workBookEntityList.size() > 0){
                        for(WorkBookEntity workBookEntity : workBookEntityList){
                            workBookIds.add(workBookEntity.getId());
                        }
                    }
                }

                if(workBookIds.size()>0&&reportSet.size()>0) {
                    for(Integer reportId : reportSet){
                        switch (reportId) {
                            case 1:
                                break;
                            case 2:
                                // 人机联合表
                                 reportManMachineCombinationService.generateReportData(workBookIds,reportId);
                                break;
                            case 3:
                                // 工位时间报表
                                stationTimeService.generateReportData(workBookIds,reportId);
                                break;
                            case 4:
                                // Compare表
                                compareService.generateReportData(workBookIds,reportId);
                                break;
                            case 5:
                                // MOST Value表
                                mostValueService.generateReportData(workBookIds, reportId);
                                break;
                            case 6:
                                // Collection-Revision History表
                                revisionHistoryService.generateReportData(workBookIds, reportId);
                                break;
                            case 7:
                                // Total表
                                totalService.generateReportData(workBookIds, reportId);
                                break;
                            case 8:
                                break;
                            case 9:
                                // 时间联络表
                                timeContactService.generateReportData(workBookIds, reportId);
                                break;
                            case 10:
                                // Process List表
                            	processListService.generateReportData(workBookIds,reportId,userId);
                                break;
                            case 11:
                                // 标准时间表
                                standardTimeService.generateReportData(workBookIds, reportId);
                                break;
                            case 12:
                                // 标准工数表
                                standardWorkService.generateReportData(workBookIds, reportId);
                                break;
                            case 13:
                                // 履历表
                                changeRecordService.generateReportData(workBookIds, reportId);
                                break;
                        }
                    }
                    for(Integer workBookId : workBookIds){
                        WorkBookEntity workBookEntity = selectById(workBookId);
                        if(workBookEntity != null) {
                            String stlst = workBookEntity.getStlst();
                            String versionNumber = workBookEntity.getVersionNumber();
                            String destinations = workBookEntity.getDestinations();
                            Integer modelId = workBookEntity.getModelId();
                            Integer phaseId = workBookEntity.getPhaseId();
                            EntityWrapper<ReportBatchEntity> wrapper = new EntityWrapper<>();
                            wrapper.eq("stlst", stlst).eq("version_number", versionNumber).eq("destinations", destinations).
                                    eq("model_id", modelId).eq("phase_id", phaseId);
                            ReportBatchEntity reportBatchEntity = reportBatchService.selectOne(wrapper);
                            if (reportBatchEntity == null) {
                                ReportBatchEntity reportBatchEntityCreate = new ReportBatchEntity();
                                reportBatchEntityCreate.setModelId(modelId);
                                reportBatchEntityCreate.setDestinations(destinations);
                                reportBatchEntityCreate.setPhaseId(phaseId);
                                reportBatchEntityCreate.setStlst(stlst);
                                reportBatchEntityCreate.setVersionNumber(versionNumber);
                                reportBatchEntityCreate.setCreateAt(new Date());
                                reportBatchEntityCreate.setCreateBy(userId);
                                reportBatchService.insert(reportBatchEntityCreate);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public WorkBookEntity getLastVersion(Integer modelId, String stlst, Integer phaseId, String destinations, String versionNumber) {
        EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.ne("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
        entityWrapper.orderBy("create_at", false);
        List<WorkBookEntity> list = selectList(entityWrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public WorkBookEntity detailWithOperations(Integer id) {
        WorkBookEntity workBook = this.selectById(id);
        workBook.setMakedAt(new Date());
        workBook.setModelEntity(modelService.selectById(workBook.getModelId()));
        workBook.setSysDeptEntity(deptService.selectById(workBook.getDeptId()));
        workBook.setPhaseEntity(phaseService.selectById(workBook.getPhaseId()));
        workBook.setWorkstationEntity(workstationService.selectById(workBook.getWorkstationId()));
        EntityWrapper<WorkOperationsEntity> workOperationsWrapper = new EntityWrapper<>();
        workOperationsWrapper.eq("work_book_id", id).isNull("delete_at");
        List<WorkOperationsEntity> workOperationsList = workOperationService.selectList(workOperationsWrapper);
        workBook.setWorkOperationsList(workOperationsList);
        EntityWrapper<WorkBookEntity> workBookWrapper = new EntityWrapper<>();
        workBookWrapper.isNull("delete_at").eq("stlst", workBook.getStlst()).eq("model_id", workBook.getModelId())
                .eq("destinations", workBook.getDestinations()).eq("version_number", workBook.getVersionNumber())
                .eq("phase_id", workBook.getPhaseId());
        List<WorkBookEntity> otherWorkBookEntities = this.selectList(workBookWrapper);
        workBook.setOtherWorkBookEnties(otherWorkBookEntities);
        return workBook;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateAll(Map<String, Object> params) {
        // 更新主表
        WorkBookEntity workBookEntity = new WorkBookEntity();
        DataUtils.transMap2Bean2((Map<String, Object>) params.get("workBook"), workBookEntity);
        Integer workBookId = workBookEntity.getId();
        workBookEntity = workBookService.selectById(workBookId);
        Integer lockById = workBookEntity.getLockBy();
        if(lockById != params.get("UserId")){
            return RD.FORBIDDEN("LOCKED","已被别人锁定，无法保存");
        }
        // 删除原有子表
        workOperationService.deletebyWrapper(
                new EntityWrapper<WorkOperationsEntity>().eq("work_book_id", workBookId).isNull("delete_at"));

        // 遍历子表数组，批量插入
        List<WorkOperationsEntity> workOperationsList = new ArrayList<>();
        List<Map<String, Object>> workOperationsMapList = (List<Map<String, Object>>) params.get("workOperations");
        JSONArray alterInfoJson = null;
        Float totalTimeValue = 0.0f;
        Double totalTmu = 0.00;
        Double totalSecondVonvert = 0.00;
        Integer totalRemark1 = 0;
        if(workOperationsMapList != null && workOperationsMapList.size() > 0) {
            for (int i = 0; i < workOperationsMapList.size(); i++) {
                WorkOperationsEntity workOperations = new WorkOperationsEntity();
                Object frequencyObj = workOperationsMapList.get(i).get("frequency");
                if (frequencyObj != "" && frequencyObj != null) {
                    workOperationsMapList.get(i).put("frequency", Float.parseFloat(frequencyObj.toString()));
                    alterInfoJson = new JSONArray(Collections.singletonList(workOperationsMapList.get(i).get("alterInfo")));
                    workOperationsMapList.get(i).put("alterInfo", alterInfoJson.toString());
                }
                workOperations = JSON.parseObject(JSON.toJSONString(workOperationsMapList.get(i)), WorkOperationsEntity.class);
//			DataUtils.transMap2Bean2(workOperationsMapList.get(i), workOperations);
                Map<String, Integer> map = new HashMap<>();
                Integer totalPositive = 0;
                map.put("totalPositive", totalPositive);
                Integer totalNegative = 0;
                map.put("totalNegative", totalNegative);
                Integer a0 = workOperations.getA0();
                map.put("data", a0);
                map = dealData(map);
                Integer b0 = workOperations.getB0();
                map.put("data", b0);
                map = dealData(map);
                Integer g0 = workOperations.getG0();
                map.put("data", g0);
                map = dealData(map);
                Integer a1 = workOperations.getA1();
                map.put("data", a1);
                map = dealData(map);
                Integer b1 = workOperations.getB1();
                map.put("data", b1);
                map = dealData(map);
                Integer p0 = workOperations.getP0();
                map.put("data", p0);
                map = dealData(map);
                Integer m0 = workOperations.getM0();
                map.put("data", m0);
                map = dealData(map);
                Integer x0 = workOperations.getX0();
                map.put("data", x0);
                map = dealData(map);
                Integer i0 = workOperations.getI0();
                map.put("data", i0);
                map = dealData(map);
                Integer a2 = workOperations.getA2();
                map.put("data", a2);
                map = dealData(map);
                Integer b2 = workOperations.getB2();
                map.put("data", b2);
                map = dealData(map);
                Integer p1 = workOperations.getP1();
                map.put("data", p1);
                map = dealData(map);
                Integer a3 = workOperations.getA3();
                map.put("data", a3);
                map = dealData(map);
                Integer a4 = workOperations.getA4();
                map.put("data", a4);
                map = dealData(map);
                Integer b3 = workOperations.getB3();
                map.put("data", b3);
                map = dealData(map);
                Integer p2 = workOperations.getP2();
                map.put("data", p2);
                map = dealData(map);
                Integer a5 = workOperations.getA5();
                map.put("data", a5);
                map = dealData(map);
                Float frequency = workOperations.getFrequency();
                frequency = frequency == null ? 0 : frequency;
                String tool = workOperations.getTool();
                Integer toolInteger = Integer.valueOf(tool.substring(1, 2));
                Float frequency2 = frequency == 0 ? 1 : frequency;
                Float timeValue = (map.get("totalPositive") + map.get("totalNegative") * frequency) * 6 + toolInteger * frequency2 * 6;
                totalTimeValue += timeValue;
                workOperations.setTimeValue(new BigDecimal(timeValue));
                Double tmu = timeValue / 6.00 * 10;
                totalTmu += tmu;
                workOperations.setTmu(new BigDecimal(tmu));
                Double secondConvert = tmu * 0.036;
                totalSecondVonvert += secondConvert;
                workOperations.setSecondConvert(new BigDecimal(secondConvert));
                Integer remark1 = workOperations.getRemark1();
                if (remark1 != null) {
                    Double calculate = Math.ceil((remark1 / 0.36 * 6) / Math.pow(10.00, 1.00)) * Math.pow(10.00, 1.00);
                    remark1 = calculate.intValue();
                    workOperations.setRemark1(remark1);
                    totalRemark1 += remark1;
                }
                workOperations.setWorkBookId(workBookId);
                workOperationsList.add(workOperations);
            }
            workBookEntity.setTimeValue(new BigDecimal(totalTimeValue));
            workBookEntity.setTmu(new BigDecimal(totalTmu));
            workBookEntity.setSecondConvert(new BigDecimal(totalSecondVonvert));
            workBookEntity.setRemark1(new BigDecimal(totalRemark1));
            updateById(workBookEntity);
            if (workOperationsList.size() > 0) {
                workOperationService.insertBatch(workOperationsList);
            }
        }
        return RD.success(workBookEntity);
    }

    @Override
    @Transactional
    public void deleteBookByIds(Integer[] ids) {
        workBookService.deleteByIds(Arrays.asList(ids));
        workOperationService.deletebyWrapper(new EntityWrapper<WorkOperationsEntity>().in("work_book_id", ids));

    }

    @Override
    @Transactional
    public List<ReportEntity> deptReports(Integer id) {
        List<ReportEntity> reportEntityList = new LinkedList<>();
        List<ReportDeptRelaEntity> reportDeptRelaEntityList = reportDeptRelaService.selectList(new EntityWrapper<ReportDeptRelaEntity>().eq("deptId", id));
        reportDeptRelaEntityList.forEach(item -> {
            reportEntityList.add(reportService.selectById(item.getReportId()));
        });
        return reportEntityList;
    }

    @Override
    @Transactional
    public void selectLock() {
        EntityWrapper<WorkBookEntity> ew = new EntityWrapper<>();
        ew.isNull("delete_at").isNotNull("lock_at").isNotNull("lock_by");
        List<WorkBookEntity> workBookEntityList = workBookService.selectList(ew);
        for (WorkBookEntity item : workBookEntityList) {
            Long lockTime = item.getLockAt().getTime();
            Long nowTime = new Date().getTime();
            Long time = lockTime + Long.parseLong(sysConfigService.getValue("LockTime"));
            if (nowTime > time) {
                item.setLockAt(null);
                item.setLockBy(null);
                workBookService.updateById(item);
            }
        }

    }

    @Override
    public void deleteByIds(Collection<? extends Serializable> ids) {
        List<WorkBookEntity> workBookEntityList = this.selectBatchIds(ids);
        for (WorkBookEntity item : workBookEntityList) {
            item.setDeleteAt(new Date());
        }
        if (workBookEntityList.size() > 0) {
            this.updateAllColumnBatchById(workBookEntityList);
        }

    }

    @Override
    @Transactional
    public WorkBookEntity copyWorkBook(WorkBookEntity workBook, Integer workBookId) {
        workBookService.insert(workBook);
        int newId = workBook.getId();
        List<WorkOperationsEntity> workOperationsEntityList = workOperationsService
                .selectList(new EntityWrapper<WorkOperationsEntity>().eq("work_book_id", workBookId));
        for (WorkOperationsEntity item : workOperationsEntityList) {
            item.setWorkBookId(newId);
        }
        if (!workOperationsEntityList.isEmpty()) {
            workOperationService.insertBatch(workOperationsEntityList);
        }
        return workBook;
    }

    @Override
    @Transactional
    public void deleteByWrapper(Wrapper wrapper) {
        List<WorkBookEntity> workBookList = this.selectList(wrapper);
        for (WorkBookEntity item : workBookList) {
            item.setDeleteAt(new Date());
        }
        this.updateAllColumnBatchById(workBookList);
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        Integer phaseId = (Integer) params.get("phaseId");
        Integer modelId = (Integer) params.get("modelId");
        String stlst = params.get("stlst").toString();
        String destinations = params.get("destinations").toString();
        String versionNumber = params.get("versionNumber").toString();
        EntityWrapper<WorkBookEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
        List<WorkBookEntity> workBookEntityList = selectList(wrapper);
        if(workBookEntityList != null && workBookEntityList.size() > 0){
            for(WorkBookEntity workBookEntity : workBookEntityList){
                downloadWorkbook(workBookEntity);
            }
        }
    }

    private void downloadWorkbook(WorkBookEntity workBookEntity) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
        String workName = workBookEntity.getWorkName();
        params.put("workName", workName);

        WorkstationEntity workstation = workstationService.selectById(workBookEntity.getWorkstationId());
        if(workstation != null) {
            String workstationName = workstation.getName();
            params.put("workstationName", workstationName);
        }

        EntityWrapper<WorkOperationsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("work_book_id", workBookEntity.getId());
        List<WorkOperationsEntity> workOperationsEntityList = workOperationService.selectList(wrapper);
        if(workOperationsEntityList != null && workOperationsEntityList.size() > 0) {
            generateTotalData(workOperationsEntityList, params);
        }

        String templateFileName = Constant.TEMPLATE_PATH + "work_operations.xls";
        String exportFileName = Constant.TEMPLATE_PATH + "template\\" + workName + ".xls";
        File historyExcel = new File(exportFileName);
        if (historyExcel.exists()) {
            historyExcel.delete();
        }

        ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(params, writeSheet);
        excelWriter.fill(workOperationsEntityList, fillConfig, writeSheet);
        excelWriter.finish();
    }

    private void generateTotalData(List<WorkOperationsEntity> workOperationsEntityList, Map<String, Object> params) {
        BigDecimal timeValueTotal = BigDecimal.ZERO;
        BigDecimal tmuTotal = BigDecimal.ZERO;
        BigDecimal secondConvertTotal = BigDecimal.ZERO;
        Integer remark1Total = 0;

        for (WorkOperationsEntity workOperationsEntity : workOperationsEntityList) {
            BigDecimal timeValue = workOperationsEntity.getTimeValue();
            BigDecimal tmu = workOperationsEntity.getTmu();
            BigDecimal secondConvert = workOperationsEntity.getSecondConvert();
            Integer remark1 = workOperationsEntity.getRemark1();

            if(timeValue!=null) {
                timeValueTotal = timeValueTotal.add(timeValue);
            }
            if(tmu!=null) {
                tmuTotal = tmuTotal.add(tmu);
            }
            if(secondConvert!=null) {
                secondConvertTotal = secondConvertTotal.add(secondConvert);
            }
            if(remark1 != null){
                remark1Total += remark1;
            }
        }
        params.put("timeValueTotal", timeValueTotal);
        params.put("tmuTotal", tmuTotal);
        params.put("secondConvertTotal", secondConvertTotal);
        params.put("remark1Total", remark1Total);
    }

    @Override
    public List<WorkBookEntity> filterUniquePhaseAndModelAndStlstOfWorkBooks(List<WorkBookEntity> workBooks) {
        Map<String, WorkBookEntity> map = new HashMap<>();
        for (WorkBookEntity entity : workBooks) {
            Integer phaseId = entity.getPhaseId();
            Integer modelId = entity.getModelId();
            String stlst = entity.getStlst();
            String destinations = entity.getDestinations();
            String versionNumber = entity.getVersionNumber();
            String key = phaseId + modelId + stlst + destinations + versionNumber;
            if (map.get(key) != null) {
                continue;
            }
            map.put(key, entity);
        }
        return new ArrayList<WorkBookEntity>(map.values());
    }

    @Override
    public List<Integer> filterWorkBookIdsByPhaseAndModelAndStlst(List<WorkBookEntity> workBooks, Integer modelId,
                                                                  String stlst, Integer phaseId, String destinations, String versionNumber) {
        String filter = modelId + stlst + phaseId + destinations + versionNumber;
        List<Integer> result = new ArrayList<>();
        for (WorkBookEntity entity : workBooks) {
            String target = entity.getModelId() + entity.getStlst() + entity.getPhaseId() + entity.getDestinations() + entity.getVersionNumber();
            if (org.apache.commons.lang3.StringUtils.equals(filter, target)) {
                result.add(entity.getId());
            }
        }
        return result;
    }


    @Override
    public Map<String , Object> dealData(Integer workBookId) {
        EntityWrapper<WorkOperationsEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("work_book_id" , workBookId);
        List<WorkOperationsEntity> workOperationsEntityList = workOperationService.selectList(wrapper);
        if(workOperationsEntityList != null && workOperationsEntityList.size() > 0){
            Map<String , Object> map = new HashMap<>();
            BigDecimal totalActuallyTimeValue = new BigDecimal(0);
            BigDecimal totalActuallyTmu = new BigDecimal(0);
            BigDecimal totalActuallySecondConvert = new BigDecimal(0);
            Integer totalRemark1 = 0;
            for(WorkOperationsEntity workOperationsEntity : workOperationsEntityList){
                BigDecimal timeValue = workOperationsEntity.getTimeValue();
                totalActuallyTimeValue.add(timeValue);
                BigDecimal tmu = workOperationsEntity.getTmu();
                totalActuallyTmu.add(tmu);
                BigDecimal secondConvert = workOperationsEntity.getSecondConvert();
                totalActuallySecondConvert.add(secondConvert);
                Integer remark1 = workOperationsEntity.getRemark1();
                remark1 = remark1 == null ? 0 : remark1;
                totalRemark1 += remark1;
            }
            map.put("totalActuallyTimeValue" , totalActuallyTimeValue);
            map.put("totalActuallyTmu" , totalActuallyTmu);
            map.put("totalActuallySecondConvert" , totalActuallySecondConvert);
            map.put("totalRemark1" , totalRemark1);
            Double averageCapacity = Double.parseDouble(sysConfigService.getValue("AverageCapacity"));
            BigDecimal totalTimeValue = totalActuallyTimeValue.multiply(new BigDecimal(averageCapacity));
            BigDecimal totalTmu = totalActuallyTmu.multiply(new BigDecimal(averageCapacity));
            BigDecimal totalSecondConvert =  totalActuallySecondConvert.multiply(new BigDecimal(averageCapacity));
            map.put("totalTimeValue" , totalTimeValue);
            map.put("totalTmu" , totalTmu);
            map.put("totalSecondConvert" , totalSecondConvert);
            return map;
        }
        return null;
    }

    private Map<String, Integer> dealData(Map<String, Integer> map) {
        Integer data = map.get("data");
        Integer totalNegative = map.get("totalNegative");
        Integer totalPositive = map.get("totalPositive");
        if(data != null){
            if(data < 0){
                if(data == -999){
                    data = 0;
                }
                totalNegative += data;
                map.put("totalNegative", totalNegative);
            }else{
                totalPositive += data;
                map.put("totalPositive", totalPositive);
            }
        }
        return map;
    }

}
