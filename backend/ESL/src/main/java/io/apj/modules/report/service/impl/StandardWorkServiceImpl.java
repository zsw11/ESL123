package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.StandardTimeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardWorkDao;
import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.service.StandardWorkService;


@Service("standardWorkService")
public class StandardWorkServiceImpl extends ServiceImpl<StandardWorkDao, StandardWorkEntity> implements StandardWorkService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("modelType") != null && params.get("modelType") != "", "model_type", (String) params.get("modelType"))
                .like(params.get("coefficient") != null && params.get("coefficient") != "", "coefficient", (String) params.get("coefficient"))
                .like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
                .like(params.get("firstStandardWorkTitle") != null && params.get("firstStandardWorkTitle") != "", "first_standard_work_title", (String) params.get("firstStandardWorkTitle"));

        Page<StandardWorkEntity> page = this.selectPage(
                new Query<StandardWorkEntity>(params).getPage(), entityWrapper
        );
        for (StandardWorkEntity entity : page.getRecords()) {
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
        }

        return new PageUtils(page);
    }

}