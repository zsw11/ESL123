package io.apj.modules.collection.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.CompareItemDao;
import io.apj.modules.collection.entity.CompareItemEntity;
import io.apj.modules.collection.service.CompareItemService;


@Service("compareItemService")
public class CompareItemServiceImpl extends ServiceImpl<CompareItemDao, CompareItemEntity> implements CompareItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CompareItemEntity> page = this.selectPage(
                new Query<CompareItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}