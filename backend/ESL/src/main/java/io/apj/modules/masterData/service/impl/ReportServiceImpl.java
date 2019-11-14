package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.PhaseEntity;
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
        EntityWrapper<ReportEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("name") != null && params.get("name") != "", "form_code",
                        (String) params.get("formCode")).or()
                .or(String.valueOf(params.get("name") == "" && params.get("name") == null));
        Page<ReportEntity> page = this.selectPage(new Query<ReportEntity>(params).getPage(), entityWrapper);


        return new PageUtils(page);
    }

}