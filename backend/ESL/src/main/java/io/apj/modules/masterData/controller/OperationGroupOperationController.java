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

import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;

/**
 * 手顺
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/operationgroupoperation")
public class OperationGroupOperationController extends AbstractController {
	@Autowired
	private OperationGroupOperationService operationGroupOperationService;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
//    @RequiresPermissions("masterData:operationgroupoperation:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = operationGroupOperationService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:operationgroupoperation:info")
	public RD info(@PathVariable("id") Integer id) {
		OperationGroupOperationEntity operationGroupOperation = operationGroupOperationService.selectById(id);

		return RD.build().put("data", operationGroupOperation);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:operationgroupoperation:create")
	public RD save(@RequestBody OperationGroupOperationEntity operationGroupOperation) {
		operationGroupOperation.setPinyin(PinyinUtil.getPinYin(operationGroupOperation.getOperation()));
		operationGroupOperationService.insert(operationGroupOperation);

		return RD.build().put("status", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:operationgroupoperation:update")
	public RD update(@RequestBody OperationGroupOperationEntity operationGroupOperation) {
		operationGroupOperation.setPinyin(PinyinUtil.getPinYin(operationGroupOperation.getOperation()));
		operationGroupOperationService.updateById(operationGroupOperation);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:operationgroupoperation:delete")
	public RD delete(@RequestBody Integer[] ids) {
		operationGroupOperationService.deleteBatchIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出设备信息")
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
		PageUtils pageUtils = operationGroupOperationService.queryPage(params);
		List<OperationGroupOperationEntity> operationGroupOperationEntityList = (List<OperationGroupOperationEntity>) pageUtils
				.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (OperationGroupOperationEntity item : operationGroupOperationEntityList) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("operationGroupOperation", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "operationGroupOperation", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "手顺.xlsx", data);
	}

}
