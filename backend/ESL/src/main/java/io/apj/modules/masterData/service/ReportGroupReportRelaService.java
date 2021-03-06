package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;

import java.util.List;
import java.util.Map;

/**
 * 报表组报表关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ReportGroupReportRelaService extends IService<ReportGroupReportRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ReportEntity> selectReportNameByReportGroupId(int id);
}

