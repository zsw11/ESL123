package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.TotalItemDao;
import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;


@Service("totalItemService")
public class TotalItemServiceImpl extends ServiceImpl<TotalItemDao, TotalItemEntity> implements TotalItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TotalItemEntity> page = this.selectPage(
                new Query<TotalItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}