package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.StationTimeItemDao;
import io.apj.modules.collection.entity.StationTimeItemEntity;
import io.apj.modules.collection.service.StationTimeItemService;


@Service("stationTimeItemService")
public class StationTimeItemServiceImpl extends ServiceImpl<StationTimeItemDao, StationTimeItemEntity> implements StationTimeItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StationTimeItemEntity> page = this.selectPage(
                new Query<StationTimeItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public List<StationTimeItemEntity> getListBySWId(Integer id) {
        EntityWrapper<StationTimeItemEntity> ew = new EntityWrapper<>();
        ew.eq("collection_station_time_id",id);
        return selectList(ew);
    }

}
