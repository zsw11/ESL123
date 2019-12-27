package io.apj.modules.report.service.impl;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.impl.ReportServiceImpl;
import io.apj.modules.report.dao.TimeContactDao;
import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.service.TimeContactService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;


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
    @Autowired
    private WorkBookService workBookService;

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
    public void generateReportData(List<Integer> workBookIds) {
        List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
        List<WorkBookEntity> filteredWorkBooks = workBookService.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
        
        List<TimeContactEntity> list = generateStandardTime(filteredWorkBooks);
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        // TODo
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id",phaseId).eq("stlst",stlst).eq("model_id",modelId);
        TimeContactEntity timeContactEntity = selectOne(entityWrapper);
        Integer id = 0;
        Map<String, Object> map = new HashMap<>();
        if(timeContactEntity!=null){
            map.put("revNo",timeContactEntity.getRevNo());
            map.put("allCountSub",timeContactEntity.getAllCountSub());
            map.put("allCountMain",timeContactEntity.getAllCountMain());
            map.put("allCountPrinting",timeContactEntity.getAllCountPrinting());
            map.put("allCountExternalInspection",timeContactEntity.getAllCountExternalInspection());
            map.put("allCountPacking",timeContactEntity.getAllCountPacking());
            map.put("towingLastVersionSub",timeContactEntity.getTowingLastVersionSub());
            map.put("towingLastVersionMain",timeContactEntity.getTowingLastVersionMain());
            map.put("towingLastVersionPrinting",timeContactEntity.getTowingLastVersionPrinting());
            map.put("towingLastVersionExternalInspection",timeContactEntity.getTowingLastVersionExternalInspection());
            map.put("towingLastVersionPacking",timeContactEntity.getTowingLastVersionPacking());
            map.put("operationStandardNo",timeContactEntity.getOperationStandardNo());
            map.put("operationInstruction",timeContactEntity.getOperationInstruction());
            map.put("exceptionOperation",timeContactEntity.getExceptionOperation());
            map.put("remarkVersionCopmare",timeContactEntity.getTowingLastVersionPrinting());
            map.put("remarkSub",timeContactEntity.getTowingLastVersionExternalInspection());
            map.put("remarkMain",timeContactEntity.getTowingLastVersionPacking());
            map.put("remarkPrinting",timeContactEntity.getTowingLastVersionPrinting());
            map.put("remarkExternalInspection",timeContactEntity.getTowingLastVersionExternalInspection());
            map.put("remarkPacking",timeContactEntity.getTowingLastVersionPacking());
        }
        ModelEntity model = modelService.selectById(modelId);
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());


        // TODO 添加调用模版方法及生成目标excel文件方法
        String path =ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String templateFileName = PathUtil.getExcelTemplatePath("report_time_contact_template");
        //String fileName1 = PathUtil.getExcelTemplatePath("report_time_contact");
        OutputStream out = response.getOutputStream();
        // ExcelWriter excelWriter = EasyExcel.write(fileName1).withTemplate(templateFileName).build();
        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("report_time_contact").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        //excelWriter.fill(list, fillConfig, writeSheet); 变更内容
        String fileName = "report_time_contact";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        excelWriter.finish();
    }

    private List<TimeContactEntity> generateStandardTime(List<WorkBookEntity> workBooks) {
        List<TimeContactEntity> results = new ArrayList<>(workBooks.size());
        for (WorkBookEntity workBook : workBooks) {

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
            results.add(entity);
        }
        return results;
    }

    private TimeContactEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
        EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
        TimeContactEntity entity = selectOne(entityWrapper);
        return entity;
    }

}
