package io.apj.modules.collection.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.CompareDao;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.CompareItemEntity;
import io.apj.modules.collection.service.CompareItemService;
import io.apj.modules.collection.service.CompareService;
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
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private CompareService compareService;
    @Autowired
    private ReportService reportService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("firstColumnName") != null && params.get("firstColumnName") != "", "first_column_name", (String) params.get("firstColumnName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
                .like(params.get("lastVersionName") != null && params.get("lastVersionName") != "", "last_version_name", (String) params.get("lastVersionName"))
                .like(params.get("currentVersionName") != null && params.get("currentVersionName") != "", "current_version_name", (String) params.get("currentVersionName"))
        ;
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
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
    public PageUtils selectListTest(Map<String, Object> params) {
        PageUtils page = compareService.queryPage(params);
        List<CompareEntity> items = (List<CompareEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for(CompareEntity entity : items){
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId",modelId);
            data.put("phaseId",phaseId);
            data.put("stlst",stlst);
            String name = "Collection-Compare表";
            data.put("name",name);
            List<ReportGroupEntity> reportGroup  =  reportService.selectReportGroup(data);
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
        CompareEntity entity = generateCompare(workBookIds.get(0));
        compareItemService.generateCompareItem(workBookIds, entity);
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        CompareEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
        Integer entityId = entity.getId();
        List<CompareItemEntity> list = compareItemService.selectByMostValueId(entityId);
        ModelEntity model = modelService.selectById(modelId);
        PhaseEntity phase = phaseService.selectById(phaseId);

        Map<String, Object> map = new HashMap<>();
        map.put("modelName", model.getName());
        map.put("phaseName", phase.getName());
        map.put("lastVersionName", entity.getLastVersionName());
        map.put("currentVersionName", entity.getCurrentVersionName());
        map.put("firstColumnName", entity.getFirstColumnName());
        map.put("customer", "??");
        map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
        generateTotalData(list, map);

        String templateFileName = PathUtil.getExcelTemplatePath("collection_compare");
        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("test").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        String fileName = "Most_Value_Compare";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        excelWriter.finish();
    }

    private void generateTotalData(List<CompareItemEntity> list, Map<String, Object> map) {
        BigDecimal lastValueToatal = BigDecimal.ZERO;
        BigDecimal currentValueTotal = BigDecimal.ZERO;
        BigDecimal secondDifferenceTotal = BigDecimal.ZERO;
        BigDecimal minuteDifferenceTotal = BigDecimal.ZERO;

        for (CompareItemEntity entity: list) {
            BigDecimal lastValue = entity.getLastValue();
            BigDecimal currentValue = entity.getCurrentValue();
            currentValueTotal = currentValueTotal.add(currentValue);
            if (lastValue == null) {
                lastValue = BigDecimal.ZERO;
                entity.setLastValue(lastValue);
            }
            lastValueToatal = lastValueToatal.add(lastValue);
            BigDecimal secondDifference = currentValue.subtract(lastValue);
            BigDecimal minuteDifference = secondDifference.divide(new BigDecimal(60), 3 , BigDecimal.ROUND_HALF_UP);
            entity.setSecondDifference(secondDifference);
            entity.setMinuteDifference(minuteDifference);
            secondDifferenceTotal = secondDifferenceTotal.add(secondDifference);
            minuteDifferenceTotal = minuteDifferenceTotal.add(minuteDifference);

            Boolean sub = entity.getSub();
            if (sub) {
                entity.setSecondColumnName("SUB");
            } else {
                entity.setSecondColumnName("MAIN");
            }
        }
        map.put("lastValueToatal", lastValueToatal);
        map.put("currentValueTotal", currentValueTotal);
        map.put("secondDifferenceTotal", secondDifferenceTotal);
        map.put("minuteDifferenceTotal", minuteDifferenceTotal);
    }

    private CompareEntity generateCompare(Integer workBookId) {
        WorkBookEntity workBook = workBookService.selectById(workBookId);
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