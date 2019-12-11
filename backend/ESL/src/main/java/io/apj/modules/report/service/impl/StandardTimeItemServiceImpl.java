package io.apj.modules.report.service.impl;

import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardTimeItemDao;
import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.service.StandardTimeItemService;


@Service("standardTimeItemService")
public class StandardTimeItemServiceImpl extends ServiceImpl<StandardTimeItemDao, StandardTimeItemEntity> implements StandardTimeItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StandardTimeItemEntity> page = this.selectPage(
                new Query<StandardTimeItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public void generateStandardTimeItem(WorkBookEntity workBook, Integer standardTimeId) {
        StandardTimeItemEntity entity = baseMapper.generateDataByWorkBook(workBook.getId());
        entity.setReportStandardTimeId(standardTimeId);
        entity.setTimeSample1(entity.getTimeTotal());
        insert(entity);
    }

}