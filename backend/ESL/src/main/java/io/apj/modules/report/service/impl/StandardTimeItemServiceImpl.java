package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.StandardTimeItemDao;
import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.service.StandardTimeItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


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
    public void generateStandardTimeItem(List<Integer> workBookIds, Integer standardTimeId) {
        List<StandardTimeItemEntity> list = baseMapper.generateDataByWorkBook(workBookIds);
        if (list!=null&&list.size() >0) {
            for (StandardTimeItemEntity entity : list) {
                entity.setReportStandardTimeId(standardTimeId);
                entity.setTimeSample1(entity.getTimeTotal());
            }
            insertOrUpdateBatch(list);
        }
    }

    @Override
    public List<StandardTimeItemEntity> selectByStandardTimeId(Integer standardTimeId) {
        EntityWrapper<StandardTimeItemEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("report_standard_time_id", standardTimeId);
        return selectList(wrapper);
    }

}