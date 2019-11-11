package io.apj.modules.workBook.service.impl;

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
        Page<WorkBookEntity> page = this.selectPage(
                new Query<WorkBookEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}