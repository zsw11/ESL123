package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    PageUtils selectListTest(Map<String, Object> params);

    void generateReportData(WorkBookEntity workBookEntity);

    void updateEntity(ChangeRecordEntity changeRecord);

    void download(Map<String, Object> params, HttpServletResponse response) throws IOException;
}

