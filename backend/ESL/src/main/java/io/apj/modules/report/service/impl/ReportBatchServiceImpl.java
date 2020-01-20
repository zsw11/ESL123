package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.modules.masterData.service.ReportService;
import io.apj.modules.report.dao.ReportBatchDao;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private CompareService compareService;
	@Autowired
	private StationTimeService stationTimeService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportManMachineCombinationService reportManMachineCombinationService;
	@Autowired
	private MostValueService mostValueService;
	@Autowired
	private RevisionHistoryService revisionHistoryService;
	@Autowired
	private TotalService totalService;
	@Autowired
	private TimeContactService timeContactService;
	@Autowired
	private StandardTimeService standardTimeService;
	@Autowired
	private StandardWorkService standardWorkService;
	@Autowired
	private ChangeRecordService changeRecordService;

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
		Page<ReportBatchEntity> page = this.selectPage(new Query<ReportBatchEntity>(params).getPage(),entityWrapper);
		for(ReportBatchEntity item : page.getRecords()){
			item.setModelEntity(modelService.selectById(item.getModelId()));
			item.setPhaseEntity(phaseService.selectById(item.getPhaseId()));
		}

			return new PageUtils(page);
	}

	@Override
	public List<Object> selectAllReport(Integer id) {
		ReportBatchEntity reportBatchEntity = reportBatchService.selectById(id);
		List<ReportEntity> reportEntity = reportBatchService.selectReportByFive(id);
		List<Object> list = new ArrayList<>();
		reportBatchEntity.setModelEntity(modelService.selectById(reportBatchEntity.getModelId()));
		list.add(reportBatchEntity);
		list.add(reportEntity);
		return list;
	}

	@Override
	public List<ReportEntity> selectReportByFive(Integer id) {
		ReportBatchEntity reportBatchEntity = reportBatchService.selectById(id);
		List<ReportEntity> reportEntityList = new ArrayList<>();
		if(reportManMachineCombinationService.selectList(new EntityWrapper<ReportManMachineCombinationEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(2));
		}
		if(stationTimeService.selectList(new EntityWrapper<StationTimeEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(3));
		}
		if(compareService.selectList(new EntityWrapper<CompareEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(4));
		}
		if(mostValueService.selectList(new EntityWrapper<MostValueEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(5));
		}
		if(revisionHistoryService.selectList(new EntityWrapper<RevisionHistoryEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(6));
		}
		if(totalService.selectList(new EntityWrapper<TotalEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(7));
		}
		if(timeContactService.selectList(new EntityWrapper<TimeContactEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(9));
		}
		if(standardTimeService.selectList(new EntityWrapper<StandardTimeEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(11));
		}
		if(standardWorkService.selectList(new EntityWrapper<StandardWorkEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(12));
		}
		if(changeRecordService.selectList(new EntityWrapper<ChangeRecordEntity>().eq("stlst",reportBatchEntity.getStlst()).eq("version_number",reportBatchEntity.getVersionNumber())
				.eq("destinations",reportBatchEntity.getDestinations()).eq("model_id",reportBatchEntity.getModelId())
				.eq("phase_id",reportBatchEntity.getPhaseId())).size()!=0){
			reportEntityList.add(reportService.selectById(13));
		}
		return reportEntityList;
	}

}
