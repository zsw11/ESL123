package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lowagie.text.pdf.PRAcroForm;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportDao;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ReportService;

@Service("reportService")
public class ReportServiceImpl extends ServiceImpl<ReportDao, ReportEntity> implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private ApproveService approveService;
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


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ReportEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("formCode") != null && params.get("formCode") != "", "form_code",
                        (String) params.get("formCode"))
                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"));
        if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
            entityWrapper.andNew(
                    "pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))) {
            String name = (String) params.get("keyWord");
            name = name.replace(",", "");
            entityWrapper.andNew("name  like '%" + name + "%'" + " or code  like '%" + name + "%'"
                    + " or pinyin  like '%" + name + "%'");

        }

        Page<ReportEntity> page = this.selectPage(new Query<ReportEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

    @Override
    public Integer selectByName(String name) {
        return reportDao.selectByName(name);
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

}