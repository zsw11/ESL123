package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.report.entity.ChangeRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardTimeDao;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.service.StandardTimeService;


@Service("standardTimeService")
public class StandardTimeServiceImpl extends ServiceImpl<StandardTimeDao, StandardTimeEntity> implements StandardTimeService {
    @Autowired
    private ModelService modelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<StandardTimeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("modelType") != null && params.get("modelType") != "", "model_type", (String) params.get("modelType"))
                .like(params.get("unit") != null && params.get("unit") != "", "unit", (String) params.get("unit"))
        ;
        Page<StandardTimeEntity> page = this.selectPage(
                new Query<StandardTimeEntity>(params).getPage(), entityWrapper
        );
        for (StandardTimeEntity entity : page.getRecords()) {
            entity.setModelName(modelService.selectById(entity.getModelId()).getName());
        }

        return new PageUtils(page);
    }

}