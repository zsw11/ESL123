package io.apj.modules.masterData.controller;

import java.util.*;

import com.google.gson.JsonElement;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.*;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;

/**
 * 生产阶段
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/phase")
public class PhaseController extends AbstractController {
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:phase:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = phaseService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:phase:info")
	public RD info(@PathVariable("id") Integer id) {
		PhaseEntity phase = phaseService.selectById(id);

		return RD.build().put("data", phase);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:phase:create")
	public RD save(@RequestBody PhaseEntity phase) {
		phase.setPinyin(PinyinUtil.getPinYin(phase.getName()));
		phaseService.insert(phase);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:phase:update")
	public RD update(@RequestBody PhaseEntity phase) {
		phaseService.updatePinAndDataById(phase);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:phase:delete")
	public RD delete(@RequestBody Integer[] ids) {
		phaseService.deleteByIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出生产阶段")
	@RequestMapping(value = "/exportExcel", produces = "application/json;charset=UTF-8")
	public void exportExcel(HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
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
		PageUtils pageUtils = phaseService.queryPage(params);
		List<PhaseEntity> phaseEntities = (List<PhaseEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (PhaseEntity item : phaseEntities) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("phase", item, dict);
			dataList.add(arr);
		}
		// 返回excel数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "phase", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "生产阶段.xlsx", data);
	}
}
