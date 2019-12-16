package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.report.entity.ChangeRecordItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Collection - Revision History 表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface RevisionHistoryItemService extends IService<RevisionHistoryItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<RevisionHistoryItemEntity> getListBySWId(Integer id);
}

