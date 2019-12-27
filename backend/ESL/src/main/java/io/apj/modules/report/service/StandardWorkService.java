package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.StandardWorkEntity;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 标准工数表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface StandardWorkService extends IService<StandardWorkEntity> {

    PageUtils queryPage(Map<String, Object> params) throws ParseException;

    void generateReportData(List<Integer> workBookIds);

    List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException;

    /**
     * 是否还可以提交审批
     */
    PageUtils selectListTest(Map<String, Object> params) throws ParseException;
}

