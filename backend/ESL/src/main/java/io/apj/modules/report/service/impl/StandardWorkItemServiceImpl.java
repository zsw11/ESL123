package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardWorkItemDao;
import io.apj.modules.report.entity.StandardWorkItemEntity;
import io.apj.modules.report.service.StandardWorkItemService;


@Service("standardWorkItemService")
public class StandardWorkItemServiceImpl extends ServiceImpl<StandardWorkItemDao, StandardWorkItemEntity> implements StandardWorkItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StandardWorkItemEntity> page = this.selectPage(
                new Query<StandardWorkItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}