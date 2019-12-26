package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.StationTimeItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Collection - 工位时间表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface StationTimeItemService extends IService<StationTimeItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<StationTimeItemEntity> getListBySWId(Integer id);

    void generateStationTimeItem(List<Integer> workBookIds, Integer stationTimeId);
}

