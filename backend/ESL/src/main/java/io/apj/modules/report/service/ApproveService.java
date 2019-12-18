package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ApproveEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
public interface ApproveService extends IService<ApproveEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void download(Map<String, Object> params, HttpServletResponse response) throws IOException;

    /**
     * 提交审批，点击确定时
     * @param approve
     * @return
     */
    List<Object> insertApprove(ApproveEntity approve);

    /**
     * 审批通过拒决
     * @param approveEntity
     * @param data
     * @return
     */
    ResponseEntity<Object> saveView(ApproveEntity approveEntity, @RequestBody Map<String,Object> data);
}

