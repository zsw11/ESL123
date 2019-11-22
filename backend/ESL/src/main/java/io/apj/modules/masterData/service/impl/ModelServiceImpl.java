package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.modules.masterData.service.ModelSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;


@Service("modelService")
public class ModelServiceImpl extends ServiceImpl<ModelDao, ModelEntity> implements ModelService {
    @Autowired
    private ModelSeriesService modelSeriesService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"))
                .eq(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"))
        .eq(params.get("deptId") != null && params.get("deptId") != "", "dept_id", (String) params.get("deptId"))
        .eq(params.get("modelSeriesId") != null && params.get("modelSeriesId") != "", "model_series_id", (String) params.get("modelSeriesId"));

         if(StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))){
             String name = (String) params.get("keyWord");
             name = name.replace(",","");
             entityWrapper.andNew("name  like '%" +name +"%'" + " or code  like '%" +name +"%'" );
         }
        Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);
         for(ModelEntity entity :page.getRecords()){
             entity.setModelSeriesEntity(modelSeriesService.selectById(entity.getModelSeriesId()));
         }
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectBySeriesId(int id, Map<String, Object> params) {
        EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .eq("model_series_id",id);
        Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

}