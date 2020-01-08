package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.CompareItemDao;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.CompareItemEntity;
import io.apj.modules.collection.service.CompareItemService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("compareItemService")
public class CompareItemServiceImpl extends ServiceImpl<CompareItemDao, CompareItemEntity> implements CompareItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CompareItemEntity> page = this.selectPage(
                new Query<CompareItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public void generateCompareItem(List<Integer> workBookIds, CompareEntity entity) {
        Integer phaseId = entity.getPhaseId();
        Integer modelId = entity.getModelId();
        String stlst = entity.getStlst();
        Integer compareId = entity.getId();
        Map<String, Object> params = new HashMap<>();
        params.put("phaseId", phaseId);
        params.put("modelId", modelId);
        params.put("stlst", stlst);
        params.put("workBookIds", workBookIds);

        List<CompareItemEntity> list = baseMapper.generateDataByWorkBook(params);
        if (list!=null&&list.size()>0) {
            for (CompareItemEntity item : list) {
                item.setCollectionCompareId(compareId);
            }
            insertOrUpdateBatch(list);
        }
    }

    @Override
    public List<CompareItemEntity> selectByMostValueId(Integer entityId) {
        EntityWrapper<CompareItemEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("collection_compare_id", entityId);
        return selectList(wrapper);

    }

}