package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.PartEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.WorkstationDao;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.WorkstationService;


@Service("workstationService")
public class WorkstationServiceImpl extends ServiceImpl<WorkstationDao, WorkstationEntity> implements WorkstationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<WorkstationEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("name") != null && params.get("name") != "", "name",
                        (String) params.get("name"))
                .like(params.get("keyWord") != null && params.get("keyWord") != "", "name",(String) params.get("keyWord"));
        Page<WorkstationEntity> page = this.selectPage(new Query<WorkstationEntity>(params).getPage(), entityWrapper);


        return new PageUtils(page);
    }

}