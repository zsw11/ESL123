package io.apj.modules.collection.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.RevisionHistoryItemDao;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;


@Service("revisionHistoryItemService")
public class RevisionHistoryItemServiceImpl extends ServiceImpl<RevisionHistoryItemDao, RevisionHistoryItemEntity> implements RevisionHistoryItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RevisionHistoryItemEntity> page = this.selectPage(
                new Query<RevisionHistoryItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}