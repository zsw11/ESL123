package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelPartRelaDao;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;


@Service("modelPartRelaService")
public class ModelPartRelaServiceImpl extends ServiceImpl<ModelPartRelaDao, ModelPartRelaEntity> implements ModelPartRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelPartRelaEntity> page = this.selectPage(
                new Query<ModelPartRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}