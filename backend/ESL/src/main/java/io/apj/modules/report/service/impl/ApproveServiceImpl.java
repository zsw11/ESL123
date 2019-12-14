package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.modules.report.service.*;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ApproveDao;
import io.apj.modules.report.entity.ApproveEntity;

import javax.servlet.http.HttpServletResponse;


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
    private CompareService compareService;
    @Autowired
    private ChangeRecordService changeRecordService;
    @Autowired
    private RevisionHistoryService revisionHistoryService;
    @Autowired
    private TotalService totalService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("status")!=null&& params.get("status")!="","status", (String) params.get("status"));
        if(StringUtils.isNotEmpty((CharSequence) params.get("reportGroupId"))){
            entityWrapper.eq("report_group_id", Integer.parseInt((String) params.get("reportGroupId")));
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
    public void download(Map<String, Object> params, HttpServletResponse response)  throws IOException {
        Integer reportId = (Integer)params.get("reportId");
        switch (reportId) {
            case 1:
                break;
            case 2:
                break;
            case 3:

                stationTimeService.download(params, response);
                break;
            case 4:
                compareService.download(params, response);
                break;
            case 5:
                mostValueService.download(params, response);
                break;
            case 6:
                // Collection-Revision History表
                revisionHistoryService.download(params, response);
                break;
            case 7:
                totalService.download(params, response);
                break;
            case 8:
                break;
            case 9:
                timeContactService.download(params, response);
                break;
            case 10:
                break;
            case 11:
                standardTimeService.download(params, response);
                break;
            case 12:
                // 标准工数表
                standardWorkService.download(params, response);
                break;
            case 13:
                // 履历表
                changeRecordService.download(params, response);
                break;
        }


    }

}