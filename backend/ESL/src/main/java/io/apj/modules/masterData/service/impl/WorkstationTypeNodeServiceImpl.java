package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
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
        EntityWrapper<WorkstationTypeNodeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("remark") != null && params.get("remark") != "", "remark", (String) params.get("remark"))
                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"));
        Page<WorkstationTypeNodeEntity> page = this.selectPage(new Query<WorkstationTypeNodeEntity>(params).getPage(), entityWrapper);


        return new PageUtils(page);
    }

}