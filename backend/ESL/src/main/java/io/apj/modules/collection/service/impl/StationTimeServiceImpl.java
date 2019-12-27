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
import io.apj.modules.collection.dao.StationTimeDao;
import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.entity.StationTimeItemEntity;
import io.apj.modules.collection.service.StationTimeItemService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.masterData.service.WorkstationService;
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


@Service("stationTimeService")
public class StationTimeServiceImpl extends ServiceImpl<StationTimeDao, StationTimeEntity> implements StationTimeService {
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private StationTimeItemService stationTimeItemService;
    @Autowired
    private StationTimeService stationTimeService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private WorkstationService workstationService;
    @Autowired
    private WorkBookService workBookService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<StationTimeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("remark") != null && params.get("remark") != "", "remark", (String) params.get("remark"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
        ;
        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }

        Page<StationTimeEntity> page = this.selectPage(
                new Query<StationTimeEntity>(params).getPage(),entityWrapper
        );
        for(StationTimeEntity entity: page.getRecords()){
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
        PageUtils page = stationTimeService.queryPage(params);
        List<StationTimeEntity> items = (List<StationTimeEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for(StationTimeEntity entity : items){
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId",modelId);
            data.put("phaseId",phaseId);
            data.put("stlst",stlst);
            String name = "Collection-工位时间表";
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
        List<StationTimeEntity> list = generateStationTime(filteredWorkBooks);
        for (StationTimeEntity entity : list) {
            List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks, entity.getModelId(), entity.getStlst(), entity.getPhaseId());
            stationTimeItemService.generateStationTimeItem(filteredWorkBookIds, entity.getId());
        }
    }

    private List<StationTimeEntity> generateStationTime(List<WorkBookEntity> workBooks) {
        List<StationTimeEntity> results = new ArrayList<>(workBooks.size());
        for (WorkBookEntity work : workBooks) {

            EntityWrapper<StationTimeEntity> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("stlst",work.getStlst()).eq("model_id",work.getModelId())
                    .eq("phase_id",work.getPhaseId());
            List<StationTimeEntity> list = selectList(entityWrapper);
            StationTimeEntity stationTimeEntity = new StationTimeEntity();
            if(list.size()>0){
                stationTimeEntity = list.get(0);
            }else{
                stationTimeEntity.setModelId(work.getModelId());
                stationTimeEntity.setPhaseId(work.getPhaseId());
                stationTimeEntity.setStlst(work.getStlst());
                stationTimeEntity.setDeptId(work.getDeptId());
                stationTimeEntity.setDestinations(work.getDestinations());
                WorkstationEntity workstation = workstationService.selectById(work.getWorkstationId());
                stationTimeEntity.setSheetName(workstation.getName()+" "+work.getWorkName() );
                insert(stationTimeEntity);
            }
            results.add(stationTimeEntity);
        }
        return results;
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        // TODO
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        EntityWrapper<StationTimeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id",phaseId).eq("stlst",stlst).eq("model_id",modelId);
        StationTimeEntity stationTimeEntity = selectOne(entityWrapper);
        Integer id = 0;
        Map<String, Object> map = new HashMap<>();
        if(stationTimeEntity!=null){
            id = stationTimeEntity.getId();
            map.put("remark", stationTimeEntity.getRemark());
        }
        List<StationTimeItemEntity> list = stationTimeItemService.getListBySWId(id);
        ModelEntity model = modelService.selectById(modelId);
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());

        generateTotalData(list, map);
        // TODO 添加调用模版方法及生成目标excel文件方法
        String sheetName = stationTimeEntity.getSheetName();
        String fileName = PathUtil.getResourcesPath() + File.separator + sheetName + ".xls";
        String templateFileName = PathUtil.getExcelTemplatePath("collection_station_time_template");
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        excelWriter.finish();

        int lastRow = 1 + list.size();
        int firstRow = 2;
        Map<Integer, Function<StationTimeItemEntity, Object>> options = new HashMap<>();
        options.put(0, StationTimeItemEntity::getSub);
        options.put(1, StationTimeItemEntity::getStationName);
        ExcelUtils.mergeCell(options, list, excelWriter, firstRow);

        ExportExcelUtils.exportExcel(Arrays.asList(fileName), response, sheetName);
    }

    private void generateTotalData(List<StationTimeItemEntity> list, Map<String, Object> map) {
        BigDecimal htTotal = BigDecimal.ZERO;
        BigDecimal mtTotal = BigDecimal.ZERO;
        BigDecimal mhTotal = BigDecimal.ZERO;
        BigDecimal totalTotal = BigDecimal.ZERO;
        BigDecimal Sample1Total = BigDecimal.ZERO;
        BigDecimal SampleSizeTotal = BigDecimal.ZERO;
        BigDecimal convTotal = BigDecimal.ZERO;
        for (StationTimeItemEntity entity: list) {
            mtTotal =  mtTotal.add(entity.getLstValue());
        }
        if(list.size()>0){
            map.put("totalPer", "100%");
        }else{
            map.put("totalPer", "");
            return;
        }
        for (StationTimeItemEntity entity: list) {
            entity.setSubName(entity.getSub()==true?"SUB":"MAIN");
            entity.setHour(entity.getLstValue().divide(BigDecimal.valueOf(60),5));
            BigDecimal per = entity.getLstValue().multiply(BigDecimal.valueOf(100));//;
            entity.setPer(per.divide(mtTotal,5)+" %");
        }
        map.put("totalMin",mtTotal );
        map.put("totalH", mtTotal.divide(BigDecimal.valueOf(60),5));
    }

}
