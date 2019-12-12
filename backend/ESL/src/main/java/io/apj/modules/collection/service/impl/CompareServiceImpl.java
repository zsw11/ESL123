package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.service.CompareItemService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.CompareDao;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.service.CompareService;


@Service("compareService")
public class CompareServiceImpl extends ServiceImpl<CompareDao, CompareEntity> implements CompareService {
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private WorkBookService workBookService;
    @Autowired
    private CompareItemService compareItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at").last("desc")
                .like(params.get("firstColumnName") != null && params.get("firstColumnName") != "", "first_column_name", (String) params.get("firstColumnName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
        ;
        if(StringUtils.isNotEmpty((CharSequence) params.get("modelId"))){
            entityWrapper.eq("model_id","modelId");
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))){
            entityWrapper.eq("phase_id","phaseId");
        }
        Page<CompareEntity> page = this.selectPage(
                new Query<CompareEntity>(params).getPage(),entityWrapper
        );
        for(CompareEntity entity: page.getRecords()){
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
    public void generateReportData(WorkBookEntity workBook) {
        CompareEntity entity = generateStandardTime(workBook);
        compareItemService.generateCompareItem(workBook, entity.getId());
    }

    private CompareEntity generateStandardTime(WorkBookEntity workBook) {
        Integer phaseId = workBook.getPhaseId();
        Integer modelId = workBook.getModelId();
        String stlst = workBook.getStlst();
        CompareEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        if (entity == null) {
            entity = new CompareEntity();
            entity.setModelId(modelId);
            entity.setPhaseId(phaseId);
            entity.setStlst(stlst);
            entity.setDeptId(workBook.getDeptId());
            entity.setTitle("Compare表");
            entity.setSheetName("Compare表");
            entity.setDestinations("仕向");
            entity.setFirstColumnName("LFP-PD内作");

            WorkBookEntity lastWookBook = workBookService.getLastVersion(modelId, stlst, phaseId);
            if (lastWookBook != null) {
                entity.setLastVersionName(lastWookBook.getVersionNumber());
            }
            entity.setCurrentVersionName(workBook.getVersionNumber());
            insert(entity);
        }
        return entity;
    }

    private CompareEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
        EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
        CompareEntity entity = selectOne(entityWrapper);
        return entity;
    }

}