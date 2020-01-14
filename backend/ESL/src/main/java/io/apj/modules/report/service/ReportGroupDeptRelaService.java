package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;

import java.util.Map;

/**
 * 报表部门关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-26 17:32:34
 */
public interface ReportGroupDeptRelaService extends IService<ReportGroupDeptRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

