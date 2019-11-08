package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportGroupReportRelaDao;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;


@Service("reportGroupReportRelaService")
public class ReportGroupReportRelaServiceImpl extends ServiceImpl<ReportGroupReportRelaDao, ReportGroupReportRelaEntity> implements ReportGroupReportRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportGroupReportRelaEntity> page = this.selectPage(
                new Query<ReportGroupReportRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}