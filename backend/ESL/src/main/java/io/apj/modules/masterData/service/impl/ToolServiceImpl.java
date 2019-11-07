package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ToolDao;
import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.ToolService;


@Service("toolService")
public class ToolServiceImpl extends ServiceImpl<ToolDao, ToolEntity> implements ToolService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ToolEntity> page = this.selectPage(
                new Query<ToolEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}