package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import java.util.Map;

/**
 * Collection - MOST Value 表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface MostValueService extends IService<MostValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateReportData(WorkBookEntity workBook);
}

