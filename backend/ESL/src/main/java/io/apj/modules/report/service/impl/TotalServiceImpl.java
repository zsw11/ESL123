package io.apj.modules.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.TotalDao;
import io.apj.modules.report.entity.TotalEntity;
import io.apj.modules.report.service.TotalService;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletResponse;


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

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws ParseException {
        EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("monthResult") != null && params.get("monthResult") != "", "month_result", (String) params.get("monthResult"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
                .like(params.get("cotegory") != null && params.get("cotegory") != "", "cotegory", (String) params.get("cotegory"))
                .like(params.get("mecha") != null && params.get("mecha") != "", "mecha", (String) params.get("mecha"))
                .like(params.get("rCode") != null && params.get("rCode") != "", "r_rode", (String) params.get("rCode"))
                .like(params.get("allowanceRate") != null && params.get("allowanceRate") != "", "allowanceRate", (String) params.get("allowanceRate"))
                .like(params.get("stRev") != null && params.get("stRev") != "", "st_rev", (String) params.get("stRev"))
                .like(params.get("lstRev") != null && params.get("lstRev") != "", "lst_rev", (String) params.get("lstRev"));

        Map<String,Object> map = (Map) JSON.parse((String) params.get("createAt"));
        if(map!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date start = format.parse((String) map.get("createAtStart"));
            Date stop = format.parse((String) map.get("createAtStop"));
            entityWrapper.between("maked_at",start,stop);
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
    public void generateReportData(WorkBookEntity work) {
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
            totalEntity.setSheetName(work.getWorkstationName()+" "+ work.getWorkName());
            totalEntity.setSheetName("sheetName");
            totalEntity.setDestinations(work.getDestinations());
            insert(totalEntity);
        }
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
        }
        List<TotalItemEntity> list = totalItemService.getListBySWId(id);
        ModelEntity model = modelService.selectById(modelId);
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());


        // TODO 添加调用模版方法及生成目标excel文件方法
        System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
        String path =ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String templateFileName = path+"exportTemplates/collection_revision_history_template.xls";
        String fileName1 = path+"exportTemplates/collection_revision_history.xls";
        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(fileName1).withTemplate(templateFileName).build();
        //    ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("collection_revision_history").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        String fileName = "Revision_History";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        excelWriter.finish();
    }

}
