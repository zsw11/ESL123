package io.apj.modules.report.service.impl;

import io.apj.common.utils.*;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.dao.TotalDao;
import io.apj.modules.report.entity.TotalEntity;
import io.apj.modules.report.entity.TotalItemEntity;
import io.apj.modules.report.service.TotalItemService;
import io.apj.modules.report.service.TotalService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

@Service("totalService")
public class TotalServiceImpl extends ServiceImpl<TotalDao, TotalEntity> implements TotalService {
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private TotalService totalService;
	@Autowired
	private ReportService reportService;

	@Autowired
	private TotalItemService totalItemService;
	@Autowired
	private WorkBookService workBookService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) throws ParseException {
		EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("destinations") != null && params.get("destinations") != "", "destinations",
						(String) params.get("destinations"))
				.like(params.get("cotegory") != null && params.get("cotegory") != "", "cotegory",
						(String) params.get("cotegory"))
				.like(params.get("mecha") != null && params.get("mecha") != "", "mecha", (String) params.get("mecha"))
				.like(params.get("rCode") != null && params.get("rCode") != "", "r_rode", (String) params.get("rCode"))
				.like(params.get("allowanceRate") != null && params.get("allowanceRate") != "", "allowanceRate",
						(String) params.get("allowanceRate"))
				.like(params.get("stRev") != null && params.get("stRev") != "", "st_rev", (String) params.get("stRev"))
				.like(params.get("lstRev") != null && params.get("lstRev") != "", "lst_rev",
						(String) params.get("lstRev"));

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
		Page<TotalEntity> page = this.selectPage(new Query<TotalEntity>(params).getPage(), entityWrapper);
		for (TotalEntity entity : page.getRecords()) {
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
	public PageUtils selectListTest(Map<String, Object> params) throws ParseException {
		PageUtils page = totalService.queryPage(params);
		List<TotalEntity> items = (List<TotalEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (TotalEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "Report-Total表";
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
		if(workBooks!=null&&workBooks.size()>0) {
			List<WorkBookEntity> filteredWorkBooks = workBookService
					.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
			List<TotalEntity> list = generateTotal(filteredWorkBooks);
			for (TotalEntity entity : list) {
				List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks, entity.getModelId(), entity.getStlst(), entity.getPhaseId(),entity.getVersionNumber(),entity.getDestinations());
				if(filteredWorkBookIds != null && filteredWorkBookIds.size() > 0) {
					totalItemService.generateTotalItem(filteredWorkBookIds, entity.getId());
				}
			}
		}
	}

	private List<TotalEntity> generateTotal(List<WorkBookEntity> workBooks) {
		List<TotalEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity work : workBooks) {
			EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
			entityWrapper.eq("stlst", work.getStlst()).eq("model_id", work.getModelId()).eq("phase_id",
				work.getPhaseId()).eq("destinations", work.getDestinations()).eq("version_number", work.getVersionNumber());
			TotalEntity total = selectOne(entityWrapper);
			if (total==null) {
				TotalEntity totalEntity = new TotalEntity();
				// TODO 有未设置
				totalEntity.setModelId(work.getModelId());
				totalEntity.setPhaseId(work.getPhaseId());
				totalEntity.setStlst(work.getStlst());
				totalEntity.setDeptId(work.getDeptId());
				//totalEntity.setDestinations(work.getDestinations());
				totalEntity.setVersionNumber(work.getVersionNumber());
				totalEntity.setSheetName("total");
				totalEntity.setDestinations(work.getDestinations());
				insert(totalEntity);
				results.add(totalEntity);
			}else{
				results.add(total);
			}
		}
		return results;
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		// TODO
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations = params.get("destinations").toString();
        String versionNumber = params.get("versionNumber").toString();

		EntityWrapper<TotalEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		TotalEntity totalEntity = selectOne(entityWrapper);
		Integer id = null;
		Map<String, Object> map = new HashMap<>();
		map.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
		List<TotalItemEntity> list = new ArrayList<>();
		String sheetName=null;
		if (totalEntity != null) {
			id = totalEntity.getId();
			map.put("monthResult", totalEntity.getMonthResult());
			map.put("destinations", totalEntity.getDestinations());
			list = totalItemService.getListBySWId(id);
			sheetName=totalEntity.getSheetName();
		}
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null) {
			map.put("modelName", model.getName());
			map.put("modelType", model.getCode());
		}
		int lastRow = 13;
		if(list!=null&&list.size()>0){
			generateTotalData(list, map);
			lastRow +=list.size();
		}

		// TODO 添加调用模版方法及生成目标excel文件方法
		String templateFileName = Constant.TEMPLATE_PATH + "report_total_template.xls";
		String exportFileName = Constant.TEMPLATE_PATH + sheetName + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}

		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
		WriteSheet writeSheet = EasyExcel.writerSheet("report_total_template").build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, writeSheet);
		excelWriter.fill(list, fillConfig, writeSheet);

		int firstRow = 14;
		Map<Integer, Function<TotalItemEntity, Object>> options = new HashMap<>();
		options.put(0, TotalItemEntity::getWorkName);
		ExcelUtils.mergeCell(options, list, excelWriter, firstRow);

		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

	private void generateTotalData(List<TotalItemEntity> list, Map<String, Object> map) {
		BigDecimal htTotal = BigDecimal.ZERO;
		BigDecimal mtTotal = BigDecimal.ZERO;
		BigDecimal mhTotal = BigDecimal.ZERO;
		BigDecimal totalTotal = BigDecimal.ZERO;
		BigDecimal Sample1Total = BigDecimal.ZERO;
		BigDecimal SampleSizeTotal = BigDecimal.ZERO;
		BigDecimal convTotal = BigDecimal.ZERO;

		for (TotalItemEntity entity : list) {

		}

		map.put("htTotal", htTotal);
		map.put("mtTotal", mtTotal);
		map.put("mhTotal", mhTotal);
		map.put("totalTotal", totalTotal);
		map.put("Sample1Total", Sample1Total);
		map.put("SampleSizeTotal", SampleSizeTotal);
		map.put("convTotal", convTotal);
	}

}
