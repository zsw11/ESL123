package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SysDeptService deptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<MeasureGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at", false)
                .like(params.get("code") != null && params.get("code") != "", "code", (String) params.get("code"))
                .eq(params.get("deptId") != null && params.get("deptId") != "", "dept_id", (String) params.get("deptId"));

        Page<MeasureGroupEntity> page = this.selectPage(new Query<MeasureGroupEntity>(params).getPage(), entityWrapper);
        for (MeasureGroupEntity entity : page.getRecords()) {
            entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
        }

        return new PageUtils(page);
    }

}