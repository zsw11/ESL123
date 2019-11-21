package io.apj.modules.masterData.service.impl;

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
    public Page<ModelEntity> selectModelByPartId(Integer id, Map<String, Object> params) {
        //部品的机种数据
        List<ModelEntity> modelEntities = modelPartRelaDao.selectModelByPartId(id);
        if(modelEntities!=null){
            int total = modelEntities.size();
            int limit = Integer.parseInt((String) params.get("limit"));
            int pagecurrent = Integer.parseInt((String) params.get("page"));
            Page<ModelEntity> page =new Page<ModelEntity>();
            page.setRecords(modelEntities);
            page.setCurrent(pagecurrent);
            page.setTotal(total);
            page.setSize(limit);
//            List<ModelEntity> modelEntityList = modelEntities.subList(limit * (page - 1), ((limit * page) > total ? total : (limit * page)));
//            HashMap<Object, Object> data = new HashMap<>();
//            data.put("modelEntityList", modelEntityList);
//            data.put("limit",limit);
//            data.put("page", page);
//            data.put("total", total);

            return page;
        }else {
            return null;
        }

    }

}