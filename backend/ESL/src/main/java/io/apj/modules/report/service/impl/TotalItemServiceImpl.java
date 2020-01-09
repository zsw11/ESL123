package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.report.dao.TotalItemDao;
import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;


@Service("totalItemService")
public class TotalItemServiceImpl extends ServiceImpl<TotalItemDao, TotalItemEntity> implements TotalItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TotalItemEntity> page = this.selectPage(
                new Query<TotalItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public List<TotalItemEntity> getListBySWId(Integer id) {
        EntityWrapper<TotalItemEntity> ew = new EntityWrapper<>();
        ew.eq("report_total_id",id);
        return selectList(ew);
    }

    @Override
    public void generateTotalItem(List<Integer> workBookIds, Integer recordId) {
//        List<TotalItemEntity> list = baseMapper.generateDataByWorkBook(workBookIds);
//        if (list!=null&&list.size() >0) {
//            for (TotalItemEntity entity : list) {
//                entity.setReportTotalId(recordId);
//            }
//            insertOrUpdateBatch(list);
//        }
    }

}
