package io.apj.modules.report.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.apj.common.utils.Constant;
import io.apj.common.utils.DateUtils;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.report.dao.AssembleProcessListDao;
import io.apj.modules.report.entity.AssembleProcessListEntity;
import io.apj.modules.report.service.ProcessListService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.WorkOperationsService;

@Service("processListService")
public class ProcessListServiceImpl extends ServiceImpl<AssembleProcessListDao,AssembleProcessListEntity>
		implements ProcessListService {
	@Autowired
	private ModelService modelService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private WorkOperationsService workOperationsService;
	


	@Override
	public void generateReportData(List<Integer> workBookIds,Integer reportId,Integer userId) {
		List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
		if(workBooks!=null&&workBooks.size()>0) {
			List<WorkBookEntity> filteredWorkBooks = workBookService
					.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
			generateProcessList(filteredWorkBooks,reportId,userId);
		}
	}
	
	
	private List<AssembleProcessListEntity> generateProcessList(List<WorkBookEntity> workBooks,Integer reportId,Integer userId) {
		List<AssembleProcessListEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity workBook : workBooks) {
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

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations =  (String) params.get("destinations");
		String versionNumber = (String) params.get("versionNumber");

		List<AssembleProcessListEntity> list = new ArrayList<>();		
				
		AssembleProcessListEntity processEntity = null;
		List<WorkBookEntity> workBookList = workBookService.selectListByCondition(modelId, stlst, phaseId, destinations,versionNumber);		
		
		if(workBookList != null && !workBookList.isEmpty()){
		
			BigDecimal totalTime = new BigDecimal("0");
			for(int i =0;i<workBookList.size();i++){
				WorkBookEntity bookEntity = workBookList.get(i);
				Integer bookId = bookEntity.getId();
				Integer workStationId = bookEntity.getWorkstationId();
				WorkstationEntity stationEntity = workstationService.selectById(workStationId);
				processEntity = new AssembleProcessListEntity();
				processEntity.setStationName(stationEntity.getName());
				processEntity.setWorkName(bookEntity.getWorkName());
				String standNum ="";
				BigDecimal secondTime = new BigDecimal("0");
				BigDecimal minTime = new BigDecimal("0");
				
				List<WorkOperationsEntity> workOperationsList  =  workOperationsService.selectListByBookId(bookId);
				if(workOperationsList != null  && !workOperationsList.isEmpty()) {
					for(WorkOperationsEntity workOperationsEntity:workOperationsList){												
						standNum = standNum + workOperationsEntity.getOperation()+";";
						secondTime = secondTime.add(workOperationsEntity.getSecondConvert());						
					}
				}
				secondTime = secondTime.multiply(new BigDecimal("1.06")).setScale(2, RoundingMode.HALF_UP);
				minTime = secondTime.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
				processEntity.setStandardBookNum(standNum);
				processEntity.setManHourSecond(secondTime);
				processEntity.setManHourMin(minTime);
				
				totalTime = totalTime.add(minTime);
				if(i+1<workBookList.size()) {
					if(workBookList.get(i).getWorkstationId() !=  workBookList.get(i+1).getWorkstationId()) {
						processEntity.setManHourMinTotal(totalTime);
						processEntity.setPersonnelAllocation(1);
						totalTime = new BigDecimal("0");
					}
				}else {
					processEntity.setManHourMinTotal(totalTime);
					processEntity.setPersonnelAllocation(1);
					totalTime = new BigDecimal("0");
				}
								
				list.add(processEntity);
			}
		}
		
		Collections.reverse(list);
		
		Map<String, Object> map = new HashMap<>();		
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null){
			map.put("modelName", model.getName());
			map.put("modelType", model.getCode());
		}
		map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
		
		String templateFileName = Constant.TEMPLATE_PATH + "report_process List_template.xls";
		String exportFileName = Constant.TEMPLATE_PATH + "template\\" + "ProcessList.xls";
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

	

	
	private AssembleProcessListEntity selectOneByPhaseAndModelAndStlst(Integer modelId, String stlst, Integer phaseId, String destinations, String versionNumber) {
		EntityWrapper<AssembleProcessListEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		AssembleProcessListEntity entity = selectOne(entityWrapper);
		return entity;
	}

}
