package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ApproveOpininonEntity;

import java.util.Map;

/**
 * 常用审批意见
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ApproveOpininonService extends IService<ApproveOpininonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

