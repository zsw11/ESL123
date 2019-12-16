package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ModelWorkstationRelaEntity;

import java.util.Map;

/**
 * 机种工位关系表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-16 17:37:03
 */
public interface ModelWorkstationRelaService extends IService<ModelWorkstationRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

