package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.MeasureGroupEntity;

import java.util.Map;

/**
 * 常用指标组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
public interface MeasureGroupService extends IService<MeasureGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

