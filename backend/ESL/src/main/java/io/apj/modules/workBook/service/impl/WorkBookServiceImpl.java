package io.apj.modules.workBook.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ToolEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.workBook.dao.WorkBookDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;


@Service("workBookService")
public class WorkBookServiceImpl extends ServiceImpl<WorkBookDao, WorkBookEntity> implements WorkBookService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at")
//                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"))
                .like(params.get("keyWord") != null && params.get("keyWord") != "", "destinations", (String) params.get("keyWord"))
        ;
        Page<WorkBookEntity> page = this.selectPage(new Query<WorkBookEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);

    }

}