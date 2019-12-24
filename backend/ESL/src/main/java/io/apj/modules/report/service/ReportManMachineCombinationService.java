package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
public interface ReportManMachineCombinationService extends IService<ReportManMachineCombinationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

