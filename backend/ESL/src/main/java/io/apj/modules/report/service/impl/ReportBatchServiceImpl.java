package io.apj.modules.report.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.dao.ReportBatchDao;
import io.apj.modules.report.dao.ReportManMachineCombinationDao;
import io.apj.modules.report.entity.AshcraftTableEntity;
import io.apj.modules.report.entity.ReportBatchEntity;
import io.apj.modules.report.entity.ReportManMachineCombinationEntity;
import io.apj.modules.report.service.AshcraftTableService;
import io.apj.modules.report.service.ReportBatchService;
import io.apj.modules.report.service.ReportManMachineCombinationService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.WorkOperationsService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Service("reportBatchService")
public class ReportBatchServiceImpl
		extends ServiceImpl<ReportBatchDao, ReportBatchEntity>
		implements ReportBatchService {

	@Autowired
	private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private ReportBatchService reportBatchService;
	@Autowired
	private WorkBookService workBookService;

	@Override
	@DataFilter(subDept = true, user = true, deptId = "dept_id")
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<ReportBatchEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").orderBy("update_at", false)
				.like(params.get("stlst") != null && params.get("stlst") != "", "stlst", (String) params.get("stlst"))
				.like(params.get("versionNumber") != null && params.get("versionNumber") != "", "version_number",
						(String) params.get("versionNumber"))
				.like(params.get("destinations") != null && params.get("destinations") != "", "destinations",
						(String) params.get("destinations"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("modelId"))) {
			entityWrapper.eq("model_id", Integer.parseInt((String) params.get("modelId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("phaseId"))) {
			entityWrapper.eq("phase_id", Integer.parseInt((String) params.get("phaseId")));
		}


		Page<ReportBatchEntity> page = this.selectPage(new Query<ReportBatchEntity>(params).getPage());
		for(ReportBatchEntity item : page.getRecords()){
			item.setModelEntity(modelService.selectById(item.getModelId()));
			item.setPhaseEntity(phaseService.selectById(item.getPhaseId()));
		}

		return new PageUtils(page);
	}

	@Override
	public List<Object> selectAllWorkBook(Integer id) {
		ReportBatchEntity reportBatchEntity = reportBatchService.selectById(id);
		EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId());
		List<WorkBookEntity> workBookEntityList = workBookService.selectList(entityWrapper);
		List<Object> list = new ArrayList<>();
		list.add(reportBatchEntity);
		list.add(workBookEntityList);
		return list;
	}

}
