package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.StandardWorkItemEntity;

import java.util.List;
import java.util.Map;

/**
 * 标准时间表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface StandardWorkItemService extends IService<StandardWorkItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<StandardWorkItemEntity> getListBySWId(Integer id);

    void generateStandardWorkItem(List<Integer> workBookIds, Integer standardWorkId);
}

