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
		List<SysDeptEntity> deptEntityList = new LinkedList<>();
		List<ReportDeptRelaEntity> reportDeptRelaEntityList = reportDeptRelaService.selectList(new EntityWrapper<ReportDeptRelaEntity>().eq("report_id", id));
		for(ReportDeptRelaEntity item : reportDeptRelaEntityList){
			SysDeptEntity deptEntity = sysDeptService.selectById(item.getDeptId());
			deptEntityList.add(deptEntity);
		}
		Map<String,Object> data = new HashMap<>();
		data.put("report",report);
		data.put("deptEntityList",deptEntityList);
//		report.setDeptEntityList(deptEntityList);
		return RD.build().put("data", data);
	}
	/**
	 * 报表属于哪些报表组并过滤
	 * @return
	 */
	@RequestMapping("/reportGroup")
	public ResponseEntity<Object> reportGroup(@RequestBody  Map<String,Object> data) {
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
		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:report:update")
	public RD update(@RequestBody ReportEntity reportEntity) {
		reportService.updatePinAndDataById(reportEntity);
		return RD.build().put("code", 200);
	}
//	@RequestMapping("/update")
//	@Transactional
//	@RequiresPermissions("masterData:report:update")
//	public RD update(@RequestBody Map<String, Object> map) {
//        ReportEntity report = (ReportEntity) map.get("report");
//		reportService.updatePinAndDataById(report);
//        List<SysDeptEntity> sysDeptEntityList = (List<SysDeptEntity>) map.get("deptEntityList");
//        List<Integer> deptIds = new ArrayList<>();
//		sysDeptEntityList.forEach(item->{
//			deptIds.add(item.getId().intValue());
//		});
//        Integer reportId = report.getId();
//        if(deptIds.size()>0){
//            for(Integer i : deptIds){
//                ReportDeptRelaEntity reportDeptRelaEntity = new ReportDeptRelaEntity();
//                reportDeptRelaEntity.setDeptId(i);
//                reportDeptRelaEntity.setCreateBy(getUserId().intValue());
//                reportDeptRelaEntity.setReportId(reportId);
//                reportDeptRelaService.insert(reportDeptRelaEntity);
//            }
//        }
//		return RD.build().put("code", 200);
//	}

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
	 * @return
	 */
	@RequestMapping(value = "/listByDeptId")
	public ResponseEntity<Object> ListByDeptId(){
		Integer deptId=getUserDeptId().intValue();
		return reportService.listByDeptId(deptId);
	}

}
