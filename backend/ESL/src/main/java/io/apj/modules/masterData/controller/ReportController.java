package io.apj.modules.masterData.controller;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ReportGroupReportRelaService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.entity.ReportDeptRelaEntity;
import io.apj.modules.report.service.ApproveService;
import io.apj.modules.report.service.ReportDeptRelaService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 报表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/report")
public class ReportController extends AbstractController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportDeptRelaService reportDeptRelaService;
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:report:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
        PageUtils page = reportService.queryPage(params);
        page.getData();
        return RD.ok(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:report:info")
    public RD info(@PathVariable("id") Integer id) {
        ReportEntity report = reportService.selectById(id);
        return RD.build().put("report", report);
    }

    /**
     * 报表属于哪些报表组并过滤
     *
     * @return
     */
    @RequestMapping("/reportGroup")
    public ResponseEntity<Object> reportGroup(@RequestBody Map<String, Object> data) {
        List<ReportGroupEntity> reportGroupEntityList = reportService.selectReportGroup(data);
        return RD.success(reportGroupEntityList);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:report:create")
    @Transactional
    public RD save(@RequestParam Map<String, Object> map) {
        ReportEntity report = (ReportEntity) map.get("report");
        report.setPinyin(PinyinUtil.getPinYin(report.getName()));
        report.setCreateBy(getUserId().intValue());
        report.setUpdateBy(getUserId().intValue());
        return RD.build().put("code", 200);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @RequiresPermissions("masterData:report:update")
    public RD update(@RequestBody Map<String, Object> map) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setCreateAt(new Date());
        reportEntity.setCreateBy(getUserId().intValue());
        reportEntity.setFormCode((String) map.get("formCode"));
        reportEntity.setRemark((String) map.get("remark"));
        reportEntity.setName((String) map.get("name"));
        reportEntity.setId((Integer) map.get("id"));
        reportEntity.setWorkstationTypeId((Integer)map.get("workstationTypeId"));
        reportService.updatePinAndDataById(reportEntity);
        return RD.build().put("code", 200);

    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:report:delete")
    public RD delete(@RequestBody Integer[] ids) {
        reportService.deleteByIds(Arrays.asList(ids));

        return RD.build().put("code", 200);
    }

    /**
     * 列表查询
     *
     * @return
     */
    @RequestMapping(value = "/listByDeptId")
    public ResponseEntity<Object> ListByDeptId() {
        Integer deptId = getUserDeptId().intValue();
        return reportService.listByDeptId(deptId);
    }

}
