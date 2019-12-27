package io.apj.modules.collection.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.*;
import io.apj.modules.collection.dao.MostValueDao;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.service.MostValueItemService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;


@Service("mostValueService")
public class MostValueServiceImpl extends ServiceImpl<MostValueDao, MostValueEntity> implements MostValueService {
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private MostValueItemService mostValueItemService;
    @Autowired
    private MostValueService mostValueService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private WorkBookService workBookService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<MostValueEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at", false)
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("firstColumnName") != null && params.get("firstColumnName") != "", "first_column_name", (String) params.get("firstColumnName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
        ;
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", "modelId");
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", "phaseId");
        }
        Page<MostValueEntity> page = this.selectPage(
                new Query<MostValueEntity>(params).getPage(), entityWrapper
        );
        for (MostValueEntity entity : page.getRecords()) {
            if (entity.getPhaseId() != null) {
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
            if (entity.getModelId() != null) {
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }

        }

        return new PageUtils(page);
    }

    @Override
    public PageUtils selectListTest(Map<String, Object> params) {
        PageUtils page = mostValueService.queryPage(params);
        List<MostValueEntity> items = (List<MostValueEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for (MostValueEntity entity : items) {
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId", modelId);
            data.put("phaseId", phaseId);
            data.put("stlst", stlst);
            String name = "Collection-MOST Value表";
            data.put("name", name);
            List<ReportGroupEntity> reportGroup = reportService.selectReportGroup(data);
            if (!reportGroup.isEmpty()) {
                //还可以选报表组
                entity.setExist(true);
            } else {
                entity.setExist(false);
            }
        }
        return page;
    }

    @Override
    public void generateReportData(List<Integer> workBookIds) {
        MostValueEntity entity = generateMostValue(workBookIds.get(0));
        mostValueItemService.generateMostValueItem(workBookIds, entity.getId());
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        MostValueEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        Integer entityId = entity.getId();
        List<MostValueItemEntity> list = mostValueItemService.selectByMostValueId(entityId);
        ModelEntity model = modelService.selectById(modelId);
        PhaseEntity phase = phaseService.selectById(phaseId);

        Map<String, Object> map = new HashMap<>();
        map.put("modelName", model.getName());
        map.put("phaseName", phase.getName());
        map.put("customer", "??");
        map.put("esl", "??");
        map.put("firstColumnName", entity.getFirstColumnName());
        map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
        generateTotalData(list, map);

        String sheetName = entity.getSheetName();
        String fileName = PathUtil.getResourcesPath() + File.separator + sheetName + ".xls";
        String templateFileName = PathUtil.getExcelTemplatePath("collection_most_value");

        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);

        int lastRow = 4 + list.size();
        int firstRow = 5;
        excelWriter.merge(firstRow, lastRow, 0, 0);
        excelWriter.merge(firstRow, lastRow, 7, 8);
        Map<Integer, Function<MostValueItemEntity, Object>> options = new HashMap<>();
        options.put(1, MostValueItemEntity::getType);
        options.put(2, MostValueItemEntity::getWorkName);
        options.put(5, MostValueItemEntity::getWorkName);
        options.put(6, MostValueItemEntity::getType);
        ExcelUtils.mergeCell(options, list, excelWriter, firstRow);
        excelWriter.finish();
        ExportExcelUtils.exportExcel(Arrays.asList(fileName), response, sheetName);
    }

    private void generateTotalData(List<MostValueItemEntity> list, Map<String, Object> map) {
        BigDecimal timeStTotal = BigDecimal.ZERO;
        BigDecimal timeTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> totalHMap = new HashMap<>();
        for (MostValueItemEntity entity: list) {
            timeTotal = timeTotal.add(entity.getTotal());
            BigDecimal timeSt = entity.getTotal().multiply(new BigDecimal(60));
            timeTotal = timeTotal.add(timeSt);
            entity.setTimeSt(timeSt);
            String type = entity.getType();
            BigDecimal totalH = totalHMap.get(type);
            if (totalH == null) {
                totalHMap.put(type, timeTotal);
            } else {
                totalHMap.put(type, totalH.add(timeTotal));
            }
        }

        generateTotalHoursForEachOfList(list, totalHMap);

        BigDecimal totalAll = timeTotal.divide(new BigDecimal(60),3,BigDecimal.ROUND_HALF_UP);
        map.put("timeStTotal", timeStTotal);
        map.put("timeTotal", timeTotal);
        map.put("totalAll", totalAll);

    }

    private void generateTotalHoursForEachOfList(List<MostValueItemEntity> list, Map<String, BigDecimal> totalHMap) {
        for (String key : totalHMap.keySet()) {
            BigDecimal totalH = totalHMap.get(key);
            totalH = totalH.divide(new BigDecimal(60),3,BigDecimal.ROUND_HALF_UP);
            totalHMap.put(key, totalH);
        }

        for (MostValueItemEntity entity: list) {
            String type = entity.getType();
            BigDecimal totalH = totalHMap.get(type);
            entity.setTotalHours(totalH);
        }
    }

    private MostValueEntity generateMostValue(Integer workBookId) {
        WorkBookEntity workBook = workBookService.selectById(workBookId);
        Integer phaseId = workBook.getPhaseId();
        Integer modelId = workBook.getModelId();
        String stlst = workBook.getStlst();
        MostValueEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        if (entity == null) {
            entity = new MostValueEntity();
            entity.setModelId(modelId);
            entity.setPhaseId(phaseId);
            entity.setStlst(stlst);
            entity.setDeptId(workBook.getDeptId());
            entity.setTitle("Most Value");
            entity.setSheetName("Most Value");
            entity.setFirstColumnName("Most Value");
            insert(entity);
        }
        return entity;
    }

    private MostValueEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
        EntityWrapper<MostValueEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
        MostValueEntity entity = selectOne(entityWrapper);
        return entity;
    }

}