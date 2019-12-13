package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.modules.report.entity.ApproveEntity;
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
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("result") != null && params.get("result") != "", "result", (String) params.get("result"))
               ;
        if(StringUtils.isNotEmpty((CharSequence) params.get("nextApproverId"))){
            entityWrapper.eq("next_approver_id", Integer.parseInt((String) params.get("nextApproverId")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("reportGroupId"))){
            entityWrapper.eq("report_group_id", Integer.parseInt((String) params.get("reportGroupId")));
        }
        Page<ApproveHistoryEntity> page = this.selectPage(
                new Query<ApproveHistoryEntity>(params).getPage(), entityWrapper    );
        for (ApproveHistoryEntity entity : page.getRecords()) {
            if(entity.getReportGroupId()!=null){
                entity.setReportGroupName(reportGroupService.selectById(entity.getReportGroupId()).getName());
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