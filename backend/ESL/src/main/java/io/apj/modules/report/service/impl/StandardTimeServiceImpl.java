package io.apj.modules.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.*;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.dao.StandardTimeDao;
import io.apj.modules.report.entity.StandardTimeEntity;
import io.apj.modules.report.entity.StandardTimeItemEntity;
import io.apj.modules.report.service.StandardTimeItemService;
import io.apj.modules.report.service.StandardTimeService;
import io.apj.modules.sys.service.SysConfigService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service("standardTimeService")
public class StandardTimeServiceImpl extends ServiceImpl<StandardTimeDao, StandardTimeEntity>
		implements StandardTimeService {
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private StandardTimeItemService standardTimeItemService;
	@Autowired
	private StandardTimeService standardTimeService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private SysConfigService sysConfigService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<StandardTimeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("modelType") != null && params.get("modelType") != "", "model_type",
						(String) params.get("modelType"))
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("unit") != null && params.get("unit") != "", "unit", (String) params.get("unit"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		Page<StandardTimeEntity> page = this.selectPage(new Query<StandardTimeEntity>(params).getPage(), entityWrapper);
		for (StandardTimeEntity entity : page.getRecords()) {
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
		PageUtils page = standardTimeService.queryPage(params);
		List<StandardTimeEntity> items = (List<StandardTimeEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (StandardTimeEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "标准时间表";
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
		List<WorkBookEntity> filteredWorkBooks = workBookService
				.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);

		List<StandardTimeEntity> list = generateStandardTime(filteredWorkBooks);
		for (StandardTimeEntity entity : list) {
			List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks,
					entity.getModelId(), entity.getStlst(), entity.getPhaseId(), entity.getDestinations(), entity.getVersionNumber());
			standardTimeItemService.generateStandardTimeItem(filteredWorkBookIds, entity.getId());
		}
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations =  (String) params.get("destinations");
		String versionNumber = (String) params.get("versionNumber");

		StandardTimeEntity standardTime = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId, destinations, versionNumber);
		Integer standardTimeId=null;
		List<StandardTimeItemEntity> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		if(standardTime!=null) {
			standardTimeId = standardTime.getId();
			list = standardTimeItemService.selectByStandardTimeId(standardTimeId);
			map.put("unit", standardTime.getUnit());
		}
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null){
			map.put("modelName", model.getName());
			map.put("modelType", model.getCode());
		}
		map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
		if(list!=null&&list.size()>0) {
			generateTotalData(list, map);
		}
		String templateFileName = Constant.TEMPLATE_PATH + "standard_time_template.xls";
		String exportFileName = Constant.TEMPLATE_PATH + "标准时间表.xls";
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

	private void generateTotalData(List<StandardTimeItemEntity> list, Map<String, Object> map) {
		BigDecimal htTotal = BigDecimal.ZERO;
		BigDecimal mtTotal = BigDecimal.ZERO;
		BigDecimal mhTotal = BigDecimal.ZERO;
		BigDecimal totalTotal = BigDecimal.ZERO;
		BigDecimal Sample1Total = BigDecimal.ZERO;
		BigDecimal SampleSizeTotal = BigDecimal.ZERO;
		BigDecimal convTotal = BigDecimal.ZERO;

		for (StandardTimeItemEntity entity : list) {
			mtTotal = mtTotal.add(entity.getMostMt());
			mhTotal = mhTotal.add(entity.getMostMh());
			htTotal = htTotal.add(entity.getMostHt());
			totalTotal = totalTotal.add(entity.getTimeTotal());
			Sample1Total = Sample1Total.add(entity.getTimeSample1());
//            SampleSizeTotal = SampleSizeTotal.add(entity.getTimeSampleSize());
			BigDecimal conv = entity.getTimeSample1().divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(60));
			entity.setConv(conv);
			convTotal = convTotal.add(conv);
		}

		map.put("htTotal", htTotal);
		map.put("mtTotal", mtTotal);
		map.put("mhTotal", mhTotal);
		map.put("totalTotal", totalTotal);
		map.put("Sample1Total", Sample1Total);
		map.put("SampleSizeTotal", SampleSizeTotal);
		map.put("convTotal", convTotal);
	}

	private List<StandardTimeEntity> generateStandardTime(List<WorkBookEntity> workBooks) {
		List<StandardTimeEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity workBook : workBooks) {

			Integer phaseId = workBook.getPhaseId();
			Integer modelId = workBook.getModelId();
			String stlst = workBook.getStlst();
			String destinations =  workBook.getDestinations();
			String versionNumber = workBook.getVersionNumber();
			StandardTimeEntity entity = selectOneByPhaseAndModelAndStlst(phaseId, stlst, modelId, destinations, versionNumber);
			if (entity == null) {
				entity = new StandardTimeEntity();
				entity.setModelId(modelId);
				entity.setPhaseId(phaseId);
				entity.setStlst(stlst);
				entity.setDeptId(workBook.getDeptId());
				entity.setDestinations(destinations);
				entity.setVersionNumber(versionNumber);
				entity.setTitle("标准时间表");
				entity.setSheetName("标准时间表");
				entity.setModelType(modelService.selectById(entity.getModelId()).getCode());
				entity.setUnit("1/1000 min");
				insert(entity);
			}
			results.add(entity);
		}
		return results;
	}

	private StandardTimeEntity selectOneByPhaseAndModelAndStlst(Integer phaseId, String stlst, Integer modelId, String destinations, String versionNumber) {
		EntityWrapper<StandardTimeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		StandardTimeEntity entity = selectOne(entityWrapper);
		return entity;
	}

}