package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.StandardTimeItemEntity;

import java.util.List;
import java.util.Map;

/**
 * 标准时间表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface StandardTimeItemService extends IService<StandardTimeItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
    void generateStandardTimeItem(List<Integer> workBookIds, Integer standardTimeId);

    List<StandardTimeItemEntity> selectByStandardTimeId(Integer standardTimeId);
}

