package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.impl.ReportServiceImpl;
import io.apj.modules.report.dao.ChangeRecordDao;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.service.ChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("changeRecordService")
public class ChangeRecordServiceImpl extends ServiceImpl<ChangeRecordDao, ChangeRecordEntity> implements ChangeRecordService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ChangeRecordService changeRecordService;
    @Autowired
    private ReportServiceImpl reportServiceimpl;

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
    public PageUtils selectListTest(Map<String, Object> params) {
        PageUtils page = changeRecordService.queryPage(params);
        List<ChangeRecordEntity> items = (List<ChangeRecordEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for(ChangeRecordEntity entity : items){
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId",modelId);
            data.put("phaseId",phaseId);
            data.put("stlst",stlst);
            String name = "履历表";
            data.put("name",name);
            List<ReportGroupEntity> reportGroup  =  reportServiceimpl.selectReportGroup(data);
            if(reportGroup!=null){
                //还可以选报表组
//                ChangeRecordEntity changeRecordEntity = new ChangeRecordEntity();
                entity.setExist(true);
            }
        }
        return page;
    }

}