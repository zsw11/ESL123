package io.apj.modules.collection.service.impl;

import com.alibaba.fastjson.JSON;
import io.apj.common.utils.Constant;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.ExcelUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.CompareDao;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.CompareItemEntity;
import io.apj.modules.collection.service.CompareItemService;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.*;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

@Service("compareService")
public class CompareServiceImpl extends ServiceImpl<CompareDao, CompareEntity> implements CompareService {
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private CompareItemService compareItemService;
	@Autowired
	private CompareService compareService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private WorkstationTypeService workstationTypeService;
	@Autowired
	private WorkstationTypeNodeService workstationTypeNodeService;
	@Autowired
	private NodeModelWorkstationRelaService nodeModelWorkstationRelaService;


	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("firstColumnName") != null && params.get("firstColumnName") != "", "first_column_name",
						(String) params.get("firstColumnName"))
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("destinations") != null && params.get("destinations") != "", "destinations",
						(String) params.get("destinations"))
				.like(params.get("lastVersionName") != null && params.get("lastVersionName") != "", "last_version_name",
						(String) params.get("lastVersionName"))
				.like(params.get("currentVersionName") != null && params.get("currentVersionName") != "",
						"current_version_name", (String) params.get("currentVersionName"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		Page<CompareEntity> page = this.selectPage(new Query<CompareEntity>(params).getPage(), entityWrapper);
		for (CompareEntity entity : page.getRecords()) {
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
	public PageUtils selectListTest(Map<String, Object> params) {
		PageUtils page = compareService.queryPage(params);
		List<CompareEntity> items = (List<CompareEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (CompareEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "Collection-Compare表";
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
			List<CompareEntity> list = generateCompare(filteredWorkBooks,reportId);
			for (CompareEntity entity : list) {
				List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks,
						entity.getModelId(), entity.getStlst(), entity.getPhaseId(), entity.getDestinations(), entity.getVersionNumber());
				if(filteredWorkBookIds!=null&&filteredWorkBookIds.size()>0) {
					compareItemService.generateCompareItem(filteredWorkBookIds, entity);
				}
			}
		}
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations =  (String) params.get("destinations");
		String versionNumber = (String) params.get("versionNumber");
		CompareEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId, destinations, versionNumber);
		//CompareEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
		List<CompareItemEntity> list=new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if(entity!=null) {
			Integer entityId = entity.getId();
			map.put("lastVersionName", entity.getLastVersionName());
			map.put("currentVersionName", entity.getCurrentVersionName());
			map.put("firstColumnName", entity.getFirstColumnName());
			list = compareItemService.selectByMostValueId(entityId);
		}
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null){
			map.put("modelName", model.getName());
		}
		PhaseEntity phase = phaseService.selectById(phaseId);
		if(phase!=null) {
			map.put("phaseName", phase.getName());
		}
		map.put("customer", "??");
		map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
		if(list!=null&&list.size()>0) {
			generateTotalData(list, map);
		}
		String templateFileName = Constant.TEMPLATE_PATH + "collection_compare.xls";
		String exportFileName = Constant.TEMPLATE_PATH + "template\\" + entity.getSheetName() + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}
		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
		WriteSheet writeSheet = EasyExcel.writerSheet().build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, writeSheet);
		excelWriter.fill(list, fillConfig, writeSheet);
		int lastRow = 2 + list.size();
		int firstRow = 3;
		if (lastRow > firstRow) {
			excelWriter.merge(firstRow, lastRow, 0, 0);
		}
		Map<Integer, Function<CompareItemEntity, Object>> options = new HashMap<>();
		options.put(1, CompareItemEntity::getSecondColumnName);
		options.put(2, CompareItemEntity::getWorkName);
		ExcelUtils.mergeCell(options, list, excelWriter, firstRow);
		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

	private void generateTotalData(List<CompareItemEntity> list, Map<String, Object> map) {
		BigDecimal lastValueToatal = BigDecimal.ZERO;
		BigDecimal currentValueTotal = BigDecimal.ZERO;
		BigDecimal secondDifferenceTotal = BigDecimal.ZERO;
		BigDecimal minuteDifferenceTotal = BigDecimal.ZERO;

		for (CompareItemEntity entity : list) {
			BigDecimal lastValue = entity.getLastValue();
			BigDecimal currentValue = entity.getCurrentValue();
			currentValueTotal = currentValueTotal.add(currentValue);
			if (lastValue == null) {
				lastValue = BigDecimal.ZERO;
				entity.setLastValue(lastValue);
			}
			lastValueToatal = lastValueToatal.add(lastValue);
			BigDecimal secondDifference = currentValue.subtract(lastValue);
			BigDecimal minuteDifference = secondDifference.divide(new BigDecimal(60), 3, BigDecimal.ROUND_HALF_UP);
			entity.setSecondDifference(secondDifference);
			entity.setMinuteDifference(minuteDifference);
			secondDifferenceTotal = secondDifferenceTotal.add(secondDifference);
			minuteDifferenceTotal = minuteDifferenceTotal.add(minuteDifference);

			Boolean sub = entity.getSub();
			if (sub) {
				entity.setSecondColumnName("SUB");
			} else {
				entity.setSecondColumnName("MAIN");
			}
		}
		map.put("lastValueToatal", lastValueToatal);
		map.put("currentValueTotal", currentValueTotal);
		map.put("secondDifferenceTotal", secondDifferenceTotal);
		map.put("minuteDifferenceTotal", minuteDifferenceTotal);
	}

	private List<CompareEntity> generateCompare(List<WorkBookEntity> workBooks,Integer reportId) {
		List<CompareEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity workBook : workBooks) {
			Integer workstationId = workBook.getWorkstationId();
			Integer phaseId = workBook.getPhaseId();
			Integer modelId = workBook.getModelId();
			String stlst = workBook.getStlst();
			String destinations = workBook.getDestinations();
			String versionNumber = workBook.getVersionNumber();
			CompareEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId, destinations, versionNumber);
			if (entity == null) {

				CompareEntity compareEntity = new CompareEntity();
				compareEntity.setModelId(modelId);
				compareEntity.setPhaseId(phaseId);
				compareEntity.setStlst(stlst);
				compareEntity.setDeptId(workBook.getDeptId());
				compareEntity.setTitle("Compare表");
				compareEntity.setSheetName("Compare表");
				//compareEntity.setDestinations("仕向");
				compareEntity.setFirstColumnName("LFP-PD内作");
				compareEntity.setDestinations(destinations);
				compareEntity.setVersionNumber(versionNumber);
				String workstationType = getWorkstationTypeDetail(reportId ,modelId ,workstationId);
				compareEntity.setWorkstationType(workstationType);

				WorkBookEntity lastWookBook = workBookService.getLastVersion(modelId, stlst, phaseId, destinations, versionNumber);
				if (lastWookBook != null) {
					compareEntity.setLastVersionName(lastWookBook.getVersionNumber());
				}
				compareEntity.setCurrentVersionName(workBook.getVersionNumber());
				insert(compareEntity);
				results.add(compareEntity);
			}else{
				results.add(entity);
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
		return JSON.toJSONString(workstationTypeEntity);
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

	private CompareEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId ,String destinations,String versionNumber) {
		EntityWrapper<CompareEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations",destinations).eq("version_number", versionNumber);
		CompareEntity entity = selectOne(entityWrapper);
		return entity;
	}

}