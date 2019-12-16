package io.apj.modules.collection.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.entity.ChangeRecordItemEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.RevisionHistoryDao;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.service.RevisionHistoryService;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletResponse;


@Service("revisionHistoryService")
public class RevisionHistoryServiceImpl extends ServiceImpl<RevisionHistoryDao, RevisionHistoryEntity> implements RevisionHistoryService {
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private RevisionHistoryService revisionHistoryService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private RevisionHistoryItemService revisionHistoryItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
                .like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name", (String) params.get("sheetName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
                .like(params.get("factory") != null && params.get("factory") != "", "factory", (String) params.get("factory"))
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
                .like(params.get("lastStName") != null && params.get("lastStName") != "", "last_st_name", (String) params.get("lastStName"))
                .like(params.get("currentStName") != null && params.get("currentStName") != "", "current_st_name", (String) params.get("currentStName"))
                .like(params.get("lastLstName") != null && params.get("lastLstName") != "", "last_lst_name", (String) params.get("lastLstName"))
                .like(params.get("currentLstName") != null && params.get("currentLstName") != "", "current_lst_name", (String) params.get("currentLstName"))
        ;

        if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
            entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
        }
        Page<RevisionHistoryEntity> page = this.selectPage(
                new Query<RevisionHistoryEntity>(params).getPage()
        );
        for(RevisionHistoryEntity entity: page.getRecords()){
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
        PageUtils page = revisionHistoryService.queryPage(params);
        List<RevisionHistoryEntity> items = (List<RevisionHistoryEntity>) page.getData();
        int phaseId;
        int modelId;
        String stlst;
        for(RevisionHistoryEntity entity : items){
            phaseId = entity.getPhaseId();
            modelId = entity.getModelId();
            stlst = entity.getStlst();
            Map<String, Object> data = new HashMap<>();
            data.put("modelId",modelId);
            data.put("phaseId",phaseId);
            data.put("stlst",stlst);
            String name = "Collection-Revision History表";
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
        EntityWrapper<RevisionHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("stlst",work.getStlst()).eq("model_id",work.getModelId())
                .eq("phase_id",work.getPhaseId());
        List<RevisionHistoryEntity> list = selectList(entityWrapper);
        RevisionHistoryEntity revisionHistoryEntity = new RevisionHistoryEntity();
        if(list.size()>0){
            revisionHistoryEntity = list.get(0);
        }else{
            //TODO 有未设置
            revisionHistoryEntity.setModelId(work.getModelId());
            revisionHistoryEntity.setPhaseId(work.getPhaseId());
            revisionHistoryEntity.setStlst(work.getStlst());
            revisionHistoryEntity.setDeptId(work.getDeptId());
            revisionHistoryEntity.setDestinations(work.getDestinations());
            revisionHistoryEntity.setSheetName(work.getWorkstationName()+" "+ work.getWorkName());
            revisionHistoryEntity.setSheetName("sheetName");
            revisionHistoryEntity.setDestinations(work.getDestinations());
            insert(revisionHistoryEntity);
        }
    }

    @Override
    public void updateEntity(RevisionHistoryEntity revisionHistory) {
        revisionHistoryItemService.insertOrUpdateBatch(revisionHistory.getItems());
        if(revisionHistory.getModelId()==0)revisionHistory.setModelId(null);
        revisionHistory.setSheetName("sheet");
        updateById(revisionHistory);
    }

    @Override
    public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
        //TODO
        Integer phaseId = (Integer)params.get("phaseId");
        Integer modelId = (Integer)params.get("modelId");
        String stlst = params.get("stlst").toString();

        EntityWrapper<RevisionHistoryEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("phase_id",phaseId).eq("stlst",stlst).eq("model_id",modelId);
        RevisionHistoryEntity revisionHistoryEntity = selectOne(entityWrapper);
        Integer id = 0;
        Map<String, Object> map = new HashMap<>();
        if(revisionHistoryEntity!=null){
            id = revisionHistoryEntity.getId();
            map.put("factory", revisionHistoryEntity.getFactory());
            map.put("monthResult", revisionHistoryEntity.getMonthResult());
            map.put("rev_no", revisionHistoryEntity.getRevNo());
            map.put("destinations", revisionHistoryEntity.getDestinations());
        }
        List<RevisionHistoryItemEntity> list = revisionHistoryItemService.getListBySWId(id);
        ModelEntity model = modelService.selectById(modelId);
        map.put("modelName", model.getName());
        map.put("modelType", model.getCode());


        // TODO 添加调用模版方法及生成目标excel文件方法
        System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath().split("target")[0];
        String templateFileName = path+"src/main/resources/static/static/exportTemplates/collection_revision_history_template.xls";
        //String fileName1 = path+"src/main/resources/static/static/exportTemplates/collection_revision_history.xls";
        OutputStream out = response.getOutputStream();
        //ExcelWriter excelWriter = EasyExcel.write(fileName1).withTemplate(templateFileName).build();

        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFileName).build();

        WriteSheet writeSheet = EasyExcel.writerSheet("collection_revision_history").build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(list, fillConfig, writeSheet);
        String fileName = "Revision_History";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        excelWriter.finish();
    }

}
