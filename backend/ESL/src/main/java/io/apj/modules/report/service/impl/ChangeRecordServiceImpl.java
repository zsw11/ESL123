package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.ChangeRecordItemService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ChangeRecordDao;
import io.apj.modules.report.service.ChangeRecordService;


@Service("changeRecordService")
public class ChangeRecordServiceImpl extends ServiceImpl<ChangeRecordDao, ChangeRecordEntity> implements ChangeRecordService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;

    @Autowired
    private ChangeRecordItemService changeRecordItemService;

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

    @Override
    public void generateReportData(WorkBookEntity work) {
        EntityWrapper<ChangeRecordEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("stlst",work.getStlst()).eq("model_id",work.getModelId())
                .eq("phase_id",work.getPhaseId());
        List<ChangeRecordEntity> list = selectList(entityWrapper);
        ChangeRecordEntity changeRecordEntity = new ChangeRecordEntity();
        if(list.size()>0){
            changeRecordEntity = list.get(0);
        }else{
            //TODO factory  title model_type 未设置
            changeRecordEntity.setModelId(work.getModelId());
            changeRecordEntity.setPhaseId(work.getPhaseId());
            changeRecordEntity.setStlst(work.getStlst());
            changeRecordEntity.setDeptId(work.getDeptId());
            changeRecordEntity.setDestinations(work.getDestinations());
            changeRecordEntity.setSheetName(work.getWorkstationName()+" "+ work.getWorkName());
            insert(changeRecordEntity);
        }
    }

}
