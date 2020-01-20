package io.apj.modules.workBook.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.masterData.entity.*;
import io.apj.modules.masterData.service.*;
import io.apj.modules.report.entity.ReportGroupDeptRelaEntity;
import io.apj.modules.report.service.ReportBatchService;
import io.apj.modules.report.service.ReportGroupDeptRelaService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * 分析表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:03
 */
@RestController
@RequestMapping("/api/v1/workbook")
public class WorkBookController extends AbstractController {
	@Autowired
	private WorkBookService workBookService;
	@Autowired
    private ModelService modelService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private ReportBatchService reportBatchService;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private ReportGroupDeptRelaService reportGroupDeptRelaService;
	@Autowired
	private ReportGroupService reportGroupService;
	@Autowired
	private ReportGroupReportRelaService reportGroupReportRelaService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ModelWorkstationRelaService modelWorkstationRelaService;


	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("workBook:workbook:list")
	public R list(@RequestParam Map<String, Object> params) throws ParseException {
		PageUtils page = workBookService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("workBook:workbook:info")
	public R info(@PathVariable("id") Integer id) {
		WorkBookEntity workBook = workBookService.selectById(id);
        workBook.setModelEntity(modelService.selectById(workBook.getModelId()));
		workBook.setPhaseEntity(phaseService.selectById(workBook.getPhaseId()));
		workBook.setSysDeptEntity(sysDeptService.selectById(workBook.getDeptId()));
		workBook.setWorkstationEntity(workstationService.selectById(workBook.getWorkstationId()));
//		JSONObject jsonRemarks = JSONObject.fromObject(workBook.getRemarks());
//		workBook.setRemarks(jsonRemarks);
		return R.ok().put("workBook", workBook);
	}

	/**
	 * 分析表和手顺详情
	 */
	@RequestMapping("/detailWithOperations/{id}")
	@RequiresPermissions("workBook:workbook:info")
	public R detailWithOperations(@PathVariable("id") Integer id) {
		WorkBookEntity workBook = workBookService.detailWithOperations(id);
		workBook.setMakerId(getUserId().intValue());
		return R.ok().put("workBook", workBook);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("workBook:workbook:create")
	public R save(@RequestBody Map<String,Object> map) {
		WorkBookEntity workBookEntity = JSON.parseObject(JSON.toJSONString(map), WorkBookEntity.class);
		workBookEntity.setDeptId(getUserDeptId().intValue());
		workBookEntity.setIfAlter(false);
		workBookEntity.setMakerId(getUserId().intValue());
		workBookEntity.setMakedAt(new Date());
		workBookEntity.setCreateBy(getUserId().intValue());
		workBookService.insert(workBookEntity);
		return R.ok();
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping("/updateremarks")
	public ResponseEntity<Object> updateRemarks(@RequestParam Integer id,@RequestBody Map<String,Object> map) {
		WorkBookEntity workBookEntity;
		map.put("remarks",map.get("remarks").toString());
		workBookEntity  = JSON.parseObject(JSON.toJSONString(map), WorkBookEntity.class);
//		DataUtils.transMap2Bean2(map, workBookEntity);
		workBookEntity.setId(id);
		Integer lockById = workBookService.selectById(id).getLockBy();
		workBookEntity.setUpdateBy(getUserId().intValue());
//		workBookEntity.setMakedAt(new Date());
		workBookEntity.setLockBy(getUserId().intValue());
		workBookEntity.setLockAt(new Date());
		workBookService.updateById(workBookEntity);
		return RD.success(workBookEntity);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("workBook:workbook:update")
	public ResponseEntity<Object> update(@RequestParam Integer id,@RequestBody Map<String,Object> map) {
//		JSONArray jsonArray = null;
//		try
//		{
//			jsonArray=new JSONArray(Collections.singletonList(map.get("remarks")));
//		} catch (JSONException e)
//		{
//			e.printStackTrace();
//		}
		WorkBookEntity workBookEntity =  new WorkBookEntity();
			map.put("remarks",map.get("remarks").toString());
		workBookEntity  = JSON.parseObject(JSON.toJSONString(map), WorkBookEntity.class);
//		DataUtils.transMap2Bean2(map, workBookEntity);
		workBookEntity.setId(id);
		Integer lockById = workBookService.selectById(id).getLockBy();
		workBookEntity.setUpdateBy(getUserId().intValue());
//		workBookEntity.setMakedAt(new Date());
		if(lockById == null){
			workBookService.updateById(workBookEntity);
		}else if(lockById.equals(getUserId().intValue())){
			workBookService.updateById(workBookEntity);
		}else {
			return RD.FORBIDDEN("LOCKED","已被被人锁定，无法保存");
		}

		return RD.success(workBookEntity);
	}

	/**
	 * 复制
	 *
	 * @return
	 */
	@RequestMapping("/copy")
//	@RequiresPermissions("workBook:workbook:update")
	public ResponseEntity<Object> copy(@RequestBody WorkBookEntity workBook) {
		int workBookId = workBook.getId();
		workBook.setCreateBy(getUserId().intValue());
		workBook.setMakedAt(new Date());
		workBook.setMakerId(getUserId().intValue());
		WorkBookEntity workBookEntity = workBookService.copyWorkBook(workBook,workBookId);

		return RD.ok(workBookEntity);
	}

	/**
	 * 批量更新
	 */
	@RequestMapping("/updateOperation")
	@RequiresPermissions("workBook:workbook:update")
	public ResponseEntity<Object> updateOperation(@RequestBody Map<String, Object> params) {
		List<Integer> newIDList = workBookService.updateOperation(params);
		return RD.success(newIDList);

	}

	/**
	 * 批量更新
	 */
	@RequestMapping("/updateAll")
	@RequiresPermissions("workBook:workbook:update")
	public ResponseEntity<Object> updateAll(@RequestBody Map<String, Object> params) {
		params.put("UserId",getUserId().intValue());
		workBookService.updateAll(params);
		Map<String, Object> map = new HashMap<>();
		map.put("data", "success");
		return RD.success(map);

	}

	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("workBook:workbook:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		workBookService.deleteBookByIds(ids);

		return RD.ok(RD.build());

	}

	/**
	 * 通过id生成报表
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/createReport")
	// @RequiresPermissions("workBook:workbook:createReport")
	public R createReport(@RequestBody Map<String, Object> params) {
		Integer deptId = getUserDeptId().intValue();
		params.put("deptId", deptId);
		Long userId = getUserId();
		params.put("userId" ,userId.intValue());
		workBookService.createReports(params);
		return R.ok();
	}

	/**
	 * 部门下的报表组
	 * @param
	 * @return
	 */
	@RequestMapping("/deptreportgroup")
	public List<ReportGroupEntity> deptReportGroup(){
		Integer deptId = getUserDeptId().intValue();
		List<ReportGroupDeptRelaEntity> reportGroupDeptRelaEntityList = reportGroupDeptRelaService.selectList(new EntityWrapper<ReportGroupDeptRelaEntity>().eq("dept_id", deptId));
		List<ReportGroupEntity> reportGroupEntities = new ArrayList<>();
		reportGroupDeptRelaEntityList.forEach(i->{
			reportGroupEntities.add(reportGroupService.selectById(i.getReportGroupId()));
		});
		for(ReportGroupEntity reportGroupEntity : reportGroupEntities){
			List<ReportEntity> reportEntityList = new ArrayList<>();
			List<ReportGroupReportRelaEntity> reportGroupReportRelaEntities = reportGroupReportRelaService.selectList(new EntityWrapper<ReportGroupReportRelaEntity>().eq("report_group_id", reportGroupEntity.getId()));
			for(ReportGroupReportRelaEntity item:reportGroupReportRelaEntities){
				reportEntityList.add(reportService.selectById(item.getReportId()));
			}
			String name = "";
			for (ReportEntity item : reportEntityList) {
				name += item.getName() + "/";
			}
			reportGroupEntity.setAllReportName(name);
			reportGroupEntity.setCreateName(staffService.selectNameByUserId(reportGroupEntity.getCreateBy()));
			reportGroupEntity.setUpdateName(staffService.selectNameByUserId(reportGroupEntity.getCreateBy()));
		}
		return reportGroupEntities;
	}


	/**
	 * 通过id报表总数
	 */
	@RequestMapping("/reportTotal/{id}")
	public int wrokBookTotal(@PathVariable Integer id){
		WorkBookEntity workBookEntity= workBookService.selectById(id);
		EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("stlst",workBookEntity.getStlst()).eq("version_number",workBookEntity.getVersionNumber())
				.eq("destinations",workBookEntity.getDestinations()).eq("model_id",workBookEntity.getModelId())
				.eq("phase_id",workBookEntity.getPhaseId());
		List<WorkBookEntity> workBookEntityList = workBookService.selectList(entityWrapper);
		return workBookEntityList.size();
	}

	/**
	 * 报表总数
	 */
	@RequestMapping("/reporttotalbyfive")
	public int wrokBookTotalByFive(@RequestBody Map<String, Object> params){
		EntityWrapper<WorkBookEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("stlst",params.get("stlst")).eq("version_number",params.get("versionNumber"))
				.eq("destinations",params.get("destinations")).eq("model_id",params.get("modelId"))
				.eq("phase_id",params.get("phaseId"));
		List<WorkBookEntity> workBookEntityList = workBookService.selectList(entityWrapper);
		return workBookEntityList.size();
	}


	/**
	 * 部门报表
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/deptreport/{id}")
	// @RequiresPermissions("workBook:workbook:createReport")
	public ResponseEntity<Object> dpetReport(@PathVariable Integer id) {
		List<ReportEntity> reportEntityList = workBookService.deptReports(id);
		for(ReportEntity item : reportEntityList){
			item.setDeptId(getUserDeptId().intValue());
		}
		return RD.success(reportEntityList);
	}
	/**
	 * 判断锁定以及重新设置锁定时间
	 */
	@RequestMapping("/lock")
	public ResponseEntity<Object> lock(@RequestParam Integer id) {
		WorkBookEntity workBookEntity = workBookService.selectById(id);
		Integer lockId = workBookEntity.getLockBy();
		if(lockId == null){
			workBookEntity.setLockBy(getUserId().intValue());
			workBookEntity.setLockAt(new Date());
			workBookService.updateById(workBookEntity);
		}else if(lockId == getUserId().intValue()){
			workBookEntity.setLockAt(new Date());
			workBookService.updateById(workBookEntity);
		}else {
			return RD.FORBIDDEN("LOCKED","有人正在编辑");
		}
		return RD.success(workBookEntity);
	}

	@RequestMapping("/unlock")
	public ResponseEntity<Object> unlock(@RequestParam Integer id) {
		WorkBookEntity workBookEntity = workBookService.selectById(id);
		if(workBookEntity.getLockBy().equals(getUserId().intValue())){
			workBookEntity.setLockBy(null);
			workBookEntity.setLockAt(null);
			workBookService.updateById(workBookEntity);
			return RD.success(workBookEntity);
		}else {
			return RD.FORBIDDEN("LOCKED","解锁失败,权限不够");
		}

	}

	@RequestMapping("/deptmodel")
	public Boolean deptModel(@RequestBody Map<String,Integer> map) {
		Integer deptId = map.get("deptId");
		Integer modelId = map.get("modelId");
		List<ModelEntity> modelEntityList = modelService.selectList(new EntityWrapper<ModelEntity>().eq("dept_id", deptId));
		List<Integer> modelIds = new ArrayList<>();
		modelEntityList.forEach(i->{
			modelIds.add(i.getId());
		});
		if(modelIds.contains(modelId)){
			return true;
		}else{
			return false;
		}
	}

	@RequestMapping("/modelworkstation")
	public Boolean modelWorkstation(@RequestBody Map<String,Object> map) {
		List<Object> workstationId = Collections.singletonList(map.get("workstationId"));
		Integer modelId = (Integer) map.get("modelId");
		List<ModelWorkstationRelaEntity> modelWorkstationRelaEntityList = modelWorkstationRelaService.selectList(new EntityWrapper<ModelWorkstationRelaEntity>().eq("model_id", modelId));
		List<Integer> workstationIds = new ArrayList<>();
		modelWorkstationRelaEntityList.forEach(i->{
			workstationIds.add(i.getWorkstationId());
		});
		if(workstationIds.containsAll(workstationId)){
			return true;
		}else{
			return false;
		}
	}
}
