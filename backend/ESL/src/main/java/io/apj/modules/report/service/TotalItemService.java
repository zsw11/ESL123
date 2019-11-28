package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.TotalItemEntity;

import java.util.Map;

/**
 * Report - Total表子表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
public interface TotalItemService extends IService<TotalItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
