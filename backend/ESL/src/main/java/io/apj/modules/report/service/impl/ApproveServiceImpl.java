package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.masterData.service.ReportGroupService;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ApproveDao;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.service.ApproveService;


@Service("approveService")
public class ApproveServiceImpl extends ServiceImpl<ApproveDao, ApproveEntity> implements ApproveService {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private ReportGroupService reportGroupService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("status")!=null&& params.get("status")!="","status", (String) params.get("status"));
        if(StringUtils.isNotEmpty((CharSequence) params.get("reportGroupId"))){
            entityWrapper.eq("report_group_id", Integer.parseInt((String) params.get("reportGroupId")));
        }
        Page<ApproveEntity> page = this.selectPage(new Query<ApproveEntity>(params).getPage(), entityWrapper);
        for(ApproveEntity entity: page.getRecords()){
            entity.setDeptName(sysDeptService.selectById(entity.getDeptId()).getName());
            entity.setReportGroupName(reportGroupService.selectById(entity.getReportGroupId()).getName());
        }
        return new PageUtils(page);
    }

}