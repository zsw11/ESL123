package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.DeptWorkstationRelaDao;
import io.apj.modules.masterData.entity.DeptWorkstationRelaEntity;
import io.apj.modules.masterData.service.DeptWorkstationRelaService;


@Service("deptWorkstationRelaService")
public class DeptWorkstationRelaServiceImpl extends ServiceImpl<DeptWorkstationRelaDao, DeptWorkstationRelaEntity> implements DeptWorkstationRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DeptWorkstationRelaEntity> page = this.selectPage(
                new Query<DeptWorkstationRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}