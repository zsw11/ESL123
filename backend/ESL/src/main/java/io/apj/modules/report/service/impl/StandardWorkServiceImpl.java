package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.entity.StandardWorkItemEntity;
import io.apj.modules.report.service.StandardWorkItemService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardWorkDao;
import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.service.StandardWorkService;

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
                .like(params.get("coefficient") != null && params.get("coefficient") != "", "coefficient", (String) params.get("coefficient"))
                .like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
                .like(params.get("firstStandardWorkTitle") != null && params.get("firstStandardWorkTitle") != "", "first_standard_work_title", (String) params.get("firstStandardWorkTitle"))
                .like(params.get("thirdStandardWorkTitle") != null && params.get("thirdStandardWorkTitle") != "", "third_standard_work_title", (String) params.get("thirdStandardWorkTitle"));

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
    }

}
