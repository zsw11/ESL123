package io.apj.modules.workBook.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.RD;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.report.service.ChangeRecordService;

import io.apj.modules.report.service.StandardTimeService;
import io.apj.modules.report.service.StandardWorkService;
import io.apj.modules.report.service.TotalService;
import io.apj.modules.report.service.StandardTimeService;
import io.apj.modules.report.service.StandardWorkService;
import io.apj.modules.report.service.TimeContactService;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.apj.common.utils.DataUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.workBook.dao.WorkBookDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.WorkOperationsService;

import org.springframework.transaction.annotation.Transactional;

@Service("workBookService")
public class WorkBookServiceImpl extends ServiceImpl<WorkBookDao, WorkBookEntity> implements WorkBookService {
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private WorkOperationsService workOperationService;

	@Autowired
	private StandardWorkService standardWorkService;

	@Autowired
	private StationTimeService stationTimeService;
	@Autowired
	private StandardTimeService standardTimeService;
	@Autowired
	private MostValueService mostValueService;

	@Autowired
	private TimeContactService timeContactService;
	@Autowired
	private CompareService compareService;
	@Autowired
	private TimeContactService timeContactService;
	@Autowired
	private CompareService compareService;
	@Autowired
	private ChangeRecordService changeRecordService;

	@Autowired
	private RevisionHistoryService revisionHistoryService;

	@Autowired
	private TotalService totalService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at")
				.like(params.get("keyWord") != null && params.get("keyWord") != "", "destinations",
						(String) params.get("keyWord"))
				.like(params.get("workName") != null && params.get("workName") != "", "work_name",
						(String) params.get("workName"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("makerId"))) {
			entityWrapper.eq("maker_id", Integer.parseInt((String) params.get("makerId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
			entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("workstationId"))) {
			entityWrapper.eq("workstation_id", Integer.parseInt((String) params.get("workstationId")));
		}

		Page<WorkBookEntity> page = this.selectPage(new Query<WorkBookEntity>(params).getPage(), entityWrapper);
		for (WorkBookEntity entity : page.getRecords()) {
			if (entity.getDeptId() != null) {
				entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());
			}
			if (entity.getModelId() != null) {
				entity.setModelName(modelService.selectById(entity.getModelId()).getName());
			}
			if (entity.getPhaseId() != null) {
				entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
			}
			if (entity.getWorkstationId() != null) {
				entity.setWorkName(workstationService.selectById(entity.getWorkstationId()).getName());
			}

		}
		return new PageUtils(page);

	}

	@Override
	@Transactional
	public ResponseEntity<Object> updateWorkBook(Map<String, Object> workBook) {
		// delete
		int[] ids = (int[]) workBook.get("delete");
		workBookService.deleteBatchIds(Collections.singleton(ids));
		// updata
		WorkBookEntity[] workBookEntities = (WorkBookEntity[]) workBook.get("update");
		List<WorkBookEntity> bookList = new ArrayList<>(workBookEntities.length);
		Collections.addAll(bookList, workBookEntities);
		workBookService.updateBatchById(bookList);
		// new
		WorkBookEntity[] workBookEntities1 = (WorkBookEntity[]) workBook.get("new");
		List<WorkBookEntity> bookListNew = new ArrayList<>(workBookEntities1.length);
		Collections.addAll(bookListNew, workBookEntities1);
		workBookService.insertBatch(bookListNew);

		return RD.ok(workBookEntities1);
	}

	@Override
	@Transactional
	public boolean updateOperation(Map<String, Object> params) {
		ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("delete");
		if (deleteList.size() > 0) {
			workOperationService.deleteBatchIds(deleteList);
		}
		List<Map<String, Object>> updateMaps = (List<Map<String, Object>>) params.get("update");
		for (int i = 0; i < updateMaps.size(); i++) {
			WorkOperationsEntity workOperationsEntity = new WorkOperationsEntity();
			DataUtils.transMap2Bean2(updateMaps.get(i), workOperationsEntity);
			workOperationService.updateById(workOperationsEntity);
		}
		List<Map<String, Object>> newMaps = (List<Map<String, Object>>) params.get("new");
		for (int i = 0; i < newMaps.size(); i++) {
			WorkOperationsEntity workOperationsEntity = new WorkOperationsEntity();
			DataUtils.transMap2Bean2(newMaps.get(i), workOperationsEntity);
			workOperationService.insert(workOperationsEntity);
		}

		return true;
	}

	@Override
	public void createReports(Map<String, Object> params) {
		ArrayList<Integer> reportList = (ArrayList<Integer>) params.get("reports");
		Integer workId = (Integer) params.get("workId");
		WorkBookEntity workBookEntity = selectById(workId);
		reportList.forEach(e->{
			switch (e){
				case 1 :
					break;
				case 2 :
					break;
				case 3 :
					//工位时间报表
					stationTimeService.generateReportData(workBookEntity);
					break;
				case 4 :
					compareService.generateReportData(workBookEntity);
					break;
				case 5 :
					mostValueService.generateReportData(workBookEntity);
					break;
				case 6 :
					//Collection-Revision History表
					revisionHistoryService.generateReportData(workBookEntity);
					break;
				case 7 :
					totalService.generateReportData(workBookEntity);
					break;
				case 8 :
					break;
				case 9 :
					timeContactService.generateReportData(workBookEntity);
					break;
				case 10 :
					break;
				case 11 :
					standardTimeService.generateReportData(workBookEntity);
					break;
				case 12 :
					//标准工数表
					standardWorkService.generateReportData(workBookEntity);
					break;
				case 13 :
					//履历表
					changeRecordService.generateReportData(workBookEntity);
					break;
			}

		});
	}

	@Override
	public WorkBookEntity getLastVersion(Integer modelId, String stlst, Integer phaseId) {
		EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.ne("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
		entityWrapper.orderBy("create_at", false);
		List<WorkBookEntity> list = selectList(entityWrapper);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}


}
