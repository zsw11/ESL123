package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.PartDao;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.PartService;


@Service("partService")
public class PartServiceImpl extends ServiceImpl<PartDao, PartEntity> implements PartService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PartEntity> page = this.selectPage(
                new Query<PartEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}