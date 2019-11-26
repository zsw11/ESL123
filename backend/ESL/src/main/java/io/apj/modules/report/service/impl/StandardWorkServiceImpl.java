package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardWorkDao;
import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.service.StandardWorkService;


@Service("standardWorkService")
public class StandardWorkServiceImpl extends ServiceImpl<StandardWorkDao, StandardWorkEntity> implements StandardWorkService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StandardWorkEntity> page = this.selectPage(
                new Query<StandardWorkEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}