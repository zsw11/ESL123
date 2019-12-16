package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.report.entity.ApproveHistoryEntity;
import io.apj.modules.report.entity.StandardWorkItemEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.ChangeRecordItemDao;
import io.apj.modules.report.entity.ChangeRecordItemEntity;
import io.apj.modules.report.service.ChangeRecordItemService;


@Service("changeRecordItemService")
public class ChangeRecordItemServiceImpl extends ServiceImpl<ChangeRecordItemDao, ChangeRecordItemEntity> implements ChangeRecordItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Page<ChangeRecordItemEntity> page = this.selectPage(
                new Query<ChangeRecordItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ChangeRecordItemEntity> getListBySWId(Integer id) {
        List<ChangeRecordItemEntity> list = Collections.EMPTY_LIST;
        EntityWrapper<ChangeRecordItemEntity> ew = new EntityWrapper<>();
        ew.eq("report_change_record_id",id);
        list = selectList(ew);
        return list;
    }

}
