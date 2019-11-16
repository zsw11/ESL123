package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ReportEntity;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"))
                .eq(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"))
        .eq(params.get("deptId") != null && params.get("deptId") != "", "dept_id", (String) params.get("deptId"))
        .eq(params.get("modelSeriesId") != null && params.get("modelSeriesId") != "", "model_series_id", (String) params.get("modelSeriesId"));

        Page<ModelEntity> page = this.selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

}