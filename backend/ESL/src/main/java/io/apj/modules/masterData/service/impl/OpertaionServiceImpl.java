package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ToolEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.OpertaionDao;
import io.apj.modules.masterData.entity.OpertaionEntity;
import io.apj.modules.masterData.service.OpertaionService;


@Service("opertaionService")
public class OpertaionServiceImpl extends ServiceImpl<OpertaionDao, OpertaionEntity> implements OpertaionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<OpertaionEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
                .like(params.get("name") != null && params.get("name") != "", "name",
                        (String) params.get("name"));
        Page<OpertaionEntity> page = this.selectPage(new Query<OpertaionEntity>(params).getPage(), ent

        return new PageUtils(page);
    }

}