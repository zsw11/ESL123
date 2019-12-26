package io.apj.modules.collection.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.collection.entity.CompareEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Collection - Compare表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
public interface CompareService extends IService<CompareEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void generateReportData(List<Integer> workBookIds);

    void download(Map<String, Object> params, HttpServletResponse response) throws IOException;

    /**
     * 是否还可以提交审批
     */
    PageUtils selectListTest(Map<String, Object> params);
}

