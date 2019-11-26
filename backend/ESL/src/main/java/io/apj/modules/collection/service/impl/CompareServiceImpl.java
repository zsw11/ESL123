package io.apj.modules.collection.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.CompareDao;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.service.CompareService;


@Service("compareService")
public class CompareServiceImpl extends ServiceImpl<CompareDao, CompareEntity> implements CompareService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CompareEntity> page = this.selectPage(
                new Query<CompareEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}