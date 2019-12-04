package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.entity.PhaseEntity;
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
import io.apj.modules.collection.dao.StationTimeDao;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.StationTimeService;


@Service("stationTimeService")
public class StationTimeServiceImpl extends ServiceImpl<StationTimeDao, StationTimeEntity> implements StationTimeService {
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<StationTimeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("remark") != null && params.get("remark") != "", "remark", (String) params.get("remark"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
        ;
        if(StringUtils.isNotEmpty((CharSequence) params.get("modelId"))){
            entityWrapper.eq("model_id","modelId");
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))){
            entityWrapper.eq("phase_id","phaseId");
        }

        Page<StationTimeEntity> page = this.selectPage(
                new Query<StationTimeEntity>(params).getPage(),entityWrapper
        );
        for(StationTimeEntity entity: page.getRecords()){
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }

        }

        return new PageUtils(page);
    }

}