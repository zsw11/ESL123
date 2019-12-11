package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import java.util.Map;

/**
 * 标准时间表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface StandardTimeService extends IService<StandardTimeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateReportData(WorkBookEntity workBook);
}

