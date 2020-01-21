package io.apj.modules.report.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.Constant;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.masterData.service.WorkstationTypeService;
import io.apj.modules.masterData.service.impl.ReportServiceImpl;
import io.apj.modules.report.dao.AssembleProcessListDao;
import io.apj.modules.report.entity.AssembleProcessListEntity;
import io.apj.modules.report.service.ProcessListService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

@Service("processListService")
public class ProcessListServiceImpl extends ServiceImpl<AssembleProcessListDao,AssembleProcessListEntity>
		implements ProcessListService {
	@Autowired
	private ModelService modelService;
	@Autowired
	private ReportServiceImpl reportServiceimpl;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private WorkstationTypeService workstationTypeService;



	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<AssembleProcessListEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)				
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"));				
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		Page<AssembleProcessListEntity> page = this.selectPage(new Query<AssembleProcessListEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public PageUtils selectListTest(Map<String, Object> params) {
		PageUtils page = queryPage(params);
		List<AssembleProcessListEntity> items = (List<AssembleProcessListEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (AssembleProcessListEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "Report-Process List表";
			data.put("name", name);
			List<ReportGroupEntity> reportGroup = reportServiceimpl.selectReportGroup(data);
			
		}
		return page;
	}

	@Override
	public void generateReportData(List<Integer> workBookIds,Integer reportId,Integer userId) {
		List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
		if(workBooks!=null&&workBooks.size()>0) {
			List<WorkBookEntity> filteredWorkBooks = workBookService
					.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
			List<AssembleProcessListEntity> list = generateStandardTime(filteredWorkBooks,reportId,userId);
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

		EntityWrapper<AssembleProcessListEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		AssembleProcessListEntity entity = selectOne(entityWrapper);
		Integer id = null;
		Map<String, Object> map = new HashMap<>();
		String sheetName=null;
		EntityWrapper<WorkBookEntity> workBookWrapper = new EntityWrapper<>();
		workBookWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations)
						.eq("version_number", versionNumber).isNull("delete_at");
		List<WorkBookEntity> workBookList = workBookService.selectList(workBookWrapper);
		
		if (entity != null) {			
			map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
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

	private List<AssembleProcessListEntity> generateStandardTime(List<WorkBookEntity> workBooks,Integer reportId,Integer userId) {
		List<AssembleProcessListEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity workBook : workBooks) {
			Integer workstationId = workBook.getWorkstationId();
			Integer phaseId = workBook.getPhaseId();
			Integer modelId = workBook.getModelId();
			String stlst = workBook.getStlst();
			String destinations =  workBook.getDestinations();
			String versionNumber = workBook.getVersionNumber();
			AssembleProcessListEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId, destinations, versionNumber);
			if (entity == null) {
				AssembleProcessListEntity processListEntity= new AssembleProcessListEntity();
				processListEntity.setModelId(modelId);

				processListEntity.setPhaseId(phaseId);
				processListEntity.setStlst(stlst);
				processListEntity.setDestinations(destinations);
				processListEntity.setVersionNumber(versionNumber);
				processListEntity.setCreateBy(userId);
				processListEntity.setUpdateBy(userId);
				insert(processListEntity);
				results.add(processListEntity);
			}else{
				results.add(entity);
			}
		}
		return results;
	}

	
	private AssembleProcessListEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId, String destinations, String versionNumber) {
		EntityWrapper<AssembleProcessListEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		AssembleProcessListEntity entity = selectOne(entityWrapper);
		return entity;
	}

}
