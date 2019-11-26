package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.TimeContactDao;
import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.service.TimeContactService;


@Service("timeContactService")
public class TimeContactServiceImpl extends ServiceImpl<TimeContactDao, TimeContactEntity> implements TimeContactService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TimeContactEntity> page = this.selectPage(
                new Query<TimeContactEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}