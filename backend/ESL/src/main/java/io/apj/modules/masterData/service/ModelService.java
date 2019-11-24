package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ModelEntity;

import java.util.Map;

/**
 * 机种
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ModelService extends IService<ModelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils selectBySeriesId(int id, Map<String, Object> params);

    PageUtils modelPartRelaList(int id, Map<String, Object> params);

    PageUtils modelToolRelaList(int id, Map<String, Object> params);
}

