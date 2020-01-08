package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.MostValueItemDao;
import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.service.MostValueItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("mostValueItemService")
public class MostValueItemServiceImpl extends ServiceImpl<MostValueItemDao, MostValueItemEntity> implements MostValueItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MostValueItemEntity> page = this.selectPage(
                new Query<MostValueItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public void generateMostValueItem(List<Integer> workBookIds, Integer recordId) {
        List<MostValueItemEntity> list = baseMapper.generateDataByWorkBook(workBookIds);
        if (list!=null&&list.size() >0) {
            for (MostValueItemEntity entity : list) {
                entity.setCollectionMostValueId(recordId);
            }
            insertOrUpdateBatch(list);
        }
    }

    @Override
    public List<MostValueItemEntity> selectByMostValueId(Integer entityId) {
        EntityWrapper<MostValueItemEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("collection_most_value_id", entityId);
        return selectList(wrapper);
    }

}