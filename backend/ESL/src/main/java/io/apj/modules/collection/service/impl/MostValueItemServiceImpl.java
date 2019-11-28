package io.apj.modules.collection.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.MostValueItemDao;
import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.service.MostValueItemService;


@Service("mostValueItemService")
public class MostValueItemServiceImpl extends ServiceImpl<MostValueItemDao, MostValueItemEntity> implements MostValueItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MostValueItemEntity> page = this.selectPage(
                new Query<MostValueItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}