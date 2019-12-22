package io.apj.modules.workBook.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.PathUtil;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.WorkstationService;
import io.apj.modules.workBook.dao.WorkOperationsDao;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("workOperationsService")
public class WorkOperationsServiceImpl extends ServiceImpl<WorkOperationsDao, WorkOperationsEntity>
		implements WorkOperationsService {
	@Autowired
	private WorkstationService workstationService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<WorkOperationsEntity> page = this.selectPage(new Query<WorkOperationsEntity>(params).getPage());

		return new PageUtils(page);
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