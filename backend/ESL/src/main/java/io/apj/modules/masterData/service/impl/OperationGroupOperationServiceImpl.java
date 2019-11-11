package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.OperationGroupOperationDao;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;


@Service("operationGroupOperationService")
public class OperationGroupOperationServiceImpl extends ServiceImpl<OperationGroupOperationDao, OperationGroupOperationEntity> implements OperationGroupOperationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<OperationGroupOperationEntity> page = this.selectPage(
                new Query<OperationGroupOperationEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}