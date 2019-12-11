package io.apj.modules.report.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.report.entity.ApproveEntity;
import io.apj.modules.report.entity.ApproveHistoryEntity;

import java.util.Map;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
public interface ApproveHistoryService extends IService<ApproveHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer insertApproveHisttory(ApproveEntity approve);
}

