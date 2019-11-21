package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelPartRelaDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Map;


@Service("modelPartRelaService")
public class ModelPartRelaServiceImpl extends ServiceImpl<ModelPartRelaDao, ModelPartRelaEntity> implements ModelPartRelaService {
    @Autowired
    private ModelPartRelaDao modelPartRelaDao;
    @Autowired
    private ModelService modelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelPartRelaEntity> page = this.selectPage(
                new Query<ModelPartRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils selectModelByPartId(Integer id, Map<String, Object> params) {
        //部品的机种数据
        List<ModelEntity> modelEntities = modelPartRelaDao.selectModelByPartId(id);
        EntityWrapper<ModelEntity> wrapper = new EntityWrapper<>();
        wrapper.setEntity((ModelEntity) modelEntities);
        Page<ModelEntity> page = modelService.selectPage(new Query<ModelEntity>(params).getPage(), wrapper);

        return new PageUtils(page);
    }

}