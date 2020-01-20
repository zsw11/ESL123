package io.apj.modules.report.service.impl;

import com.alibaba.fastjson.JSON;
import io.apj.common.utils.*;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.*;
import io.apj.modules.masterData.service.impl.ReportServiceImpl;
import io.apj.modules.report.dao.TimeContactDao;
import io.apj.modules.report.entity.TimeContactEntity;
import io.apj.modules.report.service.TimeContactService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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

@Service("timeContactService")
public class TimeContactServiceImpl extends ServiceImpl<TimeContactDao, TimeContactEntity>
		implements TimeContactService {
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private TimeContactService timeContactService;
	@Autowired
	private ReportServiceImpl reportServiceimpl;
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
	@Autowired
	private ReportService reportService;


	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
				.like(params.get("operationStandardNo") != null && params.get("operationStandardNo") != "",
						"operation_standard_no", (String) params.get("operationStandardNo"))
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("stage") != null && params.get("stage") != "", "stage", (String) params.get("stage"))
				.like(params.get("publishType") != null && params.get("publishType") != "", "publish_type",
						(String) params.get("publishType"))
				.like(params.get("operationInstruction") != null && params.get("operationInstruction") != "",
						"operation_instruction", (String) params.get("operationInstruction"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		Page<TimeContactEntity> page = this.selectPage(new Query<TimeContactEntity>(params).getPage(), entityWrapper);
		for (TimeContactEntity entity : page.getRecords()) {
			if (entity.getModelId() != null) {
				entity.setModelName(modelService.selectById(entity.getModelId()).getName());
			}
			if (entity.getPhaseId() != null) {
				entity.setPhaseName(phaseService.selectById(entity.getPhaseId()).getName());
			}
		}

		return new PageUtils(page);
	}

	@Override
	public PageUtils selectListTest(Map<String, Object> params) {
		PageUtils page = timeContactService.queryPage(params);
		List<TimeContactEntity> items = (List<TimeContactEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (TimeContactEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "Report-时间联络表";
			data.put("name", name);
			List<ReportGroupEntity> reportGroup = reportServiceimpl.selectReportGroup(data);
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
			List<TimeContactEntity> list = generateStandardTime(filteredWorkBooks,reportId);
		}
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		// TODo
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations = params.get("destinations").toString();
        String versionNumber = params.get("versionNumber").toString();

		EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		TimeContactEntity timeContactEntity = selectOne(entityWrapper);
		Integer id = null;
		Map<String, Object> map = new HashMap<>();
		String sheetName=null;
		EntityWrapper<WorkBookEntity> workBookWrapper = new EntityWrapper<>();
		workBookWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations)
						.eq("version_number", versionNumber).isNull("delete_at");
		List<WorkBookEntity> workBookList = workBookService.selectList(workBookWrapper);
		
		if (timeContactEntity != null) {
			map.put("revNo", timeContactEntity.getRevNo());
			map.put("allCountSub", timeContactEntity.getAllCountSub());
			map.put("allCountMain", timeContactEntity.getAllCountMain());
			map.put("allCountPrinting", timeContactEntity.getAllCountPrinting());
			map.put("allCountExternalInspection", timeContactEntity.getAllCountExternalInspection());
			map.put("allCountPacking", timeContactEntity.getAllCountPacking());
			map.put("towingLastVersionSub", timeContactEntity.getTowingLastVersionSub());
			map.put("towingLastVersionMain", timeContactEntity.getTowingLastVersionMain());
			map.put("towingLastVersionPrinting", timeContactEntity.getTowingLastVersionPrinting());
			map.put("towingLastVersionExternalInspection", timeContactEntity.getTowingLastVersionExternalInspection());
			map.put("towingLastVersionPacking", timeContactEntity.getTowingLastVersionPacking());
			map.put("operationStandardNo", timeContactEntity.getOperationStandardNo());
			map.put("operationInstruction", timeContactEntity.getOperationInstruction());
			map.put("exceptionOperation", timeContactEntity.getExceptionOperation());
			map.put("remarkVersionCopmare", timeContactEntity.getTowingLastVersionPrinting());
			map.put("remarkSub", timeContactEntity.getTowingLastVersionExternalInspection());
			map.put("remarkMain", timeContactEntity.getTowingLastVersionPacking());
			map.put("remarkPrinting", timeContactEntity.getTowingLastVersionPrinting());
			map.put("remarkExternalInspection", timeContactEntity.getTowingLastVersionExternalInspection());
			map.put("remarkPacking", timeContactEntity.getTowingLastVersionPacking());
			map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
			sheetName=timeContactEntity.getSheetName();
		}
		
		//时间联络表因为是其它子数据的汇总报表 生成报表的时候并没有将子数据进行汇总计算并保存 所以上面直接从timeContactEntity取值是不对的
		if(workBookList != null && workBookList.size() > 0){
			BigDecimal allCountSubSec = BigDecimal.ZERO;
			BigDecimal allCountMainSec = BigDecimal.ZERO;
			BigDecimal allCountPrintingSec = BigDecimal.ZERO;
			BigDecimal allCountExternalInspectionSec = BigDecimal.ZERO;
			BigDecimal allCountPackingSec = BigDecimal.ZERO;
			BigDecimal turnTimeToHourSec = BigDecimal.valueOf(3600);
			BigDecimal secRange = BigDecimal.valueOf(1.06);
			
			for(WorkBookEntity workbook : workBookList){
				if(workbook.getWorkstationId() == null){
					continue;
				}
				WorkstationEntity workstation = workstationService.selectById(workbook.getWorkstationId());
				if(workstation == null){
					continue;
				}
				WorkstationTypeEntity workstationType = workstationTypeService.selectById(workstation.getWorkstationTypeId());
				if(workstationType == null || workstationType.getName() == null){
					continue;
				}
				switch(workstationType.getName()){
					case Constant.WORKSTATION_TYPE_SUB:
						allCountSubSec = allCountSubSec.add(workbook.getSecondConvert());
						break;
					case Constant.WORKSTATION_TYPE_MAIN:
						allCountMainSec = allCountMainSec.add(workbook.getSecondConvert());
						break;
					case Constant.WORKSTATION_TYPE_PACKING:
						allCountPackingSec = allCountPackingSec.add(workbook.getSecondConvert());
						break;
					case Constant.WORKSTATION_TYPE_PRINT:
						allCountPrintingSec = allCountPrintingSec.add(workbook.getSecondConvert());
						break;
					case Constant.WORKSTATION_TYPE_EXTERNAL:
						allCountExternalInspectionSec = allCountExternalInspectionSec.add(workbook.getSecondConvert());
						break;
					default:
						break;
				}
					
			}
			//根据Sec./conv总和乘以系数1.06 再转换成小时 四舍五入 保留两位小数
			map.put("allCountSub", allCountSubSec.multiply(secRange).divide(turnTimeToHourSec).setScale(2,BigDecimal.ROUND_HALF_UP));
			map.put("allCountMain", allCountMainSec.multiply(secRange).divide(turnTimeToHourSec).setScale(2,BigDecimal.ROUND_HALF_UP));
			map.put("allCountPrinting", allCountPrintingSec.multiply(secRange).divide(turnTimeToHourSec).setScale(2,BigDecimal.ROUND_HALF_UP));
			map.put("allCountExternalInspection", allCountExternalInspectionSec.multiply(secRange).divide(turnTimeToHourSec).setScale(2,BigDecimal.ROUND_HALF_UP));
			map.put("allCountPacking", allCountPackingSec.multiply(secRange).divide(turnTimeToHourSec).setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null) {
			map.put("modelName", model.getName());
			map.put("modelType", model.getCode());
		}

		// TODO 添加调用模版方法及生成目标excel文件方法
		String templateFileName = Constant.TEMPLATE_PATH + "report_time_contact_template.xls";
		String exportFileName = Constant.TEMPLATE_PATH + "template\\" + sheetName + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}
		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
		WriteSheet writeSheet = EasyExcel.writerSheet().build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, writeSheet);
		// excelWriter.fill(list, fillConfig, writeSheet); 变更内容
		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

	private List<TimeContactEntity> generateStandardTime(List<WorkBookEntity> workBooks,Integer reportId) {
		List<TimeContactEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity workBook : workBooks) {
			Integer workstationId = workBook.getWorkstationId();
			Integer phaseId = workBook.getPhaseId();
			Integer modelId = workBook.getModelId();
			String stlst = workBook.getStlst();
			String destinations =  workBook.getDestinations();
			String versionNumber = workBook.getVersionNumber();
			TimeContactEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId, destinations, versionNumber);
			if (entity == null) {
				TimeContactEntity timeContactEntity=new TimeContactEntity();
				timeContactEntity = new TimeContactEntity();
				timeContactEntity.setDeptId(workBook.getDeptId());
				timeContactEntity.setTitle("时间联络表");
				timeContactEntity.setSheetName("时间联络表");
				timeContactEntity.setModelId(modelId);

				timeContactEntity.setPhaseId(phaseId);
				timeContactEntity.setStlst(stlst);
				timeContactEntity.setDestinations(destinations);
				timeContactEntity.setVersionNumber(versionNumber);
				String workstationType = getWorkstationTypeDetail(reportId ,modelId ,workstationId);
				timeContactEntity.setWorkstationType(workstationType);
				insert(timeContactEntity);
				results.add(timeContactEntity);
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

	private TimeContactEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId, String destinations, String versionNumber) {
		EntityWrapper<TimeContactEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		TimeContactEntity entity = selectOne(entityWrapper);
		return entity;
	}

}
