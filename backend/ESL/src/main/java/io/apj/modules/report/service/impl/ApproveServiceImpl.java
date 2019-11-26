package io.apj.modules.report.service.impl;

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
        Page<ApproveEntity> page = this.selectPage(
                new Query<ApproveEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}