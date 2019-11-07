package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.PartEntity;

import java.util.Map;

/**
 * 部品
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface PartService extends IService<PartEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

