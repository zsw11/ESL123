package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.OpertaionGroupDao;
import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.OpertaionGroupService;


@Service("opertaionGroupService")
public class OpertaionGroupServiceImpl extends ServiceImpl<OpertaionGroupDao, OpertaionGroupEntity> implements OpertaionGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<OpertaionGroupEntity> page = this.selectPage(
                new Query<OpertaionGroupEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}