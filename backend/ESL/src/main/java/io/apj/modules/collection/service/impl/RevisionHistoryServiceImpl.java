package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.RevisionHistoryDao;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.service.RevisionHistoryService;


@Service("revisionHistoryService")
public class RevisionHistoryServiceImpl extends ServiceImpl<RevisionHistoryDao, RevisionHistoryEntity> implements RevisionHistoryService {
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
        ;
        if(StringUtils.isNotEmpty((CharSequence) params.get("modelId"))){
            entityWrapper.eq("model_id","modelId");
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))){
            entityWrapper.eq("phase_id","phaseId");
        }
        Page<RevisionHistoryEntity> page = this.selectPage(
                new Query<RevisionHistoryEntity>(params).getPage()
        );
        for(RevisionHistoryEntity entity: page.getRecords()){
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