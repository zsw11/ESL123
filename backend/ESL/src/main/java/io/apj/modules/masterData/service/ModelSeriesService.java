package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ModelSeriesEntity;

import java.util.Map;

/**
 * 机种系列
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ModelSeriesService extends IService<ModelSeriesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

