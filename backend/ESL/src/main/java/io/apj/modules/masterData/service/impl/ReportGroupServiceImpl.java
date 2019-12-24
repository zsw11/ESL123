package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportGroupDao;
import io.apj.modules.masterData.service.ReportGroupService;

@Service("reportGroupService")
public class ReportGroupServiceImpl extends ServiceImpl<ReportGroupDao, ReportGroupEntity>
        implements ReportGroupService {
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;
    @Autowired
    private ReportService reportService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ReportGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false)
                .like(params.get("remark") != null && params.get("remark") != "","remark", (String) params.get("remark"));

        if(StringUtils.isNotEmpty((CharSequence) params.get("reportId"))){
            List<ReportGroupReportRelaEntity> groupReportRelaList = reportGroupReportRelaService.selectList(new EntityWrapper<ReportGroupReportRelaEntity>().eq("report_id", Integer.parseInt((String) params.get("reportId"))));
             List reportGroupId = new ArrayList();
            for(ReportGroupReportRelaEntity item : groupReportRelaList){
                reportGroupId.add(item.getReportGroupId());
            }
            reportGroupId.add(0);
            entityWrapper.in("id",reportGroupId);
        }

        if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
            entityWrapper.andNew(
                    "pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))) {
            String name = (String) params.get("keyWord");
            name = name.replace(",", "");
            entityWrapper.andNew("name  like '%" + name + "%'" + " or code  like '%" + name + "%'"
                    + " or pinyin  like '%" + name + "%'");
        }
        Page<ReportGroupEntity> page = this.selectPage(new Query<ReportGroupEntity>(params).getPage(), entityWrapper);
        for (ReportGroupEntity entity : page.getRecords()) {
            List<ReportEntity> reportEntities = reportGroupReportRelaService
                    .selectReportNameByReportGroupId(entity.getId());
            String name = "";
            for (ReportEntity item : reportEntities) {
                name += item.getName() + "/";
            }
            entity.setAllReportName(name);
        }

        return new PageUtils(page);
    }

    @Override
    public List<ReportGroupReportRelaEntity> reportGroupRelaList(Integer id) {
        EntityWrapper<ReportGroupReportRelaEntity> reportRelaEntityEntityWrapper = new EntityWrapper<ReportGroupReportRelaEntity>();
        reportRelaEntityEntityWrapper.eq("report_group_id", id).isNull("delete_at");
        List<ReportGroupReportRelaEntity> data = reportGroupReportRelaService.selectList(reportRelaEntityEntityWrapper);
        for (ReportGroupReportRelaEntity item : data) {
            item.setReportEntity(reportService.selectById(item.getReportId()));
        }
        return data;
    }

    @Override
    public void deleteList(List<ReportGroupEntity> reportGroupEntityList) {
        for(ReportGroupEntity item : reportGroupEntityList){
            item.setDeleteAt(new Date());
        }
        if(reportGroupEntityList.size()>0){
            this.updateAllColumnBatchById(reportGroupEntityList);
        }

    }

    @Override
    public void deleteByIds(Collection<? extends Serializable> ids) {
        List<ReportGroupEntity> reportGroupEntityList = this.selectBatchIds(ids);
        for(ReportGroupEntity item : reportGroupEntityList){
            item.setDeleteAt(new Date());
        }
        if(reportGroupEntityList.size()>0){
            this.updateAllColumnBatchById(reportGroupEntityList);
        }
    }

    @Override
    public void deleteByWrapper(Wrapper<ReportGroupEntity> wrapper) {
        List<ReportGroupEntity> reportGroupEntityList = this.selectList(wrapper);
        for(ReportGroupEntity item: reportGroupEntityList){
            item.setDeleteAt(new Date());
        }
        if(reportGroupEntityList.size()>0){
            this.updateAllColumnBatchById(reportGroupEntityList);
        }
    }

    @Override
    public void updatePinAndDataById(ReportGroupEntity reportGroupEntity) {
        reportGroupEntity.setPinyin(PinyinUtil.getPinYin(reportGroupEntity.getName()));
        reportGroupEntity.setUpdateAt(new Date());
        this.updateById(reportGroupEntity);
    }
}