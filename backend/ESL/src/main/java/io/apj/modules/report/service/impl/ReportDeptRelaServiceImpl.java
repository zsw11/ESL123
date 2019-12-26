package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ReportDeptRelaDao;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.service.ReportDeptRelaService;


@Service("reportDeptRelaService")
public class ReportDeptRelaServiceImpl extends ServiceImpl<ReportDeptRelaDao, ReportDeptRelaEntity> implements ReportDeptRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportDeptRelaEntity> page = this.selectPage(
                new Query<ReportDeptRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}