package io.apj.modules.workBook.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.workBook.dao.WorkOperationsDao;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkOperationsService;


@Service("workOperationsService")
public class WorkOperationsServiceImpl extends ServiceImpl<WorkOperationsDao, WorkOperationsEntity> implements WorkOperationsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<WorkOperationsEntity> page = this.selectPage(
                new Query<WorkOperationsEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}