package io.apj.modules.report.service;

import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.TotalEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.service.IService;

/**
 * Report - Total表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:58
 */
public interface TotalService extends IService<TotalEntity> {

    PageUtils queryPage(Map<String, Object> params) throws ParseException;

    void generateReportData(List<Integer> workBookIds);

    List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException;

    /**
     * 是否还可以提交审批
     * @param params
     * @return
     */
    PageUtils selectListTest(Map<String, Object> params) throws ParseException;
}

