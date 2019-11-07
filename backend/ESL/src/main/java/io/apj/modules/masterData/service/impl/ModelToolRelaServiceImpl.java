package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelToolRelaDao;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.service.ModelToolRelaService;


@Service("modelToolRelaService")
public class ModelToolRelaServiceImpl extends ServiceImpl<ModelToolRelaDao, ModelToolRelaEntity> implements ModelToolRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelToolRelaEntity> page = this.selectPage(
                new Query<ModelToolRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}