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


@Service("approveHistoryService")
public class ApproveHistoryServiceImpl extends ServiceImpl<ApproveHistoryDao, ApproveHistoryEntity> implements ApproveHistoryService {
    @Autowired
    private ReportGroupService reportGroupService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("result") != null && params.get("result") != "", "result", (String) params.get("result"));
        if(StringUtils.isNotEmpty((CharSequence) params.get("nextApproverId"))){
            entityWrapper.eq("next_approver_id", Integer.parseInt((String) params.get("nextApproverId")));
        }
        Page<ApproveHistoryEntity> page = this.selectPage(
                new Query<ApproveHistoryEntity>(params).getPage(), entityWrapper    );
        for (ApproveHistoryEntity entity : page.getRecords()) {
            if(reportGroupService.selectById(entity.getReportGroupId()).getName()!=null){
                entity.setReportGroupName(reportGroupService.selectById(entity.getReportGroupId()).getName());
            }

        }

        return new PageUtils(page);
    }

}