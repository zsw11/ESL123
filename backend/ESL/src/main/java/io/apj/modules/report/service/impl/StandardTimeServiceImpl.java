package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardTimeDao;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.service.StandardTimeService;


@Service("standardTimeService")
public class StandardTimeServiceImpl extends ServiceImpl<StandardTimeDao, StandardTimeEntity> implements StandardTimeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StandardTimeEntity> page = this.selectPage(
                new Query<StandardTimeEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}