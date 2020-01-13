package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ReportBatchEntity;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;

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
public interface ReportBatchService extends IService<ReportBatchEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<Object> selectAllWorkBook(Integer id);


}

