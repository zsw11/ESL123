package io.apj.modules.collection.service.impl;

import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.RevisionHistoryDao;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.*;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

@Service("revisionHistoryService")
public class RevisionHistoryServiceImpl extends ServiceImpl<RevisionHistoryDao, RevisionHistoryEntity>
		implements RevisionHistoryService {
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private RevisionHistoryService revisionHistoryService;
	@Autowired
	private ReportService reportService;

	@Autowired
	private RevisionHistoryItemService revisionHistoryItemService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private WorkstationTypeService workstationTypeService;
	@Autowired
	private WorkstationTypeNodeService workstationTypeNodeService;
	@Autowired
	private NodeModelWorkstationRelaService nodeModelWorkstationRelaService;


	@Override
	public PageUtils queryPage(Map<String, Object> params) throws ParseException {
		EntityWrapper<RevisionHistoryEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("factory") != null && params.get("factory") != "", "factory",
						(String) params.get("factory"))
				.like(params.get("destinations") != null && params.get("destinations") != "", "destinations",
						(String) params.get("destinations"));
//                .like(params.get("lastStName") != null && params.get("lastStName") != "", "last_st_name", (String) params.get("lastStName"))
//                .like(params.get("currentStName") != null && params.get("currentStName") != "", "current_st_name", (String) params.get("currentStName"))
//                .like(params.get("lastLstName") != null && params.get("lastLstName") != "", "last_lst_name", (String) params.get("lastLstName"))
//                .like(params.get("currentLstName") != null && params.get("currentLstName") != "", "current_lst_name", (String) params.get("currentLstName"))

		Map<String, Object> map = (Map) JSON.parse((String) params.get("monthResult"));
		if (map != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date start = format.parse((String) map.get("monthResultStart"));
			Date stop = format.parse((String) map.get("monthResultStop"));
			entityWrapper.between("month_result", start, stop);
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		Page<RevisionHistoryEntity> page = this.selectPage(new Query<RevisionHistoryEntity>(params).getPage(),
				entityWrapper);
		for (RevisionHistoryEntity entity : page.getRecords()) {
			if (entity.getPhaseId() != null) {
				entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
			}
			if (entity.getModelId() != null) {
				entity.setModelName(modelService.selectById(entity.getModelId()).getName());
			}

		}

		return new PageUtils(page);
	}

	@Override
	public PageUtils selectListTest(Map<String, Object> params) throws ParseException {
		PageUtils page = revisionHistoryService.queryPage(params);
		List<RevisionHistoryEntity> items = (List<RevisionHistoryEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (RevisionHistoryEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "Collection-Revision History表";
			data.put("name", name);
			List<ReportGroupEntity> reportGroup = reportService.selectReportGroup(data);
			if (!reportGroup.isEmpty()) {
				// 还可以选报表组
				entity.setExist(true);
			} else {
				entity.setExist(false);
			}
		}
		return page;
	}

	@Override
	public void generateReportData(List<Integer> workBookIds,Integer reportId) {
		List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
		if(workBooks!=null&&workBooks.size()>0) {
			List<WorkBookEntity> filteredWorkBooks = workBookService
					.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
			List<RevisionHistoryEntity> list = generateRevisionHistory(filteredWorkBooks,reportId);
//			for(RevisionHistoryEntity entity : list){
//				List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks,
//						entity.getModelId(), entity.getStlst(), entity.getPhaseId(), entity.getDestinations(), entity.getVersionNumber());
//				if (filteredWorkBookIds != null && filteredWorkBookIds.size() > 0) {
//					revisionHistoryItemService.generateRevisionHistoryItem(filteredWorkBookIds, entity.getId());
//				}
//			}
		}
	}

	private List<RevisionHistoryEntity> generateRevisionHistory(List<WorkBookEntity> workBooks,Integer reportId) {
		List<RevisionHistoryEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity work : workBooks) {
			Integer workstationId = work.getWorkstationId();
			Integer modelId = work.getModelId();
			EntityWrapper<RevisionHistoryEntity> entityWrapper = new EntityWrapper<>();
			entityWrapper.eq("stlst", work.getStlst()).eq("model_id", work.getModelId()).eq("phase_id",
			work.getPhaseId()).eq("destinations", work.getDestinations()).eq("version_number", work.getVersionNumber());
			RevisionHistoryEntity revisionHistory= selectOne(entityWrapper);
			if (revisionHistory==null) {
				RevisionHistoryEntity revisionHistoryEntity = new RevisionHistoryEntity();
				// TODO 有未设置
				revisionHistoryEntity.setModelId(work.getModelId());
				revisionHistoryEntity.setPhaseId(work.getPhaseId());
				revisionHistoryEntity.setStlst(work.getStlst());
				revisionHistoryEntity.setDeptId(work.getDeptId());
				//revisionHistoryEntity.setDestinations(work.getDestinations());
				revisionHistoryEntity.setVersionNumber(work.getVersionNumber());
				String workstationType = getWorkstationTypeDetail(reportId ,modelId ,workstationId);
				revisionHistoryEntity.setWorkstationType(workstationType);
				WorkstationEntity workstation = workstationService.selectById(work.getWorkstationId());
				revisionHistoryEntity.setSheetName(workstation.getName() + " " + work.getWorkName());
				revisionHistoryEntity.setDestinations(work.getDestinations());
				insert(revisionHistoryEntity);
				results.add(revisionHistoryEntity);
			}else{
				results.add(revisionHistory);
			}
		}
		return results;
	}

	List<WorkstationTypeNodeEntity> workstationTypeNodeEntityList = new ArrayList<>();
	private String getWorkstationTypeDetail(Integer reportId ,Integer fromModelId ,Integer fromWorkstationId){
		workstationTypeNodeEntityList.clear();
		//查询工位结构信息
		ReportEntity reportEntity = reportService.selectById(reportId);
		WorkstationTypeEntity workstationTypeEntity = workstationTypeService.selectById(reportEntity.getWorkstationTypeId());
		List<WorkstationTypeNodeEntity> workstationTypeNodeEntityList = workstationTypeNodeService.selectList(new EntityWrapper<WorkstationTypeNodeEntity>()
				.eq("workstation_type_id",workstationTypeEntity.getId()));
		if(workstationTypeNodeEntityList != null && workstationTypeNodeEntityList.size() > 0){
			for(WorkstationTypeNodeEntity workstationTypeNodeEntity : workstationTypeNodeEntityList){
				if(workstationTypeNodeEntity.getIfWorkstation()){
					List<NodeModelWorkstationRelaEntity> nodeModelWorkstationRelaEntityList = nodeModelWorkstationRelaService.selectList(
							new EntityWrapper<NodeModelWorkstationRelaEntity>().eq("workstation_type_node_id",workstationTypeNodeEntity.getId()));
					if(nodeModelWorkstationRelaEntityList != null && nodeModelWorkstationRelaEntityList.size() > 0){
						for(NodeModelWorkstationRelaEntity nodeModelWorkstationRelaEntity : nodeModelWorkstationRelaEntityList){
							Integer modelId = nodeModelWorkstationRelaEntity.getModelId();
							String workstationIds = nodeModelWorkstationRelaEntity.getWorkstationIds();
							String[] workstationIdArr = workstationIds.split(",");
							for(String workstationId : workstationIdArr){
								if(fromModelId == modelId && fromWorkstationId == Integer.valueOf(workstationId)){
									workstationTypeNodeEntity.setModelWorkstation(nodeModelWorkstationRelaEntity);
									workstationTypeNodeEntityList.add(workstationTypeNodeEntity);
									Integer parentId = workstationTypeNodeEntity.getParentId();
									getParent(parentId);
									break;
								}
							}
						}
					}
				}
			}
			if(workstationTypeNodeEntityList.size() > 0){
				workstationTypeEntity.setWorkstationTypeNodeList(workstationTypeNodeEntityList);
			}
		}
		return workstationTypeEntity.toString();
	}
	private void getParent(Integer parentId){
		if(parentId != null) {
			WorkstationTypeNodeEntity workstationTypeNodeEntity = workstationTypeNodeService.selectById(parentId);
			if (workstationTypeNodeEntity != null) {
				workstationTypeNodeEntityList.add(workstationTypeNodeEntity);
				Integer parentIdGet = workstationTypeNodeEntity.getParentId();
				if (parentIdGet != null) {
					getParent(parentIdGet);
				}
			}
		}
	}

	@Override
	public void updateEntity(RevisionHistoryEntity revisionHistory) {
		revisionHistoryItemService.insertOrUpdateBatch(revisionHistory.getItems());
		if (revisionHistory.getModelId() == 0)
			revisionHistory.setModelId(null);
		revisionHistory.setSheetName("sheet");
		updateById(revisionHistory);
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations = params.get("destinations").toString();
        String versionNumber = params.get("versionNumber").toString();

		EntityWrapper<RevisionHistoryEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		RevisionHistoryEntity revisionHistoryEntity = selectOne(entityWrapper);
		Integer id = null;
		Map<String, Object> map = new HashMap<>();
		List<RevisionHistoryItemEntity> list=new ArrayList<>();
		String sheetName=null;
		if (revisionHistoryEntity != null) {
			id = revisionHistoryEntity.getId();
			map.put("factory", revisionHistoryEntity.getFactory());
			map.put("monthResult", revisionHistoryEntity.getMonthResult());
			map.put("revNo", revisionHistoryEntity.getRevNo());
			map.put("destinations", revisionHistoryEntity.getDestinations());
			list= revisionHistoryItemService.getListBySWId(id);
			sheetName=revisionHistoryEntity.getSheetName();
		}
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null) {
			map.put("modelName", model.getName());
			map.put("modelType", model.getCode());
		}
		// TODO 添加调用模版方法及生成目标excel文件方法

		String templateFileName = Constant.TEMPLATE_PATH + "collection_revision_history_template.xls";
		String exportFileName = Constant.TEMPLATE_PATH + "template\\" + sheetName + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}
		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();

		WriteSheet writeSheet = EasyExcel.writerSheet().build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, writeSheet);
		excelWriter.fill(list, fillConfig, writeSheet);

		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

}
