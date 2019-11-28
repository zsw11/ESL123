package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.report.entity.ApproveEntity;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ApproveHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("nextApproverId")!=null&&params.get("nextApproverId")!="", "next_approver_id", (String) params.get("nextApproverId"))
                .like(params.get("result")!=null&& params.get("result")!="","result", (String) params.get("result"));
        Page<ApproveHistoryEntity> page = this.selectPage(
                new Query<ApproveHistoryEntity>(params).getPage(),entityWrapper
        );

        return new PageUtils(page);
    }

}