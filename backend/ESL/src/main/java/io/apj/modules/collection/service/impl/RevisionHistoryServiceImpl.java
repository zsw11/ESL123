package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public void generateReportData(WorkBookEntity work) {
        EntityWrapper<RevisionHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("stlst",work.getStlst()).eq("model_id",work.getModelId())
                .eq("phase_id",work.getPhaseId());
        List<RevisionHistoryEntity> list = selectList(entityWrapper);
        RevisionHistoryEntity revisionHistoryEntity = new RevisionHistoryEntity();
        if(list.size()>0){
            revisionHistoryEntity = list.get(0);
        }else{
            //TODO 有未设置
            revisionHistoryEntity.setModelId(work.getModelId());
            revisionHistoryEntity.setPhaseId(work.getPhaseId());
            revisionHistoryEntity.setStlst(work.getStlst());
            revisionHistoryEntity.setDeptId(work.getDeptId());
            revisionHistoryEntity.setDestinations(work.getDestinations());
            revisionHistoryEntity.setSheetName(work.getWorkstationName()+" "+ work.getWorkName());
            revisionHistoryEntity.setSheetName("sheetName");
            revisionHistoryEntity.setDestinations(work.getDestinations());
            insert(revisionHistoryEntity);
        }
    }

}
