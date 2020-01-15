package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.common.utils.RD;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.dao.ReportDao;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service("reportService")
public class ReportServiceImpl extends ServiceImpl<ReportDao, ReportEntity> implements ReportService {
    @Autowired
    private ApproveService approveService;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;
    @Autowired
    private ReportService reportService;
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
    private StandardWorkService standardWorkService;
    @Autowired
    private ChangeRecordService changeRecordService;
    @Autowired
    private StandardTimeService standardTimeService;
    @Autowired
    private ReportGroupService reportGroupService;
    @Autowired
    private StaffService staffService;

    @Autowired
    private ReportDeptRelaService reportDeptRelaService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ReportEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("remark") != null && params.get("remark") != "","remark", (String) params.get("remark"))
                .like(params.get("formCode") != null && params.get("formCode") != "", "form_code",
                        (String) params.get("formCode"));
//                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"));
        if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
            entityWrapper.andNew(
                    "UPPER(pinyin) like '%" + ((String) params.get("name")).toUpperCase() + "%' " + "or UPPER(name) like '%" + ((String) params.get("name")).toUpperCase() + "%'");
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))) {
            String name = (String) params.get("keyWord");
            name = name.replace(",", "");
            entityWrapper.andNew("UPPER(name)  like '%" + name.toUpperCase() + "%'" + " or UPPER(pinyin)  like '%" + name.toUpperCase() + "%'");

        }

        Page<ReportEntity> page = this.selectPage(new Query<ReportEntity>(params).getPage(), entityWrapper);
//        for(ReportEntity entity: page.getRecords()){
//            entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
//            entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
//        }

        return new PageUtils(page);
    }

    @Override
    public Integer selectByNameTest(String name) {
        return reportDao.selectByNameTest(name);
    }

    @Override
    public List<ReportEntity> selectApproveList(Integer id) {
        ApproveEntity approveEntity = approveService.selectById(id);
        int pid = approveEntity.getPhaseId();
        int mid = approveEntity.getModelId();
        String stlst = approveEntity.getStlst();
        int reportgid = approveEntity.getReportGroupId();
        List<ReportGroupReportRelaEntity> entityList = reportGroupReportRelaService.selectList(new EntityWrapper<ReportGroupReportRelaEntity>().eq("report_group_id", reportgid));
        List<ReportEntity> reportEntities = new ArrayList<>();
        for(ReportGroupReportRelaEntity item : entityList){
            ReportEntity reportEntity= reportService.selectById(item.getReportId());
            String reportName = reportEntity.getEname();
            switch (reportName){
                case "report_total":
                    List<TotalEntity> total = (List<TotalEntity>) totalService.selectList(new EntityWrapper<TotalEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!total.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "work_book":
                    List<WorkBookEntity> workBookEntity = (List<WorkBookEntity>) workBookService.selectList(new EntityWrapper<WorkBookEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!workBookEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "collection_station_time":
                    List<StationTimeEntity> stationTimeEntity = (List<StationTimeEntity>) stationTimeService.selectList(new EntityWrapper<StationTimeEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!stationTimeEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "collection_compare":
                    List<CompareEntity> compareEntity = (List<CompareEntity>) compareService.selectList(new EntityWrapper<CompareEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    reportEntities.add(reportEntity);
                    break;
                case "collection_most_value":
                    List<MostValueEntity> mostValueEntity = (List<MostValueEntity>) mostValueService.selectList(new EntityWrapper<MostValueEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!mostValueEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "collection_revision_history":
                    List<RevisionHistoryEntity> revisionHistoryEntity = (List<RevisionHistoryEntity>) revisionHistoryService.selectList(new EntityWrapper<RevisionHistoryEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!revisionHistoryEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "report_time_contact":
                    List<TimeContactEntity> timeContactEntity = (List<TimeContactEntity>) timeContactService.selectList(new EntityWrapper<TimeContactEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!timeContactEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "report_standard_time":
                    List<StandardTimeEntity> standardTimeEntity = (List<StandardTimeEntity>) standardTimeService.selectList(new EntityWrapper<StandardTimeEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!standardTimeEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "report_standard_work":
                    List<StandardWorkEntity> standardWorkEntity = (List<StandardWorkEntity>) standardWorkService.selectList(new EntityWrapper<StandardWorkEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!standardWorkEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
                case "report_change_record":
                    List<ChangeRecordEntity> changeRecordEntity = (List<ChangeRecordEntity>) changeRecordService.selectList(new EntityWrapper<ChangeRecordEntity>().eq("model_id", mid).eq("phase_id", pid).eq("stlst", stlst));
                    if(!changeRecordEntity.isEmpty()){
                        reportEntities.add(reportEntity);
                    }
                    break;
            }
        }
        return reportEntities;
    }

    @Override
    public List<ReportGroupEntity> selectReportGroup(Map<String, Object> data) {
        String reportName = (String) data.get("name");
        int id =0;
        if(reportName!=null&&reportName!= ""){
            EntityWrapper<ReportEntity> ew = new EntityWrapper<ReportEntity>();
            ew.eq("name",reportName);
            ReportEntity reportEntity = reportService.selectOne(ew);
            id = reportService.selectByNameTest(reportName);
            reportEntity.getId();
        }
        List<ReportGroupReportRelaEntity>  reportGroupReportRelaEntities = reportGroupReportRelaService.selectList(new EntityWrapper<ReportGroupReportRelaEntity>().eq("report_id", id));
        int idG;
        ReportGroupEntity reportGroupEntity;
        List<ReportGroupEntity> reportGroupEntities = new ArrayList<>();
        for(ReportGroupReportRelaEntity item : reportGroupReportRelaEntities){
            idG = item.getReportGroupId();
            int modelId = (int) data.get("modelId");
            int phaseId = (int) data.get("phaseId");
            String stlst = (String) data.get("stlst");
            String destinations =  (String) data.get("destinations");
    		String versionNumber = (String) data.get("versionNumber");
            // 报表组过滤
            List<ApproveEntity> approveEntityList = approveService.selectList(new EntityWrapper<ApproveEntity>().eq("model_id", modelId).eq("phase_id", phaseId).eq("stlst", stlst).eq("report_group_id", idG)
            		.eq("destinations", destinations).eq("version_number", versionNumber).ne("status", Constant.APPROVE_REPORT_STATUS_REJECT));
            List<Integer> reportGIds = new ArrayList<>();
            for(ApproveEntity approveEntity : approveEntityList ){
                reportGIds = Collections.singletonList(approveEntity.getReportGroupId());
            }
            if(!reportGIds.contains(idG)){
                reportGroupEntity = reportGroupService.selectById(idG);
                if(reportGroupEntity!=null){
                    reportGroupEntities.add(reportGroupEntity);
                }
            }

        }
        return reportGroupEntities;
    }

    @Override
    public void deleteList(List<ReportEntity> reportEntityList) {
        for(ReportEntity item : reportEntityList){
            item.setDeleteAt(new Date());
        }
        if(reportEntityList.size()>0){
            this.updateAllColumnBatchById(reportEntityList);
        }

    }

    @Override
    public void deleteByIds(Collection<? extends Serializable> ids) {
        List<ReportEntity> reportEntityList = this.selectBatchIds(ids);
        for(ReportEntity item : reportEntityList){
            item.setDeleteAt(new Date());
        }
        if(reportEntityList.size()>0){
            this.updateAllColumnBatchById(reportEntityList);
        }
    }

    @Override
    public void deleteByWrapper(Wrapper<ReportEntity> wrapper) {
        List<ReportEntity> reportEntityList = this.selectList(wrapper);
        for(ReportEntity item: reportEntityList){
            item.setDeleteAt(new Date());
        }
        if(reportEntityList.size()>0){
            this.updateAllColumnBatchById(reportEntityList);
        }
    }

    @Override
    public void updatePinAndDataById(ReportEntity reportEntity) {
        reportEntity.setPinyin(PinyinUtil.getPinYin(reportEntity.getName()));
        reportEntity.setUpdateAt(new Date());
        this.updateById(reportEntity);
    }

    @Override
    public ResponseEntity<Object> listByDeptId(Integer deptId) {
        EntityWrapper<ReportDeptRelaEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("dept_id",deptId);
        List<ReportDeptRelaEntity> reportDeptRelaEntityList = reportDeptRelaService.selectList(wrapper);
        if(reportDeptRelaEntityList != null && reportDeptRelaEntityList.size() > 0){
            List<ReportEntity> reportEntityList = new ArrayList<>();
            for(ReportDeptRelaEntity reportDeptRelaEntity : reportDeptRelaEntityList){
                Integer reportId = reportDeptRelaEntity.getReportId();
                ReportEntity reportEntity = selectById(reportId);
                if(reportEntity != null){
                    reportEntityList.add(reportEntity);
                }
            }
            return RD.listReturn(reportEntityList,reportEntityList.size());
        }
        return null;
    }

}