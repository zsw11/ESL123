package io.apj.modules.report.service.impl;

import io.apj.common.utils.Constant;
import io.apj.common.utils.ExportExcelUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.report.dao.ReportManMachineCombinationDao;
import io.apj.modules.report.entity.AshcraftTableEntity;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;
import io.apj.modules.report.service.AshcraftTableService;
import io.apj.modules.report.service.ReportManMachineCombinationService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("reportManMachineCombinationService")
public class ReportManMachineCombinationServiceImpl
		extends ServiceImpl<ReportManMachineCombinationDao, ReportManMachineCombinationEntity>
		implements ReportManMachineCombinationService {

	@Autowired
	private ModelService modelService;

	@Value("classpath:static/ashcraftTable.json")
	private Resource ashcraftTable;

	@Value("classpath:static/FilterData.json")
	private Resource filterData;

	@Autowired
	private AshcraftTableService ashcraftTableService;
	@Autowired
	private WorkBookService workBookService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<ReportManMachineCombinationEntity> page = this
				.selectPage(new Query<ReportManMachineCombinationEntity>(params).getPage());

		return new PageUtils(page);
	}

	@Override
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();

		EntityWrapper<ReportManMachineCombinationEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId);
		ReportManMachineCombinationEntity reportManMachineCombinationEntity = selectOne(entityWrapper);
		Integer id = 0;
		Map<String, Object> map = new HashMap<>();
		if (reportManMachineCombinationEntity != null) {
			id = reportManMachineCombinationEntity.getId();
			map.put("date", reportManMachineCombinationEntity.getMonthResult());
			map.put("mt", reportManMachineCombinationEntity.getMt());
			map.put("tableNum", reportManMachineCombinationEntity.getSelectnum());
			map.put("enter", reportManMachineCombinationEntity.getEnter());
			generateTotalData(map);
		}
		ModelEntity model = modelService.selectById(modelId);
		map.put("modelName", model.getName());
		map.put("modelType", model.getCode());

		// TODO 添加调用模版方法及生成目标excel文件方法
		String exportFileName = Constant.TEMPLATE_PATH + reportManMachineCombinationEntity.getSheetName() + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}
		BigDecimal P = (BigDecimal) map.get("P");
		String templateFileName = "";
		if (P.compareTo(BigDecimal.valueOf(0.79)) > 0) {
			templateFileName = Constant.TEMPLATE_PATH + "man_machine_joint_template2.xls";
		} else {
			templateFileName = Constant.TEMPLATE_PATH + "man_machine_joint_template1.xls";
		}
		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
		WriteSheet writeSheet = EasyExcel.writerSheet().build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, fillConfig, writeSheet);
		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

	@Override
	public void generateReportData(List<Integer> workBookIds) {
		List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
		List<WorkBookEntity> filteredWorkBooks = workBookService
				.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
		List<ReportManMachineCombinationEntity> list = generateReportManMachineCombination(filteredWorkBooks);
	}

	private List<ReportManMachineCombinationEntity> generateReportManMachineCombination(
			List<WorkBookEntity> workBooks) {
		List<ReportManMachineCombinationEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity work : workBooks) {
			EntityWrapper<ReportManMachineCombinationEntity> entityWrapper = new EntityWrapper<>();
			entityWrapper.eq("stlst", work.getStlst()).eq("model_id", work.getModelId()).eq("phase_id",
					work.getPhaseId());
			List<ReportManMachineCombinationEntity> list = selectList(entityWrapper);
			ReportManMachineCombinationEntity reportManMachineCombinationEntity = new ReportManMachineCombinationEntity();
			if (list.size() > 0) {
				reportManMachineCombinationEntity = list.get(0);
			} else {
				reportManMachineCombinationEntity.setModelId(work.getModelId());
				reportManMachineCombinationEntity.setPhaseId(work.getPhaseId());
				reportManMachineCombinationEntity.setStlst(work.getStlst());
				reportManMachineCombinationEntity.setDeptId(work.getDeptId());
				insert(reportManMachineCombinationEntity);
			}
			results.add(reportManMachineCombinationEntity);
		}

		return results;
	}

	private void generateTotalData(Map<String, Object> map) throws IOException {
		String data = IOUtils.toString(ashcraftTable.getInputStream());
		Map<String, Object> ashcraftTables = JSONObject.parseObject(data);
		BigDecimal coefficient = BigDecimal.valueOf(Double.valueOf((String) ashcraftTables.get("Coefficient")));
		EntityWrapper<AshcraftTableEntity> ew = new EntityWrapper<>();

		BigDecimal HT1 = coefficient.multiply((BigDecimal) map.get("enter"));
		BigDecimal HT = HT1.subtract(BigDecimal.valueOf(0.1), new MathContext(0)).multiply(BigDecimal.valueOf(10))
				.add(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(10), 2, RoundingMode.HALF_UP);
		BigDecimal P1 = HT.divide((BigDecimal) map.get("mt"), 3, RoundingMode.HALF_UP);
		BigDecimal P = P1.add(BigDecimal.valueOf(0.005));
		ew.lt("p", P).isNotNull("ou").isNotNull("sa").isNotNull("mu");
		ew.setSqlSelect("max(p) as p, ou, sa, mu");
		ew.groupBy("ou, sa, mu");
		AshcraftTableEntity ashcraftTableEntity = ashcraftTableService.selectOne(ew);
		map.put("HT1", HT1);
		map.put("HT", HT);
		map.put("P1", P1);
		map.put("P", P);
		if (P.compareTo(BigDecimal.valueOf(0.79)) > 0) {
			// P>0.79
			BigDecimal rWorkTime = HT.multiply(coefficient).divide(BigDecimal.valueOf(0.95), 0, RoundingMode.HALF_UP);
			BigDecimal STime = rWorkTime.multiply(coefficient);
			BigDecimal STime1 = STime.divide(BigDecimal.valueOf(10), 0, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(10));
			BigDecimal N2Time = STime1.multiply(BigDecimal.valueOf(0.06));
			map.put("rWorkTime", rWorkTime);
			map.put("STime", STime);
			map.put("STime1", STime1);
			map.put("N2Time", N2Time);
		} else {
			// n2-n6
			ashcraftTableEntity.getMu();
			map.put("mu", ashcraftTableEntity.getMu());
			map.put("sa", ashcraftTableEntity.getSa());
			map.put("ou", ashcraftTableEntity.getOu());
			BigDecimal tableNum = BigDecimal.valueOf(Double.valueOf((String) map.get("tableNum")));
			BigDecimal mt = (BigDecimal) map.get("mt");
			BigDecimal i = HT.add(mt).multiply(ashcraftTableEntity.getSa()).divide(BigDecimal.valueOf(100), 0,
					RoundingMode.HALF_UP);
			// map.get("tableNum") => 2 3 4 5 6
			BigDecimal rWorkTime = HT.add(mt).add(i).divide(tableNum, RoundingMode.HALF_UP);
			BigDecimal sTime = rWorkTime.multiply(coefficient).divide(BigDecimal.valueOf(1), 0, RoundingMode.HALF_UP);
			BigDecimal sTime1 = rWorkTime.multiply(coefficient).divide(BigDecimal.valueOf(1), 2, RoundingMode.HALF_UP);
			BigDecimal rdValues = rWorkTime.multiply(coefficient)
					.divide(BigDecimal.valueOf(10), 0, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10));
			map.put("i", i);
			map.put("rWorkTime", rWorkTime);
			map.put("sTime", sTime);
			map.put("sTime1", sTime1);
			map.put("rdValues", rdValues);
		}
		map.put("Coefficient", coefficient);
	}

}
