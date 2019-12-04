package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.entity.TimeContactEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.TotalDao;
import io.apj.modules.report.entity.TotalEntity;
import io.apj.modules.report.service.TotalService;


@Service("totalService")
public class TotalServiceImpl extends ServiceImpl<TotalDao, TotalEntity> implements TotalService {
    @Autowired
    private ModelService modelService;
    @Autowired
    private PhaseService phaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("destinations") != null && params.get("destinations") != "", "destinations", (String) params.get("destinations"))
                .like(params.get("cotegory") != null && params.get("cotegory") != "", "cotegory", (String) params.get("cotegory"))
                .like(params.get("mecha") != null && params.get("mecha") != "", "mecha", (String) params.get("mecha"))
                .like(params.get("rCode") != null && params.get("rCode") != "", "r_rode", (String) params.get("rCode"))
                .like(params.get("allowanceRate") != null && params.get("allowanceRate") != "", "allowanceRate", (String) params.get("allowanceRate"))
                .like(params.get("stRev") != null && params.get("stRev") != "", "st_rev", (String) params.get("stRev"))
                .like(params.get("lstRev") != null && params.get("lstRev") != "", "lst_rev", (String) params.get("lstRev"));
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

}