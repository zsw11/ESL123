package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.impl.ReportServiceImpl;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.TimeContactDao;
import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.service.TimeContactService;

import javax.servlet.http.HttpServletResponse;


@Service("timeContactService")
public class TimeContactServiceImpl
        extends ServiceImpl<TimeContactDao, TimeContactEntity> implements TimeContactService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private TimeContactService timeContactService;
    @Autowired
    private ReportServiceImpl reportServiceimpl;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at", false)
                .like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
                .like(params.get("operationStandardNo") != null && params.get("operationStandardNo") != "", "operation_standard_no", (String) params.get("operationStandardNo"))
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("stage") != null && params.get("stage") != "", "stage", (String) params.get("stage"))
                .like(params.get("publishType") != null && params.get("publishType") != "", "publish_type", (String) params.get("publishType"))
                .like(params.get("operationInstruction") != null && params.get("operationInstruction") != "", "operation_instruction", (String) params.get("operationInstruction"))
        ;
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
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

    @Override
    public PageUtils selectListTest(Map<String, Object> params) {
        PageUtils page = timeContactService.queryPage(params);
        List<TimeContactEntity> items = (List<TimeContactEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for(TimeContactEntity entity : items){
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId",modelId);
            data.put("phaseId",phaseId);
            data.put("stlst",stlst);
            String name = "Report-时间联络表";
            data.put("name",name);
            List<ReportGroupEntity> reportGroup  =  reportServiceimpl.selectReportGroup(data);
            if(!reportGroup.isEmpty()){
                //还可以选报表组
                entity.setExist(true);
            }else{
                entity.setExist(false);
            }
        }
        return page;
    }

    @Override
    public void generateReportData(WorkBookEntity workBookEntity) {
        TimeContactEntity entity = generateStandardTime(workBookEntity);
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        // TODo
    }

    private TimeContactEntity generateStandardTime(WorkBookEntity workBook) {
        Integer phaseId = workBook.getPhaseId();
        Integer modelId = workBook.getModelId();
        String stlst = workBook.getStlst();
        TimeContactEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        if (entity == null) {
            entity = new TimeContactEntity();
            entity.setModelId(modelId);
            entity.setPhaseId(phaseId);
            entity.setStlst(stlst);
            entity.setDeptId(workBook.getDeptId());
            entity.setTitle("时间联络表");
            entity.setSheetName("时间联络表");
            insert(entity);
        }
        return entity;
    }

    private TimeContactEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
        EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
        TimeContactEntity entity = selectOne(entityWrapper);
        return entity;
    }

}