package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.PartDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelSeriesService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PartService;
import io.apj.modules.sys.service.SysDeptService;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("partService")
public class PartServiceImpl extends ServiceImpl<PartDao, PartEntity> implements PartService {
    @Autowired
    private ModelPartRelaService modelPartRelaService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelSeriesService modelSeriesService;
    @Autowired
    private SysDeptService deptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<PartEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at");
        if (params.get("name") != null && params.get("name") != "") {
            params.put("name", ((String) params.get("name")).replace('*', '%'));
            entityWrapper.andNew(
                    "pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
        }
        Page<PartEntity> page = this.selectPage(new Query<PartEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

    @Override
    public PageUtils partModeRelaList(Integer id, Map<String, Object> params) {
    	//判断从哪里查
        if (params.size() > 5) {
            List<ModelPartRelaEntity> modelPartRelaEntityList = modelPartRelaService.selectList(new EntityWrapper<ModelPartRelaEntity>().eq("part_id", id));
            for (ModelPartRelaEntity entity : modelPartRelaEntityList) {
                Integer entityModelId = entity.getModelId();
                // 机种里modelid为entityModelId
                EntityWrapper<ModelEntity> entityWrapper = new EntityWrapper<ModelEntity>();
                entityWrapper.in("id", Collections.singleton(entityModelId))
                        .like(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"));
                if (StringUtils.isNotEmpty((CharSequence) params.get("modelSeriesId"))) {
                    entityWrapper.eq("model_series_id", Integer.parseInt((String) params.get("modelSeriesId")));
                }
                if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
                    entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
                }
                if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
                    String name = (String) params.get("name");
                    name = name.replace(",", "");
                    entityWrapper.and("name  like '%" + name + "%'" + " or pinyin  like '%" + name + "%'");
                }
                Page<ModelEntity> page = modelService
                        .selectPage(new Query<ModelEntity>(params).getPage(), entityWrapper);
                return new PageUtils(page);
            }

        } else {
            EntityWrapper<ModelPartRelaEntity> relaEntityWrapper = new EntityWrapper<ModelPartRelaEntity>();
            relaEntityWrapper.eq("part_id", id).isNull("delete_at");
            Page<ModelPartRelaEntity> page = modelPartRelaService
                    .selectPage(new Query<ModelPartRelaEntity>(params).getPage(), relaEntityWrapper);
            for (ModelPartRelaEntity item : page.getRecords()) {
                item.setModelEntity(modelService.selectById(item.getModelId()));
                int modelSeriesId = modelService.selectById(item.getModelId()).getModelSeriesId();
                item.setModelSeriesEntity(modelSeriesService.selectById(modelSeriesId));
                item.setDeptName(deptService.selectById(modelService.selectById(item.getModelId()).getDeptId()).getName());
            }
            return new PageUtils(page);
        }
		return null;
    }

}