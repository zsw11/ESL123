package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.ApproveHistoryEntity;
import io.apj.modules.report.entity.ChangeRecordItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ChangeRecordDao;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.service.ChangeRecordService;


@Service("changeRecordService")
public class ChangeRecordServiceImpl extends ServiceImpl<ChangeRecordDao, ChangeRecordEntity> implements ChangeRecordService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ChangeRecordEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("factory") != null && params.get("factory") != "", "factory", (String) params.get("factory"))
                .like(params.get("modelType") != null && params.get("modelType") != "", "model_type", (String) params.get("modelType"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"));
        Page<ChangeRecordEntity> page = this.selectPage(
                new Query<ChangeRecordEntity>(params).getPage(), entityWrapper
        );
        for (ChangeRecordEntity entity : page.getRecords()) {
            if((entity.getModelId())!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }

        }

        return new PageUtils(page);
    }

}