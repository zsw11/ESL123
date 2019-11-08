package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.PhaseEntity;

import java.util.Map;

/**
 * 生产阶段
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface PhaseService extends IService<PhaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

