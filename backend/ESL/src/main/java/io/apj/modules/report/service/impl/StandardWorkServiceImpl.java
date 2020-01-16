package io.apj.modules.report.service.impl;

import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.*;
import io.apj.modules.report.dao.StandardWorkDao;
import io.apj.modules.report.entity.StandardWorkEntity;
import io.apj.modules.report.entity.StandardWorkItemEntity;
import io.apj.modules.report.service.StandardWorkItemService;
import io.apj.modules.report.service.StandardWorkService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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

@Service("standardWorkService")
public class StandardWorkServiceImpl extends ServiceImpl<StandardWorkDao, StandardWorkEntity>
		implements StandardWorkService {
	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private StandardWorkService standardWorkService;
	@Autowired
	private StandardWorkItemService standardWorkItemService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private WorkBookService workBookService;
	@Autowired
	private WorkstationTypeService workstationTypeService;
	@Autowired
	private WorkstationTypeNodeService workstationTypeNodeService;
	@Autowired
	private NodeModelWorkstationRelaService nodeModelWorkstationRelaService;


	@Override
	public PageUtils queryPage(Map<String, Object> params) throws ParseException {
		EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("sheetName") != null && params.get("sheetName") != "", "sheet_name",
						(String) params.get("sheetName"))
				.like(params.get("modelType") != null && params.get("modelType") != "", "model_type",
						(String) params.get("modelType"))
				.like(params.get("revNo") != null && params.get("revNo") != "", "rev_no", (String) params.get("revNo"))
				.like(params.get("firstStandardWorkTitle") != null && params.get("firstStandardWorkTitle") != "",
						"first_standard_work_title", (String) params.get("firstStandardWorkTitle"))
				.like(params.get("thirdStandardWorkTitle") != null && params.get("thirdStandardWorkTitle") != "",
						"third_standard_work_title", (String) params.get("thirdStandardWorkTitle"));

		Map<String, Object> map = (Map) JSON.parse((String) params.get("monthResult"));
		if (map != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date start = format.parse((String) map.get("monthResultStart"));
			Date stop = format.parse((String) map.get("monthResultStop"));
			entityWrapper.between("month_result", start, stop);
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("coefficient"))) {
			entityWrapper.eq("coefficient", (String) params.get("coefficient"));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}
		Page<StandardWorkEntity> page = this.selectPage(new Query<StandardWorkEntity>(params).getPage(), entityWrapper);
		for (StandardWorkEntity entity : page.getRecords()) {
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
		PageUtils page = standardWorkService.queryPage(params);
		List<StandardWorkEntity> items = (List<StandardWorkEntity>) page.getData();
		int phaseId;
		int modelId;
		String stlst;
		for (StandardWorkEntity entity : items) {
			phaseId = entity.getPhaseId();
			modelId = entity.getModelId();
			stlst = entity.getStlst();
			Map<String, Object> data = new HashMap<>();
			data.put("modelId", modelId);
			data.put("phaseId", phaseId);
			data.put("stlst", stlst);
			String name = "标准工数表";
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

	/**
	 * report 加数据
	 */
	@Override
	public void generateReportData(List<Integer> workBookIds,Integer reportId) {
		List<WorkBookEntity> workBooks = workBookService.selectBatchIds(workBookIds);
		if(workBooks!=null&&workBooks.size()>0) {
			List<WorkBookEntity> filteredWorkBooks = workBookService
					.filterUniquePhaseAndModelAndStlstOfWorkBooks(workBooks);
			List<StandardWorkEntity> list = generateStandardWork(filteredWorkBooks,reportId);
			for (StandardWorkEntity entity : list) {
				List<Integer> filteredWorkBookIds = workBookService.filterWorkBookIdsByPhaseAndModelAndStlst(workBooks,
						entity.getModelId(), entity.getStlst(), entity.getPhaseId(), entity.getDestinations(), entity.getVersionNumber());
				if (filteredWorkBookIds != null && filteredWorkBookIds.size() > 0) {
					standardWorkItemService.generateStandardWorkItem(filteredWorkBookIds, entity.getId());
				}
			}
		}
	}

	private List<StandardWorkEntity> generateStandardWork(List<WorkBookEntity> workBooks,Integer reportId) {
		List<StandardWorkEntity> results = new ArrayList<>(workBooks.size());
		for (WorkBookEntity work : workBooks) {
			Integer modelId = work.getModelId();
			Integer workstationId = work.getWorkstationId();
			EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
			entityWrapper.eq("stlst", work.getStlst()).eq("model_id", work.getModelId()).eq("phase_id",
				work.getPhaseId()).eq("destinations", work.getDestinations()).eq("version_number",work.getVersionNumber());
			StandardWorkEntity entity = selectOne(entityWrapper);
			if (entity==null) {
				StandardWorkEntity standardWorkEntity = new StandardWorkEntity();
				standardWorkEntity.setModelId(work.getModelId());
				standardWorkEntity.setPhaseId(work.getPhaseId());
				standardWorkEntity.setStlst(work.getStlst());
				standardWorkEntity.setDeptId(work.getDeptId());
				standardWorkEntity.setDestinations(work.getDestinations());
				standardWorkEntity.setVersionNumber(work.getVersionNumber());
				String workstationType = getWorkstationTypeDetail(reportId ,modelId ,workstationId);
				standardWorkEntity.setWorkstationType(workstationType);

				insert(standardWorkEntity);
				results.add(standardWorkEntity);
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
	public List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException {
		// TODO
		Integer phaseId = (Integer) params.get("phaseId");
		Integer modelId = (Integer) params.get("modelId");
		String stlst = params.get("stlst").toString();
		String destinations =  (String) params.get("destinations");
		String versionNumber = (String) params.get("versionNumber");

		EntityWrapper<StandardWorkEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("phase_id", phaseId).eq("stlst", stlst).eq("model_id", modelId).eq("destinations", destinations).eq("version_number", versionNumber);
		StandardWorkEntity standardWorkEntity = selectOne(entityWrapper);
		Integer id = null;
		Map<String, Object> map = new HashMap<>();
		List<StandardWorkItemEntity> list = new ArrayList<>();
		String sheetName=null;
		if (standardWorkEntity != null) {
			id = standardWorkEntity.getId();
			map.put("date", standardWorkEntity.getMonthResult());
			list = standardWorkItemService.getListBySWId(id);
			sheetName=standardWorkEntity.getSheetName();
		}
		ModelEntity model = modelService.selectById(modelId);
		if(model!=null) {
			map.put("modelName", model.getName());
			map.put("modelType", model.getCode());
		}
		if(list!=null&&list.size()>0) {
			generateTotalData(list, map);
		}
		// TODO 添加调用模版方法及生成目标excel文件方法
		String templateFileName = Constant.TEMPLATE_PATH + "report_standard_work_template.xls";
		String exportFileName = Constant.TEMPLATE_PATH + "template\\" + sheetName + ".xls";
		File historyExcel = new File(exportFileName);
		if (historyExcel.exists()) {
			historyExcel.delete();
		}

		ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
		// ExcelWriter excelWriter =
		// EasyExcel.write(out).withTemplate(templateFileName).build();
		WriteSheet writeSheet = EasyExcel.writerSheet().build();
		FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
		excelWriter.fill(map, writeSheet);
		excelWriter.fill(list, fillConfig, writeSheet);
		response.setHeader("Content-disposition", "attachment;filename=" + standardWorkEntity.getSheetName() + ".xls");
		excelWriter.finish();
		return Arrays.asList(exportFileName);
	}

	private void generateTotalData(List<StandardWorkItemEntity> list, Map<String, Object> map) {
		BigDecimal htTotal = BigDecimal.ZERO;
		BigDecimal stTotal = BigDecimal.ZERO;

		for (StandardWorkItemEntity entity : list) {
			htTotal = htTotal
					.add(entity.getFirstTime().multiply(BigDecimal.valueOf(1000)).divide(BigDecimal.valueOf(3600), 5));
			stTotal = stTotal.add(entity.getFirstTime());
			entity.setHfTime(
					entity.getFirstTime().multiply(BigDecimal.valueOf(1000)).divide(BigDecimal.valueOf(3600), 5));
		}
		map.put("htTotal", htTotal);
		map.put("stTotal", stTotal);

	}

}
