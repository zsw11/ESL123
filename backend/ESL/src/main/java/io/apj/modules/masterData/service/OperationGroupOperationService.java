package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;

import java.util.Map;

/**
 * 手顺
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
public interface OperationGroupOperationService extends IService<OperationGroupOperationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

