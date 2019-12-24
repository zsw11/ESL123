package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ReportManMachineCombinationDao;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;
import io.apj.modules.report.service.ReportManMachineCombinationService;


@Service("reportManMachineCombinationService")
public class ReportManMachineCombinationServiceImpl extends ServiceImpl<ReportManMachineCombinationDao, ReportManMachineCombinationEntity> implements ReportManMachineCombinationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportManMachineCombinationEntity> page = this.selectPage(
                new Query<ReportManMachineCombinationEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}