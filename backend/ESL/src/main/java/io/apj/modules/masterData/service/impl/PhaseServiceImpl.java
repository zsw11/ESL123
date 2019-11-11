package io.apj.modules.masterData.service.impl;

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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PhaseEntity> page = this.selectPage(
                new Query<PhaseEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}