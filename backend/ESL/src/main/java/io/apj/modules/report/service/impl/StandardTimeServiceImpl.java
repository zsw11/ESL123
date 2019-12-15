package io.apj.modules.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.DateUtils;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.dao.StandardTimeItemDao;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.service.StandardTimeItemService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardTimeDao;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.service.StandardTimeService;

import javax.servlet.http.HttpServletResponse;


@Service("standardTimeService")
public class StandardTimeServiceImpl extends ServiceImpl<StandardTimeDao, StandardTimeEntity> implements StandardTimeService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private StandardTimeItemService standardTimeItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<StandardTimeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("modelType") != null && params.get("modelType") != "", "model_type", (String) params.get("modelType"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("unit") != null && params.get("unit") != "", "unit", (String) params.get("unit"))
        ;
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
        Page<StandardTimeEntity> page = this.selectPage(
                new Query<StandardTimeEntity>(params).getPage(), entityWrapper
        );
        for (StandardTimeEntity entity : page.getRecords()) {
            if (entity.getModelId() != null) {
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if (entity.getPhaseId() != null) {
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
        }

        return new PageUtils(page);
    }

    @Override
    public void generateReportData(WorkBookEntity workBook) {
        StandardTimeEntity entity = generateStandardTime(workBook);
        standardTimeItemService.generateStandardTimeItem(workBook, entity.getId());
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        StandardTimeEntity standardTime = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        Integer standardTimeId = standardTime.getId();
        List<StandardTimeItemEntity> list = standardTimeItemService.selectByStandardTimeId(standardTimeId);
        ModelEntity model = modelService.selectById(modelId);

        Map<String, Object> map = new HashMap<>();
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());
        map.put("unit", standardTime.getUnit());
        map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
        generateTotalData(list, map);
        // TODO 添加调用模版方法及生成目标excel文件方法
        String templateFileName = "D:/ESL-MOST/backend/ESL/src/main/resources/static/exportTemplates/standard_time_template.xls";
        String fileName1 = "D:/ESL-MOST/backend/ESL/src/main/resources/static/exportTemplates/standard_time_template1.xls";
        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(fileName1).withTemplate(templateFileName).build();
//        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("test").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        String fileName = "标准时间表";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        excelWriter.finish();
    }

    private void generateTotalData(List<StandardTimeItemEntity> list, Map<String, Object> map) {
        BigDecimal htTotal = BigDecimal.ZERO;
        BigDecimal mtTotal = BigDecimal.ZERO;
        BigDecimal mhTotal = BigDecimal.ZERO;
        BigDecimal totalTotal = BigDecimal.ZERO;
        BigDecimal Sample1Total = BigDecimal.ZERO;
        BigDecimal SampleSizeTotal = BigDecimal.ZERO;
        BigDecimal convTotal = BigDecimal.ZERO;

        for (StandardTimeItemEntity entity: list) {
            mtTotal = mtTotal.add(entity.getMostMt());
            mhTotal = mhTotal.add(entity.getMostMh());
            htTotal = htTotal.add(entity.getMostHt());
            totalTotal = totalTotal.add(entity.getTimeTotal());
            Sample1Total = Sample1Total.add(entity.getTimeSample1());
//            SampleSizeTotal = SampleSizeTotal.add(entity.getTimeSampleSize());
            BigDecimal conv = entity.getTimeSample1().divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(60));
            entity.setConv(conv);
            convTotal = convTotal.add(conv);
        }

        map.put("htTotal", htTotal);
        map.put("mtTotal", mtTotal);
        map.put("mhTotal", mhTotal);
        map.put("totalTotal", totalTotal);
        map.put("Sample1Total", Sample1Total);
        map.put("SampleSizeTotal", SampleSizeTotal);
        map.put("convTotal", convTotal);
    }

    private StandardTimeEntity generateStandardTime(WorkBookEntity workBook) {
        Integer phaseId = workBook.getPhaseId();
        Integer modelId = workBook.getModelId();
        String stlst = workBook.getStlst();
        StandardTimeEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        if (entity == null) {
            entity = new StandardTimeEntity();
            entity.setModelId(modelId);
            entity.setPhaseId(phaseId);
            entity.setStlst(stlst);
            entity.setDeptId(workBook.getDeptId());
            entity.setTitle("标准时间表");
            entity.setSheetName("标准时间表");
            entity.setModelType(modelService.selectById(entity.getModelId()).getCode());
            entity.setUnit("1/1000 min");
            insert(entity);
        }
        return entity;
    }

    private StandardTimeEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
        EntityWrapper<StandardTimeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
        StandardTimeEntity entity = selectOne(entityWrapper);
        return entity;
    }


}