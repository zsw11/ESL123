package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelPartRelaDao;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Page<Map<String, Object>> selectModelByPartId(Integer id, Map<String, Object> params) {
        //新建分页
        Page<Map<String,Object>> page  = new Page<>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
                return page.setRecords(this.baseMapper.selectModelByPartId(id, page));

    }

}