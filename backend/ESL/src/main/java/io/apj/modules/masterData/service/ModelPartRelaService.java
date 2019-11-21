package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;

import java.util.Map;

/**
 * 机种部品关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ModelPartRelaService extends IService<ModelPartRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils selectModelByPartId(Integer id, Map<String, Object> params);
}

