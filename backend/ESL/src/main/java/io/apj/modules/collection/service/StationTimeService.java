package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import java.util.Map;

/**
 * Collection - 工位时间表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface StationTimeService extends IService<StationTimeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateReportData(WorkBookEntity workBookEntity);
}

