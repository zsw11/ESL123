package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;
import io.apj.modules.report.service.ReportGroupDeptRelaService;
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
import org.springframework.transaction.annotation.Transactional;

@Service("reportGroupService")
public class ReportGroupServiceImpl extends ServiceImpl<ReportGroupDao, ReportGroupEntity>
        implements ReportGroupService {
    @Autowired
    private ReportGroupReportRelaService reportGroupReportRelaService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReportGroupDeptRelaService reportGroupDeptRelaService;

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
                    "UPPER(pinyin) like '%" + params.get("name").toString().toUpperCase() + "%' " + "or UPPER(name) like '%" + params.get("name").toString().toUpperCase() + "%'");
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("keyWord"))) {
            String name = (String) params.get("keyWord");
            name = name.replace(",", "");
            entityWrapper.andNew("name  like '%" + name + "%'" + " or code  like '%" + name + "%'"
                    + " or pinyin  like '%" + name + "%'");
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("createBy"))) {
            entityWrapper.eq("create_By", Integer.parseInt((String) params.get("createBy")));
        }
        if (StringUtils.isNotEmpty((CharSequence) params.get("updateBy"))) {
            entityWrapper.eq("update_by", Integer.parseInt((String) params.get("updateBy")));
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
            entity.setUpdateName(staffService.selectNameByUserId(entity.getUpdateBy()));
            entity.setCreateName(staffService.selectNameByUserId(entity.getCreateBy()));
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
    @Transactional
    public void updateReportGroupDeptRela(List<Integer> ids,Integer reportGroupId) {
        if (ids.size() > 0) {
            List<ReportGroupDeptRelaEntity> entityList1 = reportGroupDeptRelaService.selectList(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("report_group_id", reportGroupId));
            //原本的deptids
            List<Integer> deptIds = new ArrayList<>();
            for (ReportGroupDeptRelaEntity item : entityList1) {
                deptIds.add(item.getDeptId());
            }
            for (Integer i : ids) {
                ReportGroupDeptRelaEntity reportGroupDeptRelaEntity = new ReportGroupDeptRelaEntity();
                reportGroupDeptRelaEntity.setDeptId(i);
//                reportGroupDeptRelaEntity.setCreateBy(getUserId().intValue());
                reportGroupDeptRelaEntity.setReportGroupId(reportGroupId);
                if (reportGroupDeptRelaService.selectList(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("report_group_id", reportGroupId)).size() == 0) {
                    reportGroupDeptRelaService.insert(reportGroupDeptRelaEntity);
                } else {
                    ReportGroupDeptRelaEntity entityList = reportGroupDeptRelaService.selectOne(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("report_group_id", reportGroupId).eq("dept_id", i));
                    if (entityList != null) {
                        reportGroupDeptRelaEntity.setId(entityList.getId());
                        reportGroupDeptRelaService.updateAllColumnById(reportGroupDeptRelaEntity);
                    } else if (!deptIds.contains(i)) {
                        reportGroupDeptRelaService.insert(reportGroupDeptRelaEntity);
                    }
                }
            }
            for (Integer deptId : deptIds) {
                if (!ids.contains(deptId)) {
                    ReportGroupDeptRelaEntity reportGroupDeptRelaEntity1 = reportGroupDeptRelaService.selectOne(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("dept_id", deptId).eq("report_group_id", reportGroupId));
                    reportGroupDeptRelaService.deleteById(reportGroupDeptRelaEntity1);
                }
            }

        } else {
            reportGroupDeptRelaService.delete(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("report_group_id", reportGroupId));
        }

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