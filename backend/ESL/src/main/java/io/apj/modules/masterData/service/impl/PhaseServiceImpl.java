package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.PhaseDao;
import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.service.PhaseService;


@Service("phaseService")
public class PhaseServiceImpl extends ServiceImpl<PhaseDao, PhaseEntity> implements PhaseService {
    @Autowired
    private PhaseService  phaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<PhaseEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("name") != null && params.get("name") != "", "name",
                        (String) params.get("name"))
               .like(params.get("keyWord") != null && params.get("keyWord") != "" , "name", (String) params.get("keyWord"))
                .ne(params.get("keyWord") != null && params.get("keyWord") != "", "id", params.get("id"));
        Page<PhaseEntity> page = this.selectPage(new Query<PhaseEntity>(params).getPage(), entityWrapper);

        for(PhaseEntity entity :page.getRecords()){
            entity.setPhaseEntity(phaseService.selectById(entity.getContinuePhaseId()));
        }
        return new PageUtils(page);
    }

}