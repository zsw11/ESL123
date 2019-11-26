package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.StandardWorkEntity;

import java.util.Map;

/**
 * 标准工数表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface StandardWorkService extends IService<StandardWorkEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

