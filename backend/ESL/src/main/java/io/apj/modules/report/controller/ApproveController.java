package io.apj.modules.report.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.dao.ModelDao;
import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import lombok.experimental.PackagePrivate;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;

/**
 * 报表审批表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:56
 */
@RestController
@RequestMapping("/api/v1/approve")
public class ApproveController {
	@Autowired
	private ApproveService approveService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private ReportGroupService reportGroupService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ApproveHistoryService approveHistoryService;
	@Autowired
	private ReportGroupReportRelaService reportGroupReportRelaService;
	@Autowired
	private TotalService totalService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private StationTimeService stationTimeService;
	@Autowired
	private CompareService compareService;
	@Autowired
	private MostValueService mostValueService;
	@Autowired
	private RevisionHistoryService revisionHistoryService;
	@Autowired
	private TimeContactService timeContactService;
	@Autowired
	private StandardTimeService standardTimeService;
	@Autowired
	private StandardWorkService standardWorkService;
	@Autowired
	private ChangeRecordService changeRecordService;


	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:approve:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = approveService.queryPage(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:approve:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		ApproveEntity approve = approveService.selectById(id);
		if(approve.getModelId()!=null){
			approve.setModelName(modelService.selectById(approve.getModelId()).getName());
		}
		if(approve.getPhaseId()!=null){
			approve.setPhaseName(phaseService.selectById(approve.getPhaseId()).getName());
		}
		if(approve.getReportGroupId()!=null){
			approve.setReportGroupName(reportGroupService.selectById(approve.getReportGroupId()).getName());
		}
		//报表审批详情，符合三个字段所在的报表组里的报表
		List<ReportEntity> reportEntity = reportService.selectApproveList(id);
		Map<String,Object> page = new HashMap<>();
		page.put("data",reportEntity);
        page.put("approve",approve);
		return RD.success(page);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:approve:create")
	public ResponseEntity<Object> save(@RequestBody ApproveEntity approve) {
		//判断所选报表组里的报表是否生成了
		int reportGroupId = approve.getReportGroupId();
		List<ReportEntity> reportEntityList = reportGroupReportRelaService.selectReportNameByReportGroupId(reportGroupId);
		List<Object> reportList = new ArrayList<>();
		for(ReportEntity item : reportEntityList){
			ReportEntity reportEntity= reportService.selectById(item.getId());
			String reportName = reportEntity.getEname();
			//通过Ename去所有报表里查是否生产了表
			switch (reportName){
				case "report_total":
					int total =  totalService.selectCount(null);
					if(total<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "work_book":
					int workBook =  workBookService.selectCount(null);
					if(workBook<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "collection_station_time":
					int station =  stationTimeService.selectCount(null);
					if(station<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "collection_compare":
					int compare =  compareService.selectCount(null);
					if(compare<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "collection_most_value":
					int mostValue =  mostValueService.selectCount(null);
					if(mostValue<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "collection_revision_history":
					int revision =  revisionHistoryService.selectCount(null);
					if(revision<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "report_time_contact":
					int timeContact =  timeContactService.selectCount(null);
					if(timeContact<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "report_standard_time":
					int standardTime =  standardTimeService.selectCount(null);
					if(standardTime<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "report_standard_work":
					int standardWork =  standardWorkService.selectCount(null);
					if(standardWork<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
				case "report_change_record":
					int changRecord =  changeRecordService.selectCount(null);
					if(changRecord<0){
						//提醒生成
						reportList.add(reportName);
					}
					break;
			}
		}
		if(!reportList.isEmpty()){
			return (ResponseEntity<Object>) reportList;
		}else {
			approveService.insert(approve);
			return RD.ok(approve);
		}

	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:approve:update")
	public ResponseEntity<Object> update(@RequestBody ApproveEntity approve){
		approveService.updateById(approve);
		approveHistoryService.insertApproveHisttory(approve);

		return RD.success(approve);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:approve:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		approveService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
