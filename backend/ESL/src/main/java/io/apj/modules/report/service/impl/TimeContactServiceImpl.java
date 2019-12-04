package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.StandardWorkEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
        ;
        Page<TimeContactEntity> page = this.selectPage(
                new Query<TimeContactEntity>(params).getPage(), entityWrapper
        );
        for (TimeContactEntity entity : page.getRecords()) {
            if(entity.getModelId()!= null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
        }

        return new PageUtils(page);
    }

}