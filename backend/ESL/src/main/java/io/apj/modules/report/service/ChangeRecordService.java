package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ChangeRecordEntity;

import java.util.Map;

/**
 * 履历表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface ChangeRecordService extends IService<ChangeRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

