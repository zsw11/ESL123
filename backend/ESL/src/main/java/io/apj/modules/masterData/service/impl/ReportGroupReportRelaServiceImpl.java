package io.apj.modules.masterData.service.impl;

import io.apj.modules.masterData.entity.ReportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportGroupReportRelaDao;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import org.springframework.transaction.annotation.Transactional;


@Service("reportGroupReportRelaService")
@Transactional
public class ReportGroupReportRelaServiceImpl extends ServiceImpl<ReportGroupReportRelaDao, ReportGroupReportRelaEntity> implements ReportGroupReportRelaService {
    @Autowired
    private ReportGroupReportRelaDao reportGroupReportRelaDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportGroupReportRelaEntity> page = this.selectPage(
                new Query<ReportGroupReportRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ReportEntity> selectReportNameByReportGroupId(int id) {
        return reportGroupReportRelaDao.selectReportNameByReportGroupId(id);
    }


}