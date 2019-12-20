package io.apj.modules.workBook.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.*;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.report.service.*;
import io.apj.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.modules.workBook.dao.WorkBookDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.WorkOperationsService;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	private ChangeRecordService changeRecordService;
	@Autowired
	private RevisionHistoryService revisionHistoryService;
	@Autowired
	private TotalService totalService;
	@Autowired
	private WorkOperationsService workOperationsService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) throws ParseException {
		EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("keyWord") != null && params.get("keyWord") != "", "destinations",
						(String) params.get("keyWord"))
				.like(params.get("workName") != null && params.get("workName") != "", "work_name",
						(String) params.get("workName"))
				.like(params.get("destinations") != null && params.get("destinations") != "", "destinations",
						(String) params.get("destinations"));

		Map<String,Object> map = (Map) JSON.parse((String) params.get("makedAt"));
		if(map!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date start = format.parse((String) map.get("createAtStart"));
			Date stop = format.parse((String) map.get("createAtStop"));
			entityWrapper.between("maked_at",start,stop);
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
				entity.setWorkstationName(workstationService.selectById(entity.getWorkstationId()).getName());
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
	public List<Integer> updateOperation(Map<String, Object> params) {
		// 新增的手顺ID列表
		List<Integer> newIDList = new ArrayList<>();
		String type = (String) params.get("type");
		Integer seqNumber = (Integer) params.get("seqNumber") + 1;
		Integer workBookId = (Integer) params.get("workBookId");
		EntityWrapper<WorkOperationsEntity> workOperationsWrapper = new EntityWrapper<>();
		if (type.equals("delete")) {
			ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("list");
			workOperationService.deleteBatchIds(deleteList);
			Integer deleteNumber = deleteList.size();
			workOperationsWrapper.eq("work_book_id", workBookId).isNull("delete_at").ge("seq_number", seqNumber);
			List<WorkOperationsEntity> workOperationsEntityList = workOperationService
					.selectList(workOperationsWrapper);
			for (WorkOperationsEntity item : workOperationsEntityList) {
				item.setSeqNumber(item.getSeqNumber() - deleteNumber);
			}
			workOperationService.updateBatchById(workOperationsEntityList);
		} else {
			List<WorkOperationsEntity> workOperationsList = new ArrayList<>();
			List<Map<String, Object>> operateList = (List<Map<String, Object>>) params.get("list");
			for (int i = 0; i < operateList.size(); i++) {
				WorkOperationsEntity workOperationsEntity = new WorkOperationsEntity();
				DataUtils.transMap2Bean2(operateList.get(i), workOperationsEntity);
				workOperationsList.add(workOperationsEntity);
			}
			if (type.equals("update")) {
				workOperationService.updateBatchById(workOperationsList);
			} else {
				// 更新seqNumber大于所要新增的最大值的所有记录
				workOperationsWrapper.eq("work_book_id", workBookId).isNull("delete_at").ge("seq_number", seqNumber);
				List<WorkOperationsEntity> workOperationsEntityList = workOperationService
						.selectList(workOperationsWrapper);
				Integer operateNumber = operateList.size();
				for (WorkOperationsEntity item : workOperationsEntityList) {
					item.setSeqNumber(item.getSeqNumber() + operateNumber);
				}
				workOperationService.insertOrUpdateBatch(workOperationsEntityList);
				// 批量插入新增的手顺
				workOperationService.insertBatch(workOperationsList);
				for (WorkOperationsEntity item : workOperationsList) {
					newIDList.add(item.getId());
				}
			}

		}

		return newIDList;
	}

	@Override
	public void createReports(Map<String, Object> params) {
		ArrayList<Integer> reportList = (ArrayList<Integer>) params.get("reports");
		Integer workId = (Integer) params.get("workId");
		WorkBookEntity workBookEntity = selectById(workId);
		reportList.forEach(e -> {
			switch (e) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				// 工位时间报表
				stationTimeService.generateReportData(workBookEntity);
				break;
			case 4:
				compareService.generateReportData(workBookEntity);
				break;
			case 5:
				mostValueService.generateReportData(workBookEntity);
				break;
			case 6:
				// Collection-Revision History表
				revisionHistoryService.generateReportData(workBookEntity);
				break;
			case 7:
				totalService.generateReportData(workBookEntity);
				break;
			case 8:
				break;
			case 9:
				timeContactService.generateReportData(workBookEntity);
				break;
			case 10:
				break;
			case 11:
				standardTimeService.generateReportData(workBookEntity);
				break;
			case 12:
				// 标准工数表
				standardWorkService.generateReportData(workBookEntity);
				break;
			case 13:
				// 履历表
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

	@Override
	public WorkBookEntity detailWithOperations(Integer id) {
		WorkBookEntity workBook = this.selectById(id);
		EntityWrapper<WorkOperationsEntity> workOperationsWrapper = new EntityWrapper<>();
		workOperationsWrapper.eq("work_book_id", id).isNull("delete_at");
		List<WorkOperationsEntity> workOperationsList = workOperationService.selectList(workOperationsWrapper);
		workBook.setWorkOperationsList(workOperationsList);
		EntityWrapper<WorkBookEntity> workBookWrapper = new EntityWrapper<>();
		workBookWrapper.isNull("delete_at").eq("stlst", workBook.getStlst()).eq("model_id", workBook.getModelId())
				.eq("destinations", workBook.getDestinations()).eq("version_number", workBook.getVersionNumber())
				.eq("phase_id", workBook.getPhaseId());
		List<WorkBookEntity> otherWorkBookEntities = this.selectList(workBookWrapper);
		workBook.setOtherWorkBookEnties(otherWorkBookEntities);
		return workBook;
	}

	@Override
	@Transactional
	public void updateAll(Map<String, Object> params) {
		// 更新主表
		WorkBookEntity workBook = new WorkBookEntity();
		DataUtils.transMap2Bean2((Map<String, Object>) params.get("workBook"), workBook);

		// 删除原有子表
		workOperationService.deletebyWrapper(
				new EntityWrapper<WorkOperationsEntity>().eq("work_book_id", workBook.getId()).isNull("delete_at"));

		// 遍历子表数组，批量插入
		List<WorkOperationsEntity> workOperationsList = new ArrayList<>();
		List<Map<String, Object>> workOperationsMapList = (List<Map<String, Object>>) params.get("workOperations");
		for (int i = 0; i < workOperationsMapList.size(); i++) {
			WorkOperationsEntity workOperations = new WorkOperationsEntity();
			if (workOperationsMapList.get(i).get("frequency") != "" && workOperationsMapList.get(i).get("frequency") != null) {
				workOperationsMapList.get(i).put("frequency",
						Integer.parseInt(workOperationsMapList.get(i).get("frequency").toString()));
			}
			DataUtils.transMap2Bean2(workOperationsMapList.get(i), workOperations);
			workOperations.setWorkBookId(workBook.getId());
			workOperationsList.add(workOperations);
		}
		workOperationService.insertBatch(workOperationsList);
	}

	@Override
	@Transactional
	public WorkBookEntity copyWorkBook(WorkBookEntity workBook, Integer workBookId) {
		workBookService.insert(workBook);
		int newId = workBook.getId();
		List<WorkOperationsEntity> workOperationsEntityList =  workOperationsService.selectList(new EntityWrapper<WorkOperationsEntity>().eq("work_book_id",workBookId));
		for(WorkOperationsEntity item : workOperationsEntityList){
			item.setWorkBookId(newId);
		}
		if(!workOperationsEntityList.isEmpty()){
			workOperationService.insertBatch(workOperationsEntityList);
		}
		return workBook;
	}

	@Override
	@Transactional
	public void deleteByWrapper(Wrapper wrapper) {
		List<WorkBookEntity> workBookList = this.selectList(wrapper);
		for (WorkBookEntity item : workBookList) {
			item.setDeleteAt(new Date());
		}
		this.updateAllColumnBatchById(workBookList);
	}

	@Override
	public void download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer)params.get("phaseId");
		Integer modelId = (Integer)params.get("modelId");
		String stlst = params.get("stlst").toString();

		List<WorkBookEntity> workBookEntities = selectByPhaseAndModelAndStlst(phaseId, stlst, modelId);
		List<String> workBookFilePaths = workOperationService.getWorkBookFilePaths(workBookEntities);
		String fileName = "test";
		ExportExcelUtils.exportExcel(workBookFilePaths, response, fileName);
	}

	private List<WorkBookEntity> selectByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
		EntityWrapper<WorkBookEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
		return selectList(wrapper);
	}
}
