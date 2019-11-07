package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.DeptWorkstationRelaEntity;

import java.util.Map;

/**
 * 组织机构工位关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface DeptWorkstationRelaService extends IService<DeptWorkstationRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

