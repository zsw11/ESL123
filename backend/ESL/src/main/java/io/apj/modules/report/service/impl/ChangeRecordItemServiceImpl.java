package io.apj.modules.report.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ChangeRecordItemDao;
import io.apj.modules.report.entity.ChangeRecordItemEntity;
import io.apj.modules.report.service.ChangeRecordItemService;


@Service("changeRecordItemService")
public class ChangeRecordItemServiceImpl extends ServiceImpl<ChangeRecordItemDao, ChangeRecordItemEntity> implements ChangeRecordItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ChangeRecordItemEntity> page = this.selectPage(
                new Query<ChangeRecordItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}