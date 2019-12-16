package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelWorkstationRelaDao;
import io.apj.modules.masterData.entity.ModelWorkstationRelaEntity;
import io.apj.modules.masterData.service.ModelWorkstationRelaService;


@Service("modelWorkstationRelaService")
public class ModelWorkstationRelaServiceImpl extends ServiceImpl<ModelWorkstationRelaDao, ModelWorkstationRelaEntity> implements ModelWorkstationRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelWorkstationRelaEntity> page = this.selectPage(
                new Query<ModelWorkstationRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}