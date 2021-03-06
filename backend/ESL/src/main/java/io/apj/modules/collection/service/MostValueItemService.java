package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.MostValueItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Collection - MOST Value 表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface MostValueItemService extends IService<MostValueItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateMostValueItem(List<Integer> workBookIds, Integer recordId);

    List<MostValueItemEntity> selectByMostValueId(Integer entityId);

}

