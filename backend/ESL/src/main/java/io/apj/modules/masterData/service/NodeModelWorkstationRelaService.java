package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.NodeModelWorkstationRelaEntity;

import java.util.Map;

/**
 * 报表组部门关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2020-01-15 18:11:00
 */
public interface NodeModelWorkstationRelaService extends IService<NodeModelWorkstationRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

