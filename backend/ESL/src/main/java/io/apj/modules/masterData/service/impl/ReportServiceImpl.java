package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportDao;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ReportService;


@Service("reportService")
public class ReportServiceImpl extends ServiceImpl<ReportDao, ReportEntity> implements ReportService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportEntity> page = this.selectPage(
                new Query<ReportEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}