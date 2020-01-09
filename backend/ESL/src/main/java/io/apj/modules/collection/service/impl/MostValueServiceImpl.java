package io.apj.modules.collection.service.impl;

import io.apj.common.utils.Constant;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.ExcelUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.MostValueDao;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.MostValueItemEntity;
import io.apj.modules.collection.service.MostValueItemService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

@Service("mostValueService")
public class MostValueServiceImpl extends ServiceImpl<MostValueDao, MostValueEntity> implements MostValueService {
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MostValueItemService mostValueItemService;
	@Autowired
	private MostValueService mostValueService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private WorkBookService workBookService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<MostValueEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("firstColumnName") != null && params.get("firstColumnName") != "", "first_column_name",
						(String) params.get("firstColumnName"))
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", "modelId");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", "phaseId");
		}
		Page<MostValueEntity> page = this.selectPage(new Query<MostValueEntity>(params).getPage(), entityWrapper);
		for (MostValueEntity entity : page.getRecords()) {
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
		PageUtils page = mostValueService.queryPage(params);
		List<MostValueEntity> items = (List<MostValueEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (MostValueEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "Collection-MOST Value表";
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
	public void generateReportData(List<Integer> workBookIds) {

		List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
		if(workBooks!=null && workBooks.size()>0 ) {
			List<WorkBookEntity> filteredWorkBooks = workBookService
					.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
			List<MostValueEntity> list = generateMostValue(filteredWorkBooks);
			for (MostValueEntity entity : list) {
				List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks,
						entity.getModelId(), entity.getStlst(), entity.getPhaseId());
				if (filteredWorkBookIds != null && filteredWorkBookIds.size() > 0) {
					mostValueItemService.generateMostValueItem(filteredWorkBookIds, entity.getId());
				}
			}
		}
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();

		MostValueEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
		List<MostValueItemEntity> list=new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if(entity!=null) {
			Integer entityId = entity.getId();
			map.put("firstColumnName", entity.getFirstColumnName());
			list = mostValueItemService.selectByMostValueId(entityId);
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
		map.put("esl", "??");
		map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
		if(list!=null&&list.size()>0) {
			generateTotalData(list, map);
		}
		String templateFileName = Constant.TEMPLATE_PATH + "collection_most_value.xls";
		String exportFileName = Constant.TEMPLATE_PATH + entity.getSheetName() + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}
		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
		WriteSheet writeSheet = EasyExcel.writerSheet().build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, writeSheet);
		excelWriter.fill(list, fillConfig, writeSheet);

//		int lastRow = 5 + list.size();
		int lastRow = 5 + list.size();
		int firstRow = 5;
		if (lastRow != firstRow) {

			excelWriter.merge(firstRow, lastRow, 0, 0);
			excelWriter.merge(firstRow, lastRow, 7, 8);
		}
		Map<Integer, Function<MostValueItemEntity, Object>> options = new HashMap<>();
		options.put(1, MostValueItemEntity::getType);
		options.put(2, MostValueItemEntity::getWorkName);
		options.put(5, MostValueItemEntity::getWorkName);
		options.put(6, MostValueItemEntity::getType);
		ExcelUtils.mergeCell(options, list, excelWriter, firstRow);
		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

	private void generateTotalData(List<MostValueItemEntity> list, Map<String, Object> map) {
		BigDecimal timeStTotal = BigDecimal.ZERO;
		BigDecimal timeTotal = BigDecimal.ZERO;
		Map<String, BigDecimal> totalHMap = new HashMap<>();
		for (MostValueItemEntity entity : list) {
			timeTotal = timeTotal.add(entity.getTotal());
			BigDecimal timeSt = entity.getTotal().multiply(new BigDecimal(60));
			timeTotal = timeTotal.add(timeSt);
			entity.setTimeSt(timeSt);
			String type = entity.getType();
			BigDecimal totalH = totalHMap.get(type);
			if (totalH == null) {
				totalHMap.put(type, timeTotal);
			} else {
				totalHMap.put(type, totalH.add(timeTotal));
			}
		}

		generateTotalHoursForEachOfList(list, totalHMap);

		BigDecimal totalAll = timeTotal.divide(new BigDecimal(60), 3, BigDecimal.ROUND_HALF_UP);
		map.put("timeStTotal", timeStTotal);
		map.put("timeTotal", timeTotal);
		map.put("totalAll", totalAll);

	}

	private void generateTotalHoursForEachOfList(List<MostValueItemEntity> list, Map<String, BigDecimal> totalHMap) {
		for (String key : totalHMap.keySet()) {
			BigDecimal totalH = totalHMap.get(key);
			totalH = totalH.divide(new BigDecimal(60), 3, BigDecimal.ROUND_HALF_UP);
			totalHMap.put(key, totalH);
		}

		for (MostValueItemEntity entity : list) {
			String type = entity.getType();
			BigDecimal totalH = totalHMap.get(type);
			entity.setTotalHours(totalH);
		}
	}

	private List<MostValueEntity> generateMostValue(List<WorkBookEntity> workBooks) {
		List<MostValueEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity workBook : workBooks) {
			Integer phaseId = workBook.getPhaseId();
			Integer modelId = workBook.getModelId();
			String stlst = workBook.getStlst();
			MostValueEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId);
			if (entity == null) {
				MostValueEntity mostValueEntity = new MostValueEntity();
				mostValueEntity.setModelId(modelId);
				mostValueEntity.setPhaseId(phaseId);
				mostValueEntity.setStlst(stlst);
				mostValueEntity.setDeptId(workBook.getDeptId());
				mostValueEntity.setTitle("Most Value");
				mostValueEntity.setSheetName("Most Value");
				mostValueEntity.setFirstColumnName("Most Value");
				insert(mostValueEntity);
				results.add(mostValueEntity);
			}else{
				results.add(entity);
			}
		}
		return results;
	}

	private MostValueEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId) {
		EntityWrapper<MostValueEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
		MostValueEntity entity = selectOne(entityWrapper);
		return entity;
	}

}