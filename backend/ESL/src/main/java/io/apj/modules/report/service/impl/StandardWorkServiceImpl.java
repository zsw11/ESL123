package io.apj.modules.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.DateUtils;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.entity.StandardWorkItemEntity;
import io.apj.modules.report.service.StandardWorkItemService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardWorkDao;
import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.service.StandardWorkService;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletResponse;


@Service("standardWorkService")
public class StandardWorkServiceImpl extends ServiceImpl<StandardWorkDao, StandardWorkEntity> implements StandardWorkService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;

    @Autowired
    private  StandardWorkItemService standardWorkItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("monthResult") != null && params.get("monthResult") != "", "month_result", (String) params.get("monthResult"))
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("modelType") != null && params.get("modelType") != "", "model_type", (String) params.get("modelType"))
                .like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
                .like(params.get("firstStandardWorkTitle") != null && params.get("firstStandardWorkTitle") != "", "first_standard_work_title", (String) params.get("firstStandardWorkTitle"))
                .like(params.get("thirdStandardWorkTitle") != null && params.get("thirdStandardWorkTitle") != "", "third_standard_work_title", (String) params.get("thirdStandardWorkTitle"));

        if (StringUtils.isNotEmpty((CharSequence) params.get("coefficient"))) {
            entityWrapper.eq("coefficient",(String) params.get("coefficient"));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
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

    /**
     * report 加数据
     * @param work
     */
    @Override
    public void generateReportData(WorkBookEntity work) {
        EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("stlst",work.getStlst()).eq("model_id",work.getModelId())
                .eq("phase_id",work.getPhaseId());
        List<StandardWorkEntity> list = selectList(entityWrapper);
        StandardWorkEntity standardWorkEntity = new StandardWorkEntity();
        if(list.size()>0){
            standardWorkEntity = list.get(0);
        }else{
            standardWorkEntity.setModelId(work.getModelId());
            standardWorkEntity.setPhaseId(work.getPhaseId());
            standardWorkEntity.setStlst(work.getStlst());
            standardWorkEntity.setDeptId(work.getDeptId());
            insert(standardWorkEntity);
        }
        StandardWorkItemEntity standardWorkItem = new StandardWorkItemEntity();
        standardWorkItem.setReportStandardWorkId(standardWorkEntity.getId());
        standardWorkItem.setSecondTime(work.getSecondConvert());
        standardWorkItem.setFirstTime(work.getSecondConvert());
        standardWorkItem.setThirdTime(work.getSecondConvert());
        standardWorkItem.setProcessNo(work.getWorkstationId());
        standardWorkItem.setProcessName(work.getWorkName());
        standardWorkItemService.insert(standardWorkItem);

    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        //TODO
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id",phaseId).eq("stlst",stlst).eq("model_id",modelId);
        StandardWorkEntity standardWorkEntity = selectOne(entityWrapper);
        Integer id = 0;
        if(standardWorkEntity!=null){
            id = standardWorkEntity.getId();
        }
        List<StandardWorkItemEntity> list = standardWorkItemService.getListBySWId(id);
        ModelEntity model = modelService.selectById(modelId);

        Map<String, Object> map = new HashMap<>();
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());
        //map.put("unit", standardWorkEntity.getUnit());
        map.put("date", standardWorkEntity.getMonthResult());
        generateTotalData(list, map);
        // TODO 添加调用模版方法及生成目标excel文件方法
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath().split("target")[0];
        System.out.println(path);
        String templateFileName = path+"src/main/resources/static/exportTemplates/report_standard_work_template.xls";
        //String fileName1 = path+"src/main/resources/static/exportTemplates/report_standard_work.xls";
        OutputStream out = response.getOutputStream();
        //ExcelWriter excelWriter = EasyExcel.write(fileName1).withTemplate(templateFileName).build();
        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("standardWork").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        String fileName = "标准工数表";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        excelWriter.finish();
    }

    private void generateTotalData(List<StandardWorkItemEntity> list, Map<String, Object> map) {
        BigDecimal htTotal = BigDecimal.ZERO;
        BigDecimal stTotal = BigDecimal.ZERO;

        for (StandardWorkItemEntity entity: list) {
            htTotal = htTotal.add(entity.getFirstTime().multiply(BigDecimal.valueOf(1000)).divide(BigDecimal.valueOf(3600)));
            stTotal = stTotal.add(entity.getFirstTime());
            entity.setHfTime(entity.getFirstTime().multiply(BigDecimal.valueOf(1000)).divide(BigDecimal.valueOf(3600)));
        }
        map.put("htTotal", htTotal);
        map.put("stTotal", stTotal);

    }

}
