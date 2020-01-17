package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-12-24 16:26:43
 */
public interface ReportManMachineCombinationService extends IService<ReportManMachineCombinationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void download(Map<String, Object> params, HttpServletResponse response) throws IOException;

    void generateReportData(List<Integer> workBookIds,Integer reportId);

    void update(ReportManMachineCombinationEntity reportManMachineCombinationEntity);
}

