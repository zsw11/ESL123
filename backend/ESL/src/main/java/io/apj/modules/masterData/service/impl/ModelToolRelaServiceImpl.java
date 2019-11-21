package io.apj.modules.masterData.service.impl;

import io.apj.modules.masterData.entity.ModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
    public HashMap<Object, Object> selectModelByToolId(Integer id, Map<String, Object> params) {
        //治工具的机种数据
        List<ModelEntity> modelEntities = modelToolRelaDao.selectModelByToolId(id);
        if (modelEntities != null) {
            int limit = Integer.parseInt((String) params.get("limit"));
            int page = Integer.parseInt((String) params.get("page"));
            int total = modelEntities.size();
            List<ModelEntity> modelEntityList = modelEntities.subList(limit * (page - 1), ((limit * page) > total ? total : (limit * page)));
            HashMap<Object, Object> data = new HashMap<>();
            data.put("modelEntityList", modelEntityList);
            data.put("limit", limit);
            data.put("page", page);
            data.put("total", total);

            return data;
        } else {
            return null;
        }

    }

}