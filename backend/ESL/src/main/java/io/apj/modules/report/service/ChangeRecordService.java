package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.swagger.models.auth.In;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
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

    /**
     * 判断当前报表是否可以提交审批
     * @param params
     * @return
     */
    PageUtils selectListTest(Map<String, Object> params);

    void generateReportData(List<Integer> workBookIds, Integer reportId);

    void updateEntity(ChangeRecordEntity changeRecord);

    List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException;
}

