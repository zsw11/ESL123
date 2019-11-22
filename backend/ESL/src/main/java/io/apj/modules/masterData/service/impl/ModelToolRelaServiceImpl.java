package io.apj.modules.masterData.service.impl;

import io.apj.modules.masterData.entity.ModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ModelToolRelaDao;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.service.ModelToolRelaService;


@Service("modelToolRelaService")
public class ModelToolRelaServiceImpl extends ServiceImpl<ModelToolRelaDao, ModelToolRelaEntity> implements ModelToolRelaService {
    @Autowired
    private  ModelToolRelaDao modelToolRelaDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelToolRelaEntity> page = this.selectPage(
                new Query<ModelToolRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public Page<Map<String, Object>> selectModelByToolId(Integer id, Map<String, Object> params) {
        //治工具的机种数据
        //新建分页
        Page<Map<String,Object>> page  = new Page<>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
               return page.setRecords(this.baseMapper.selectModelByToolId(id, (Map<String, Object>) page));

    }

}