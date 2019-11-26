package io.apj.modules.report.service.impl;

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
        Page<ApproveHistoryEntity> page = this.selectPage(
                new Query<ApproveHistoryEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}