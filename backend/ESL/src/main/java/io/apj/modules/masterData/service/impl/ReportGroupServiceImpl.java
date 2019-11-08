package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportGroupDao;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ReportGroupService;


@Service("reportGroupService")
public class ReportGroupServiceImpl extends ServiceImpl<ReportGroupDao, ReportGroupEntity> implements ReportGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ReportGroupEntity> page = this.selectPage(
                new Query<ReportGroupEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}