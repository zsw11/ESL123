package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.entity.ReportEntity;
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
        EntityWrapper<WorkstationTypeEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("remark") != null && params.get("remark") != "", "remark", (String) params.get("remark"))
                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"));
        Page<WorkstationTypeEntity> page = this.selectPage(new Query<WorkstationTypeEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

}