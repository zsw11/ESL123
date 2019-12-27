package io.apj.modules.report.service.impl;

import io.apj.common.utils.ExcelUtils;
import io.apj.common.utils.ExportExcelUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.dao.TotalDao;
import io.apj.modules.report.entity.TotalEntity;
import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;
import io.apj.modules.report.service.TotalService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;


@Service("totalService")
public class TotalServiceImpl extends ServiceImpl<TotalDao, TotalEntity> implements TotalService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private TotalService totalService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private TotalItemService totalItemService;
    @Autowired
    private WorkBookService workBookService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws ParseException {
        EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
                .like(params.get("cotegory") != null && params.get("cotegory") != "", "cotegory", (String) params.get("cotegory"))
                .like(params.get("mecha") != null && params.get("mecha") != "", "mecha", (String) params.get("mecha"))
                .like(params.get("rCode") != null && params.get("rCode") != "", "r_rode", (String) params.get("rCode"))
                .like(params.get("allowanceRate") != null && params.get("allowanceRate") != "", "allowanceRate", (String) params.get("allowanceRate"))
                .like(params.get("stRev") != null && params.get("stRev") != "", "st_rev", (String) params.get("stRev"))
                .like(params.get("lstRev") != null && params.get("lstRev") != "", "lst_rev", (String) params.get("lstRev"));

        Map<String,Object> map = (Map) JSON.parse((String) params.get("monthResult"));
        if(map!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date start = format.parse((String) map.get("monthResultStart"));
            Date stop = format.parse((String) map.get("monthResultStop"));
            entityWrapper.between("month_result",start,stop);
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
        Page<TotalEntity> page = this.selectPage(
                new Query<TotalEntity>(params).getPage(), entityWrapper
        );
        for (TotalEntity entity : page.getRecords()) {
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }

        }
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectListTest(Map<String, Object> params) throws ParseException {
        PageUtils page = totalService.queryPage(params);
        List<TotalEntity> items = (List<TotalEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for(TotalEntity entity : items){
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId",modelId);
            data.put("phaseId",phaseId);
            data.put("stlst",stlst);
            String name = "Report-Total表";
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
        List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
        List<WorkBookEntity> filteredWorkBooks = workBookService.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
        List<TotalEntity> list = generateTotal(filteredWorkBooks);
    }

    private List<TotalEntity> generateTotal(List<WorkBookEntity> workBooks) {
        List<TotalEntity> results = new ArrayList<>(workBooks.size());
        for (WorkBookEntity work : workBooks) {
            EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("stlst",work.getStlst()).eq("model_id",work.getModelId())
                    .eq("phase_id",work.getPhaseId());
            List<TotalEntity> list = selectList(entityWrapper);
            TotalEntity totalEntity = new TotalEntity();
            if(list.size()>0){
                totalEntity = list.get(0);
            }else{
                //TODO 有未设置
                totalEntity.setModelId(work.getModelId());
                totalEntity.setPhaseId(work.getPhaseId());
                totalEntity.setStlst(work.getStlst());
                totalEntity.setDeptId(work.getDeptId());
                totalEntity.setDestinations(work.getDestinations());
                totalEntity.setSheetName("total");
                totalEntity.setDestinations(work.getDestinations());
                insert(totalEntity);
            }
            results.add(totalEntity);
        }
        return results;
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        //TODO
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id",phaseId).eq("stlst",stlst).eq("model_id",modelId);
        TotalEntity totalEntity = selectOne(entityWrapper);
        Integer id = 0;
        Map<String, Object> map = new HashMap<>();
        if(totalEntity!=null){
            id = totalEntity.getId();
            map.put("monthResult",totalEntity.getMonthResult());
            map.put("destinations",totalEntity.getDestinations());
        }
        List<TotalItemEntity> list = totalItemService.getListBySWId(id);
        ModelEntity model = modelService.selectById(modelId);
        generateTotalData(list, map);
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());


        // TODO 添加调用模版方法及生成目标excel文件方法
        String sheetName = totalEntity.getSheetName();
        String fileName = PathUtil.getResourcesPath() + File.separator + sheetName + ".xls";
        String templateFileName = PathUtil.getExcelTemplatePath("report_total_template");

        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("report_total_template").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);

        int lastRow = 13 + list.size();
        int firstRow = 14;
        Map<Integer, Function<TotalItemEntity, Object>> options = new HashMap<>();
        options.put(0, TotalItemEntity::getWorkName);
        ExcelUtils.mergeCell(options, list, excelWriter, firstRow);

        excelWriter.finish();
        ExportExcelUtils.exportExcel(Arrays.asList(fileName), response, sheetName);
    }

    private void generateTotalData(List<TotalItemEntity> list, Map<String, Object> map) {
        BigDecimal htTotal = BigDecimal.ZERO;
        BigDecimal mtTotal = BigDecimal.ZERO;
        BigDecimal mhTotal = BigDecimal.ZERO;
        BigDecimal totalTotal = BigDecimal.ZERO;
        BigDecimal Sample1Total = BigDecimal.ZERO;
        BigDecimal SampleSizeTotal = BigDecimal.ZERO;
        BigDecimal convTotal = BigDecimal.ZERO;

        for (TotalItemEntity entity: list) {

        }

        map.put("htTotal", htTotal);
        map.put("mtTotal", mtTotal);
        map.put("mhTotal", mhTotal);
        map.put("totalTotal", totalTotal);
        map.put("Sample1Total", Sample1Total);
        map.put("SampleSizeTotal", SampleSizeTotal);
        map.put("convTotal", convTotal);
    }


}
