package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.entity.TotalEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ApproveHistoryDao;
import io.apj.modules.report.entity.ApproveHistoryEntity;
import io.apj.modules.report.service.ApproveHistoryService;
import io.apj.modules.report.service.ApproveHistoryService;


@Service("approveHistoryService")
public class ApproveHistoryServiceImpl extends ServiceImpl<ApproveHistoryDao, ApproveHistoryEntity> implements ApproveHistoryService {
    @Autowired
    private ReportGroupService reportGroupService;
    @Autowired
    private ApproveHistoryService approveHistoryService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private StaffService staffService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("result") != null && params.get("result") != "", "result", (String) params.get("result"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
               ;
        if(StringUtils.isNotEmpty((CharSequence) params.get("nextApproverId"))){
            entityWrapper.eq("next_approver_id", Integer.parseInt((String) params.get("nextApproverId")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("reportGroupId"))){
            entityWrapper.eq("report_group_id", Integer.parseInt((String) params.get("reportGroupId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
        Page<ApproveHistoryEntity> page = this.selectPage(
                new Query<ApproveHistoryEntity>(params).getPage(), entityWrapper    );
        for (ApproveHistoryEntity entity : page.getRecords()) {
            if(entity.getReportGroupId()!=null){
                entity.setReportGroupName(reportGroupService.selectById(entity.getReportGroupId()).getName());
            }

        }
        for (ApproveHistoryEntity entity : page.getRecords()) {
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
            if(entity.getNextApproverId()!=null){
                entity.setNextApproverName(staffService.selectById(entity.getNextApproverId()).getName());
            }
        }

        return new PageUtils(page);
    }

    @Override
    public Integer insertApproveHisttory(ApproveEntity approve) {
        ApproveHistoryEntity approveHistoryEntity = new ApproveHistoryEntity();
        approveHistoryEntity.setDeptId(approve.getDeptId());
        approveHistoryEntity.setReportApproveId(approve.getId());
        approveHistoryEntity.setResult(approve.getStatus());
        approveHistoryEntity.setReportGroupId(approve.getReportGroupId());
        approveHistoryEntity.setNextApproverId(approve.getNextApproverId());
        approveHistoryService.insert(approveHistoryEntity);

        return null;
    }

}