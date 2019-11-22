package io.apj.modules.basic.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.Constant;
import io.apj.common.utils.DataUtils;
import io.apj.common.utils.DateUtils;
import io.apj.common.utils.ExcelData;
import io.apj.common.utils.ExportExcelUtils;
import io.apj.common.utils.ImportExcelUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.basic.entity.FilterEntity;
import io.apj.modules.basic.entity.JobEntity;
import io.apj.modules.basic.service.JobService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.SysDictService;

/**
 * 岗位信息
 *
 * @author Royluo
 *
 * @date 2018-12-11 13:28:07
 */
@RestController
@RequestMapping("basic/job")
public class JobController extends AbstractController {
	@Autowired
	private JobService jobService;
	@Autowired
	private SysDictService sysDictService;
	@Value("classpath:static/query-configuration/areaData.json")
	private Resource areaData;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("basic:job:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = jobService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("basic:job:info")
	public R info(@PathVariable("id") Long id) {
		JobEntity job = jobService.selectById(id);

		return R.ok().put("job", job);
	}

	/**
	 * 获取所有数据列表
	 */
	@RequestMapping("/selectAll")
//	@RequiresPermissions("basic:job:selectAll")
	public R selectAll() {

		EntityWrapper<JobEntity> wrapper = new EntityWrapper<JobEntity>();

		List<JobEntity> list = jobService.selectList(wrapper);

		return R.ok().put("data", list);
	}

	/**
	 * 保存
	 */
	@SysLog("保存岗位")
	@RequestMapping("/save")
	@RequiresPermissions("basic:job:save")
	public R save(@RequestBody JobEntity job) {
		job.setCreateBy(getUserId());
		job.setCreateAt(new Date());
		job.setUpdateAt(new Date());
		job.setPinyin(PinyinUtil.getPinYin(job.getName()));
		ValidatorUtils.validateEntity(job);
		jobService.save(job);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改岗位")
	@RequestMapping("/update")
	@RequiresPermissions("basic:job:update")
	public R update(@RequestBody JobEntity job) {
		job.setUpdateBy(getUserId());
		job.setUpdateAt(new Date());
		job.setPinyin(PinyinUtil.getPinYin(job.getName()));
		ValidatorUtils.validateEntity(job);
		jobService.update(job);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除岗位")
	@RequestMapping("/delete")
	@RequiresPermissions("basic:job:delete")
	public R delete(@RequestBody Long[] ids) {
		// 检查是否存在引用
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("basic_job", ids[i]);
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return R.error("表名：" + reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("basic_job", ids[i]);
			}
		}
		jobService.deleteBatch(ids);

		return R.ok();
	}

	/*
	 * 高级查询
	 */
	@RequestMapping("/select")
	public R select(@RequestBody List<FilterEntity> filterJson) {
		EntityWrapper<JobEntity> wrapper = new EntityWrapper<>();
		for (FilterEntity entity : filterJson) {
			switch (entity.getCode()) {
			case "like":
				wrapper.like(entity.getColumn(), entity.getParams().toString());
				break;
			case "eq":
				wrapper.eq(entity.getColumn(), entity.getParams());
				break;
			case "ge":
				wrapper.ge(entity.getColumn(), entity.getParams());
				break;
			case "le":
				wrapper.le(entity.getColumn(), entity.getParams());
				break;
			case "between":
				wrapper.between(entity.getColumn(), entity.getParams().get(0), entity.getParams().get(1));
				break;
			default:
				break;
			}
		}
		List<JobEntity> list = jobService.selectList(wrapper);
		return R.ok().put("list", list);
	}

	/**
	 * 获取Excel模板
	 *
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/ecxelTemplate")
	public void ecxelTemplate(HttpServletResponse response) throws Exception {
		ExcelData data = new ExcelData();
		List<String> titles = new ArrayList<String>();
		List<List<Object>> rows = new ArrayList<List<Object>>();
		List<Object> row = new ArrayList<Object>();
		// 表头数据
		data.setName("岗位信息");
		titles.add("岗位编码");
		titles.add("岗位名称");
		titles.add("工程师等级");
		data.setTitles(titles);
		row.add("T00001");
		row.add("维修工程师");
		row.add("一级");
		rows.add(row);
		data.setRows(rows);
		ExportExcelUtils.exportExcel(response, "岗位信息模板.xlsx", data);
	}

	/**
	 * 提供数据源导出Excel
	 *
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/exportExcelByList")
	public R exportExcelByList(HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) params.get("list");
		if (list == null)
			return R.error(403, "请提供数据源！");
		ExcelData data = new ExcelData();
		List<String> titles = new ArrayList<String>();
		List<List<Object>> rows = new ArrayList<List<Object>>();
		// 表头数据
		data.setName("岗位信息");
		titles.add("岗位编码");
		titles.add("岗位名称");
		titles.add("工程师等级");
		data.setTitles(titles);
		for (HashMap<String, Object> item : list) {
			List<Object> row = new ArrayList<Object>();
			row.add(item.get("code"));
			row.add(item.get("name"));
			row.add(item.get("grade"));
			rows.add(row);
		}
		data.setRows(rows);
		ExportExcelUtils.exportExcel(response, "岗位信息模板.xlsx", data);
		return R.ok();
	}

	/**
	 * 批量导入数据
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/importExcel")
//  @RequiresPermissions("basic:job::importExcel")
	public R importExcel(MultipartFile file) throws Exception {
		List<JobEntity> list = new ArrayList<>();
		JobEntity entity;
		Boolean rowStatus = false;

		if (file == null)
			return R.error(403, "请上传Excel文件！");
		Map<String, Object> map = ImportExcelUtils.readExcelData(file);// 读取Excel数据
		if (map.get("status").equals(403)) {
			return R.error(403, map.get("message").toString());
		}
		Sheet sheet = (Sheet) map.get("sheet");

		for (int r = 1; r <= sheet.getLastRowNum(); r++) {// r = 1 表示从第2行开始循环
			Row row = sheet.getRow(r); // 通过sheet表单对象得到 行对象
			if (row == null) { // 行对象为null跳出循环
				continue;
			}
			entity = new JobEntity();
			String name = row.getCell(0).getStringCellValue();// 得到每一行第一个单元格的值
			if (name == null || name.isEmpty()) {// 判断是否为空
				rowStatus = true;
				System.out.println("导入失败(第" + (r + 1) + "行,岗位名称未填写)");
			}

			row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);// 得到每一行的 第二个单元格的值
			String code = row.getCell(1).getStringCellValue();

			if (code == null || code.isEmpty()) {
				rowStatus = true;
				System.out.println("导入失败(第" + (r + 1) + "行,岗位编码未填写)");
			}

			row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);// 得到每一行的 第二个单元格的值
			String grade = row.getCell(1).getStringCellValue();

			if (grade == null || grade.isEmpty()) {
				rowStatus = true;
				System.out.println("导入失败(第" + (r + 1) + "行,工程师等级未填写)");
			}
			// 完整的循环一次 就组成了一个对象
			entity.setName(name);
			entity.setCode(code);
			entity.setGrade(grade);
			entity.setCreateBy(getUserId());
			entity.setCreateAt(new Date());
			entity.setUpdateAt(new Date());
			entity.setPinyin(PinyinUtil.getPinYin(entity.getName()));

			EntityWrapper<JobEntity> wrapper = new EntityWrapper<JobEntity>();
			if (rowStatus) { // 异常数据
				list.add(entity);
			} else { // 插入表数据
				JobEntity job = jobService.selectOne(wrapper.eq("code", entity.getCode()));
				if (job != null) { // 存在则不插入
					list.add(entity);
				} else {
					jobService.insert(entity);
				}
			}
		}

		return R.ok().put("data", list).put("total", list.size());

	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出岗位信息")
	@RequestMapping("/exportExcel")
	public R exportExcel(HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
		// 过滤字段，前端传
		List<String> list = (List<String>) map.get("exportAttributes");
		List<JobEntity> jobEntities;
		// 普通查询
		Map<String, Object> params = (Map<String, Object>) map.get("filters");
		if (params == null) {
			params = new HashMap<>();
		}
		params.put("page", "1");
		params.put("limit", "9999999");
		PageUtils pageUtils = jobService.queryPage(params);
		jobEntities = (List<JobEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dictMap = sysDictService.getDictDetail();
		String jsonStr = IOUtils.toString(areaData.getInputStream(), Charset.forName("UTF-8"));
		Gson gson = new Gson();
		JsonObject areaDataFilter = gson.fromJson(jsonStr, JsonObject.class);
		for (JobEntity item : jobEntities) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("job", item, dictMap);
			dataList.add(arr);
		}
		// 返回excel数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "job", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "岗位信息.xlsx", data);
		return R.ok();
	}

	/**
	 * 导入
	 *
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/import")
	public R importExcel(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<JobEntity> jobEntityList = new ArrayList<>();
		Set<String> codeCheck = new HashSet<>();
		for (int i = 0; i < maps.size(); i++) {
			JobEntity jobEntity = new JobEntity();
			// JobMap
			Map<String, Object> jobMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				if ("code".equals(keyStrs[1])) {
					// 检查导入数据重复
					if (!codeCheck.add(value.toString())) {
						return R.error(500, "第" + (i + 1) + "行，导入数据岗位编码【" + value.toString() + "】存在重复！");
					}
					// 判断code唯一
					Wrapper<JobEntity> jobEntityWrapper = new EntityWrapper<JobEntity>().eq("code", value.toString());
					JobEntity jobCode = jobService.selectOne(jobEntityWrapper);
					if (jobCode != null) {
						return R.error("第" + (i + 1) + "行，该岗位编码已存在！");
					}
				}
				jobMap.put(keyStrs[1], value);
			}
			// map转实体
			DataUtils.transMap2Bean2(jobMap, jobEntity);
			jobEntity.setPinyin(jobEntity.getName());
			jobEntity.setCreateBy(getUserId());
			jobEntity.setCreateAt(new Date());
			jobEntity.setUpdateAt(new Date());
			// 校验
			ValidatorUtils.validateEntity(jobEntity, i);
			jobEntityList.add(jobEntity);
		}
		try {
			jobService.insertBatch(jobEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException(e.getMessage(), 500);
		}
		return R.ok();
	}

	/**
	 * 测试导入
	 */
	@PostMapping("testImport")
	public RD<JsonElement> testImport(MultipartFile file) {
		List<JobEntity> jobList = new ArrayList<JobEntity>();
		try {
			Map map = ImportExcelUtils.readExcelData(file);
			List<Map<Integer, String>> valueList = (List<Map<Integer, String>>) map.get("list");
			for (Map<Integer, String> item : valueList) {
				if (item.get(0) == null || "".equals(item.get(0))) {
					return new RD<JsonElement>((Integer) 403, "403", "某列不能为空");
				} else {
					int num = 0;
					JobEntity job = new JobEntity();
					job.setCode(item.get(0));
					job.setName(item.get(1));
					job.setPinyin(PinyinUtil.getPinYin(item.get(1)));
					job.setGrade(item.get(2));
					job.setCreateAt(new Date());
					job.setCreateBy(getUserId());
					jobList.add(job);
				}
			}
			jobService.insertBatch(jobList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RD.build();
	}

	/**
	 * 测试导出
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "testExport", produces = "application/json;charset=UTF-8")
	public void testExport(HttpServletResponse response) throws Exception {
		EntityWrapper<JobEntity> jobWrapper = new EntityWrapper<>();
		List<JobEntity> jobList = jobService.selectList(jobWrapper);
		List<String> titles = new ArrayList<>(
				Arrays.asList("岗位编码", "岗位名称", "pinyin", "岗位等级", "创建者角色id", "创建时间", "更新者id", "更新时间", "删除时间"));
		List<List<Object>> rows = new ArrayList<>();
		for (JobEntity item : jobList) {
			List<Object> list = new ArrayList<>();
			list.add(item.getCode());
			list.add(item.getName());
			list.add(item.getPinyin());
			list.add(item.getGrade());
			list.add(item.getCreateBy());
			list.add(item.getCreateAt());
			list.add(item.getUpdateBy());
			list.add(item.getUpdateAt());
			list.add(item.getDeleteAt());
			rows.add(list);
		}
		ExcelData data = new ExcelData();
		data.setName("岗位");
		data.setTitles(titles);
		data.setRows(rows);
		ExportExcelUtils.exportExcel(response, "岗位信息.xlsx", data);
	}

}
