package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.CompareItemEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import java.util.List;
import java.util.Map;

/**
 * Collection - Compare表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface CompareItemService extends IService<CompareItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateCompareItem(WorkBookEntity workBook, Integer compareId);

    List<CompareItemEntity> selectByMostValueId(Integer entityId);
}

