package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.TotalEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import java.util.Map;

/**
 * Report - Total表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
public interface TotalService extends IService<TotalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateReportData(WorkBookEntity workBookEntity);
}

