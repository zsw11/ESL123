package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.DeptActionRelaEntity;

import java.util.Map;

/**
 * 组织机构关键词关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface DeptActionRelaService extends IService<DeptActionRelaEntity> {

	PageUtils queryPage(Map<String, Object> params);


}
