package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.WorkstationTypeNodeDao;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;
import io.apj.modules.masterData.service.WorkstationTypeNodeService;


@Service("workstationTypeNodeService")
public class WorkstationTypeNodeServiceImpl extends ServiceImpl<WorkstationTypeNodeDao, WorkstationTypeNodeEntity> implements WorkstationTypeNodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<WorkstationTypeNodeEntity> page = this.selectPage(
                new Query<WorkstationTypeNodeEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}