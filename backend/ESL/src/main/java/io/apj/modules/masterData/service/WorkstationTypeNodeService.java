package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;

import java.util.Map;

/**
 * 工位类型节点
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface WorkstationTypeNodeService extends IService<WorkstationTypeNodeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

