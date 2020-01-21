package io.apj.modules.report.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.AssembleProcessListEntity;

/**
 * Report - 时间联络表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface ProcessListService extends IService<AssembleProcessListEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateReportData(List<Integer> workBookIds,Integer reportId,Integer userId);

    List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException;

    PageUtils selectListTest(Map<String, Object> params);
}

