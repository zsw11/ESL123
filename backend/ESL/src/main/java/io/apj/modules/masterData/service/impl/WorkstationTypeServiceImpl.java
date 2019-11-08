package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.WorkstationTypeDao;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.WorkstationTypeService;


@Service("workstationTypeService")
public class WorkstationTypeServiceImpl extends ServiceImpl<WorkstationTypeDao, WorkstationTypeEntity> implements WorkstationTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<WorkstationTypeEntity> page = this.selectPage(
                new Query<WorkstationTypeEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}