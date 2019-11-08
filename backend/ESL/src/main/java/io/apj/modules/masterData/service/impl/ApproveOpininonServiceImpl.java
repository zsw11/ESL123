package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ApproveOpininonDao;
import io.apj.modules.masterData.entity.ApproveOpininonEntity;
import io.apj.modules.masterData.service.ApproveOpininonService;


@Service("approveOpininonService")
public class ApproveOpininonServiceImpl extends ServiceImpl<ApproveOpininonDao, ApproveOpininonEntity> implements ApproveOpininonService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ApproveOpininonEntity> page = this.selectPage(
                new Query<ApproveOpininonEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}