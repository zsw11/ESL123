package io.apj.modules.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.PathUtil;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.ChangeRecordItemService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.impl.ReportServiceImpl;
import io.apj.modules.report.dao.ChangeRecordDao;
import io.apj.modules.report.service.ChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private ChangeRecordItemService changeRecordItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ChangeRecordEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("factory") != null && params.get("factory") != "", "factory", (String) params.get("factory"))
                .like(params.get("modelType") != null && params.get("modelType") != "", "model_type", (String) params.get("modelType"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"));

        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
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
            if(!reportGroup.isEmpty()){
                //还可以选报表组
                entity.setExist(true);
            }else{
                entity.setExist(false);
            }
        }
        return page;
    }

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

    @Override
    public void updateEntity(ChangeRecordEntity changeRecord) {

        if(changeRecord.getItems().size()>0){
            changeRecordItemService.insertOrUpdateBatch(changeRecord.getItems());
        }
        if(changeRecord.getModelId()==0)changeRecord.setModelId(null);
        //TODO 随意设置一个值 后期要删除
        changeRecord.setTitle("oo");
        updateById(changeRecord);

    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        //TODO
        Integer phaseId =  Integer.valueOf(params.get("phaseId").toString());
        Integer modelId = Integer.valueOf(params.get("modelId").toString());
        String stlst =  params.get("stlst").toString();

        EntityWrapper<ChangeRecordEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id",phaseId).eq("stlst",stlst).eq("model_id",modelId);
        ChangeRecordEntity changeRecordEntity = selectOne(entityWrapper);
        Integer id = 0;
        if(changeRecordEntity!=null){
            id = changeRecordEntity.getId();
        }
        List<ChangeRecordItemEntity> list = changeRecordItemService.getListBySWId(id);
        ModelEntity model = modelService.selectById(modelId);

        Map<String, Object> map = new HashMap<>();
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());
        generateTotalData(list, map);
        // TODO 添加调用模版方法及生成目标excel文件方法
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath().split("target")[0];
        System.out.println(path);
        path = path.split("target")[0];
        String templateFileName = PathUtil.getExcelTemplatePath("report_change_record_template");
        //String fileName1 = path+"src/main/resources/static/exportTemplates/report_change_record.xls";
        String fileName = "test";

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(), "ISO-8859-1") + ".xls");
        OutputStream out = response.getOutputStream();

        //ExcelWriter excelWriter = EasyExcel.write(fileName1).withTemplate(templateFileName).build();
        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("report_change_record").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        excelWriter.finish();
        out.flush();
    }

    private void generateTotalData(List<ChangeRecordItemEntity> list, Map<String, Object> map) {
        for (ChangeRecordItemEntity entity: list) {
            BigDecimal sub = entity.getLastValue()==null?BigDecimal.valueOf(0):entity.getLastValue();
            entity.setSubValue(entity.getCurrentValue()==null?BigDecimal.valueOf(0):entity.getCurrentValue().subtract(sub));
        }
    }

}
