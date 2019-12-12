package io.apj.modules.masterData.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.ReportGroupDao;
import io.apj.modules.masterData.entity.ReportGroupEntity;
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
        entityWrapper.isNull("delete_at").orderBy("update_at").last("desc")
                .like(params.get("id") != null && params.get("id") != "", "id", (String) params.get("id"))
                .like(params.get("name") != null && params.get("name") != "", "name", (String) params.get("name"));
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
}