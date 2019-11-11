package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.MeasureGroupDao;
import io.apj.modules.masterData.entity.MeasureGroupEntity;
import io.apj.modules.masterData.service.MeasureGroupService;


@Service("measureGroupService")
public class MeasureGroupServiceImpl extends ServiceImpl<MeasureGroupDao, MeasureGroupEntity> implements MeasureGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MeasureGroupEntity> page = this.selectPage(
                new Query<MeasureGroupEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}