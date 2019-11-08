package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.DeptOperationRelaDao;
import io.apj.modules.masterData.entity.DeptOperationRelaEntity;
import io.apj.modules.masterData.service.DeptOperationRelaService;


@Service("deptOperationRelaService")
public class DeptOperationRelaServiceImpl extends ServiceImpl<DeptOperationRelaDao, DeptOperationRelaEntity> implements DeptOperationRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DeptOperationRelaEntity> page = this.selectPage(
                new Query<DeptOperationRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}