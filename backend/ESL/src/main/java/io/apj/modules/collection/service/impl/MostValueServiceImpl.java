package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.MostValueDao;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.service.MostValueService;


@Service("mostValueService")
public class MostValueServiceImpl extends ServiceImpl<MostValueDao, MostValueEntity> implements MostValueService {
@Autowired
private PhaseService phaseService;
@Autowired
private ModelService modelService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<MostValueEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("firstColumnName") != null && params.get("firstColumnName") != "", "first_column_name", (String) params.get("firstColumnName"))
                .like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
        ;
        if(StringUtils.isNotEmpty((CharSequence) params.get("modelId"))){
            entityWrapper.eq("model_id","modelId");
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))){
            entityWrapper.eq("phase_id","phaseId");
        }
        Page<MostValueEntity> page = this.selectPage(
                new Query<MostValueEntity>(params).getPage(),entityWrapper
        );
        for(MostValueEntity entity: page.getRecords()){
            if(entity.getPhaseId()!=null){
                entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
            }
            if(entity.getModelId()!=null){
                entity.setModelName(modelService.selectById(entity.getModelId()).getName());
            }

        }

        return new PageUtils(page);
    }

}