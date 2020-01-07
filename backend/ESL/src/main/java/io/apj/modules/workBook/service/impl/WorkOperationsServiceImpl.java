package io.apj.modules.workBook.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
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
		EntityWrapper<WorkOperationsEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false);
		Page<WorkOperationsEntity> page = this.selectPage(new Query<WorkOperationsEntity>(params).getPage(),
				entityWrapper);

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
				if (keyStrs[0].equals("workOperations")) {
					if (keyStrs[1].equals("common")) {
						if (value.equals("是")) {
							deviceMap.put(keyStrs[1], true);
						} else {
							deviceMap.put(keyStrs[1], false);
						}
						continue;
					}
					deviceMap.put(keyStrs[1], value);
				}
			}
			workOperationsEntity = JSON.parseObject(JSON.toJSONString(deviceMap), WorkOperationsEntity.class);
//			DataUtils.transMap2Bean2(deviceMap, workOperationsEntity);
			ValidatorUtils.validateEntity(workOperationsEntity, i);
			workOperationsEntity.setCreateBy((Integer) map.get("userID"));
			workOperationsEntity.setWorkBookId(Integer.parseInt((String) map.get("filterId")));
			operationsEntityList.add(workOperationsEntity);
		}
		try {
			workOperationsService.insertBatch(operationsEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("分析表明细有重复，请检查后再导入", 500);
		}
	}

	@Override
	@Transactional
	public void workOperationExport(Map<String, Object> map, HttpServletResponse response) throws Exception {
		// 过滤字段，前端传
		List<String> list = (List<String>) map.get("exportAttributes");
		List<String> ListFilter = new ArrayList<>();
		ListFilter.add("workOperations.No");
		ListFilter.add(list.get(0));
		ListFilter.add(list.get(1));
		ListFilter.add(list.get(2));
		ListFilter.add("workOperations.a0null");
		ListFilter.add(list.get(3));
		ListFilter.add("workOperations.b0null");
		ListFilter.add(list.get(4));
		ListFilter.add("workOperations.g0null");
		ListFilter.add(list.get(5));
		ListFilter.add("workOperations.a1null");
		ListFilter.add(list.get(6));
		ListFilter.add("workOperations.b1null");
		ListFilter.add(list.get(7));
		ListFilter.add("workOperations.p0null");
		ListFilter.add(list.get(8));
		ListFilter.add("workOperations.m0null");
		ListFilter.add(list.get(9));
		ListFilter.add("workOperations.x0null");
		ListFilter.add(list.get(10));
		ListFilter.add("workOperations.i0null");
		ListFilter.add(list.get(11));
		ListFilter.add("workOperations.a2null");
		ListFilter.add(list.get(12));
		ListFilter.add("workOperations.b2null");
		ListFilter.add(list.get(13));
		ListFilter.add("workOperations.p1null");
		ListFilter.add(list.get(14));
		ListFilter.add("workOperations.a3null");
		ListFilter.add(list.get(15));
		ListFilter.add(list.get(16));
		ListFilter.add("workOperations.a4null");
		ListFilter.add(list.get(17));
		ListFilter.add("workOperations.b3null");
		ListFilter.add(list.get(18));
		ListFilter.add("workOperations.p2null");
		ListFilter.add(list.get(19));
		ListFilter.add(list.get(20));
		ListFilter.add(list.get(21));

		// 查询类型
		String type = map.get("filterType").toString();
		Integer workBookId = Integer.parseInt((String) map.get("filterId"));
		// 普通查询
		Map<String, Object> params = (Map<String, Object>) map.get("filters");
		if (null == params) {
			params = new HashMap<>();
		}
		params.put("page", "1");
		params.put("limit", "9999999");
		PageUtils pageUtils = workOperationsService.queryPage(params);
		List<WorkOperationsEntity> workOperationsEntityList = (List<WorkOperationsEntity>) pageUtils.getData();
		List<WorkOperationsEntity> workOperationsEntityListFilter = new ArrayList<>();
		for (WorkOperationsEntity item : workOperationsEntityList) {
			if (item.getWorkBookId() != null) {
				if (item.getWorkBookId() == workBookId) {
					workOperationsEntityListFilter.add(item);
				}
			}
		}
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		Integer count = 1;
		for (WorkOperationsEntity item : workOperationsEntityListFilter) {
			item.setNo(count++);
			if(item.getA0()!=null && item.getA0()<0){
				if(item.getA0() == -9999){
					item.setA0null(0);
					item.setA0(null);
				}else {
					item.setA0null(Math.abs(item.getA0()));
					item.setA0(null);
				}
			}
			if(item.getB0()!=null && item.getB0()<0){
				if(item.getB0()==-9999){
					item.setB0(null);
					item.setA0null(0);

				}else{
					item.setB0null(Math.abs(item.getB0()));
					item.setB0(null);
				}
			}
			if(item.getP0()!=null && item.getP0()<0){
				if(item.getP0()==-9999){
					item.setP0null(0);
					item.setP0(null);
				}else {
					item.setP0null(Math.abs(item.getP0()));
					item.setP0(null);
				}

			}
			if(item.getA1()!=null && item.getA1()<0){
				if(item.getA1()==-9999){
					item.setA1null(0);
					item.setA1(null);
				}else {
					item.setA1null(Math.abs(item.getA1()));
					item.setA1(null);
				}
			}
			if(item.getB1()!=null && item.getB1()<0){
				if(item.getB1()==-9999){
					item.setB1null(0);
					item.setB1(null);
				}else {
					item.setB1null(Math.abs(item.getB1()));
					item.setB1null(null);
				}
			}
			if(item.getP1()!=null && item.getP1()<0){
				if(item.getP1()==-9999){
					item.setP1null(0);
					item.setP1(null);
				}else {
					item.setP1null(Math.abs(item.getP1()));
					item.setP1(null);
				}
			}
			if(item.getA2()!=null && item.getA2()<0){
				if(item.getA2()==-9999){
					item.setA2null(0);
					item.setA2(null);
				}else {
					item.setA2null(Math.abs(item.getA2()));
					item.setA2(null);
				}
			}
			if(item.getB2()!=null && item.getB2()<0){
				if(item.getB2()==-9999){
					item.setB2null(0);
					item.setB2(null);
				}else {
					item.setB2null(Math.abs(item.getB2()));
					item.setB2(null);
				}

			}
			if(item.getP2()!=null && item.getP2()<0){
				if(item.getP2()==-9999){
					item.setP2null(0);
					item.setP2(null);
				}else {
					item.setP2null(Math.abs(item.getP2()));
					item.setP2(null);
				}

			}
			if(item.getA3()!=null && item.getA3()<0){
				if(item.getA3()==-9999){
					item.setA3null(0);
					item.setA3(null);
				}else {
					item.setA3null(Math.abs(item.getA3()));
					item.setA3(null);
				}

			}
			if(item.getB3()!=null && item.getB3()<0){
				if(item.getB3()==-9999){
					item.setB3null(0);
					item.setB3(null);
				}else {
					item.setB3null(Math.abs(item.getB3()));
					item.setB3(null);
				}

			}
			if(item.getG0()!=null && item.getG0()<0){
				if(item.getG0()==-9999){
					item.setG0null(0);
					item.setG0(null);
				}else {
					item.setG0null(Math.abs(item.getG0()));
					item.setG0(null);
				}
			}
			if(item.getM0()!=null && item.getM0()<0){
				if(item.getM0()==-9999){
					item.setM0null(0);
					item.setM0(null);
				}else {
					item.setM0null(Math.abs(item.getM0()));
					item.setM0(null);
				}
			}
			if(item.getX0()!=null && item.getX0()<0){
				if(item.getX0()==-9999){
					item.setX0null(0);
					item.setX0(null);
				}else{
					item.setX0null(Math.abs(item.getX0()));
					item.setX0(null);
				}
			}
			if(item.getI0()!=null && item.getI0()<0){
				if(item.getI0()==-9999){
					item.setI0null(0);
					item.setI0(null);
				}else {
					item.setI0null(Math.abs(item.getI0()));
					item.setI0(null);
				}
			}
			if(item.getA4()!=null && item.getA4()<0){
				if(item.getA4()==-9999){
					item.setA4null(0);
					item.setA4(null);
				}else {
					item.setA4null(Math.abs(item.getA4()));
					item.setA4(null);
				}
			}

			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("workOperations", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(ListFilter, "workOperations", dataList);
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
			String templateFileName = Constant.TEMPLATE_PATH + "work_operations.xls";
			String exportFileName = Constant.TEMPLATE_PATH + workName + ".xls";
			File historyExcel = new File(exportFileName);
			if (historyExcel.exists()) {
				historyExcel.delete();
			}
			paths.add(exportFileName);

			ExcelWriter excelWriter = EasyExcel.write(exportFileName).withTemplate(templateFileName).build();
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

			// TODO remark是数字吗?

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