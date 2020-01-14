package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ReportDeptRelaDao;
import io.apj.modules.report.dao.ReportGroupDeptRelaDao;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;
import io.apj.modules.report.service.ReportDeptRelaService;
import io.apj.modules.report.service.ReportGroupDeptRelaService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("reportGroupDeptRelaService")
public class ReportGroupDeptRelaServiceImpl extends ServiceImpl<ReportGroupDeptRelaDao, ReportGroupDeptRelaEntity> implements ReportGroupDeptRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportGroupDeptRelaEntity> page = this.selectPage(
                new Query<ReportGroupDeptRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}