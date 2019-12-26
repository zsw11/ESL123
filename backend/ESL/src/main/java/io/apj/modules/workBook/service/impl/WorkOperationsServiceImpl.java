package io.apj.modules.workBook.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.sys.service.SysDictService;
import io.apj.modules.workBook.dao.WorkOperationsDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("workOperationsService")
public class WorkOperationsServiceImpl extends ServiceImpl<WorkOperationsDao, WorkOperationsEntity>
		implements WorkOperationsService {
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private WorkOperationsService workOperationsService;
	@Autowired
	private SysDictService sysDictService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<WorkOperationsEntity> page = this.selectPage(new Query<WorkOperationsEntity>(params).getPage());

		return new PageUtils(page);
	}

	@Override
	@Transactional
	public void workOperationImport(Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<WorkOperationsEntity> operationsEntityList = new ArrayList<>();
		for (int i = 0; i < maps.size(); i++) {
			WorkOperationsEntity workOperationsEntity = new WorkOperationsEntity();
			// deviceMap
			Map<String, Object> deviceMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 设备
				if (keyStrs[0].equals("workOperation")) {
					if (keyStrs[1].equals("common")) {
						if(value.equals("是")) {
							deviceMap.put(keyStrs[1], true);
						} else {
							deviceMap.put(keyStrs[1], false);
						}
						continue;
					}
					deviceMap.put(keyStrs[1], value);
				}
			}
			DataUtils.transMap2Bean2(deviceMap, workOperationsEntity);
			ValidatorUtils.validateEntity(workOperationsEntity, i);
			workOperationsEntity.setCreateBy((Integer) map.get("userID"));
			operationsEntityList.add(workOperationsEntity);
		}
		try {
			workOperationsService.insertBatch(operationsEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("批量导入失败", 500);
		}
	}

	@Override
	@Transactional
	public void workOperationExport(Map<String, Object> map, HttpServletResponse response) throws Exception {
		// 过滤字段，前端传
		List<String> list = (List<String>) map.get("exportAttributes");
		// 查询类型
		String type = map.get("filterType").toString();
		// 普通查询
		Map<String, Object> params = (Map<String, Object>) map.get("filters");
		if (null == params) {
			params = new HashMap<>();
		}
		params.put("page", "1");
		params.put("limit", "9999999");
		PageUtils pageUtils = workOperationsService.queryPage(params);
		List<WorkOperationsEntity> workOperationsEntityList = (List<WorkOperationsEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (WorkOperationsEntity item : workOperationsEntityList) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("workOperation", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "workOperation", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "分析表明细.xlsx", data);
	}

	@Override
	public void deletebyWrapper(Wrapper<WorkOperationsEntity> wrapper) {
		List<WorkOperationsEntity> WorkOperationsList = this.selectList(wrapper);
		for (WorkOperationsEntity item : WorkOperationsList) {
			item.setDeleteAt(new Date());
		}
		if (WorkOperationsList.size() > 0) {
			this.updateAllColumnBatchById(WorkOperationsList);
		}
	}

	@Override
	public List<String> getWorkBookFilePaths(List<WorkBookEntity> workBookEntities) throws IOException {
		List<String> paths = new ArrayList<>(workBookEntities.size());
		Map<String, Object> params = new HashMap<>();
		params.put("date", DateUtils.format(new Date(), "yyyy/MM/dd"));
		for (WorkBookEntity entity : workBookEntities) {
			Integer entityId = entity.getId();
			List<WorkOperationsEntity> list = selectByWorkBook(entityId);

			String workName = entity.getWorkName();
			Integer workstationId = entity.getWorkstationId();
			WorkstationEntity workstation = workstationService.selectById(workstationId);
			String workstationName = workstation.getName();
			params.put("workName", workName);
			params.put("workstationName", workstationName);

			generateTotalData(list, params);
			String templateFileName = PathUtil.getExcelTemplatePath("work_operations");
			String fileName = PathUtil.getResourcesPath() + File.separator + workName + ".xls";
			paths.add(fileName);

			ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
			WriteSheet writeSheet = EasyExcel.writerSheet().build();
			FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
			excelWriter.fill(params, writeSheet);
			excelWriter.fill(list, fillConfig, writeSheet);
			excelWriter.finish();
		}
		return paths;
	}

	private void generateTotalData(List<WorkOperationsEntity> list, Map<String, Object> params) {
		BigDecimal timeValueTotal = BigDecimal.ZERO;
		BigDecimal tmuTotal = BigDecimal.ZERO;
		BigDecimal secondConvertTotal = BigDecimal.ZERO;

		for (WorkOperationsEntity entity : list) {
			BigDecimal timeValue = entity.getTimeValue();
			BigDecimal tmu = entity.getTmu();
			BigDecimal secondConvert = entity.getSecondConvert();

			timeValueTotal = timeValueTotal.add(timeValue);
			tmuTotal = tmuTotal.add(tmu);
			secondConvertTotal = secondConvertTotal.add(secondConvert);

			//TODO remark是数字吗?

		}
		params.put("timeValueTotal", timeValueTotal);
		params.put("tmuTotal", tmuTotal);
		params.put("secondConvertTotal", secondConvertTotal);
	}

	private List<WorkOperationsEntity> selectByWorkBook(Integer workBookId) {
		EntityWrapper<WorkOperationsEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("work_book_id", workBookId);
		return selectList(wrapper);
	}

}