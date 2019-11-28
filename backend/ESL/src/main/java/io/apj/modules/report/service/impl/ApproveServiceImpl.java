package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("nextApproverId")!=null&&params.get("nextApproverId")!="", "next_approver_id", (String) params.get("nextApproverId"))
                .like(params.get("status")!=null&& params.get("status")!="","status", (String) params.get("status"));
        Page<ApproveEntity> page = this.selectPage(new Query<ApproveEntity>(params).getPage(), entityWrapper);
        return new PageUtils(page);
    }

}