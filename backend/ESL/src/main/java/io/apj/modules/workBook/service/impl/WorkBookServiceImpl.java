package io.apj.modules.workBook.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.workBook.dao.WorkBookDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;


@Service("workBookService")
public class WorkBookServiceImpl extends ServiceImpl<WorkBookDao, WorkBookEntity> implements WorkBookService {
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private WorkstationService workstationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("keyWord") != null && params.get("keyWord") != "", "destinations", (String) params.get("keyWord"))
                .like(params.get("workName") != null && params.get("workName") != "", "work_name", (String) params.get("workName"));
        if(StringUtils.isNotEmpty((CharSequence) params.get("makerId"))){
            entityWrapper.eq("maker_id",Integer.parseInt((String) params.get("makerId")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("deptId"))){
            entityWrapper.eq( "dept_id", Integer.parseInt((String) params.get("deptId")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("modelId"))){
            entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))){
            entityWrapper.eq( "phase_id",Integer.parseInt((String) params.get("phaseId")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("workstationId"))){
            entityWrapper.eq("workstation_id", Integer.parseInt((String) params.get("workstationId")));
        }

        Page<WorkBookEntity> page = this.selectPage(new Query<WorkBookEntity>(params).getPage(), entityWrapper);
        for (WorkBookEntity entity : page.getRecords()) {
            if(entity.getDeptId()!=null){
                entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
            }
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
            if(entity.getWorkstationId()!=null){
                entity.setWorkName(workstationService.selectById(entity.getWorkstationId()).getName());
            }

        }
        return new PageUtils(page);

    }

}