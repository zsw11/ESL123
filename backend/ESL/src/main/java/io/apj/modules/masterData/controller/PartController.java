package io.apj.modules.masterData.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.JsonElement;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;

import io.apj.modules.masterData.service.ModelPartRelaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.PartService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.SysDictService;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.RD;

/**
 * 部品
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/part")
public class PartController extends AbstractController {
	@Autowired
	private PartService partService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private ModelPartRelaService modelPartRelaService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:part:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = partService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:part:info")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		PartEntity part = partService.selectById(id);

		return RD.ok(part);
	}

	/**
	 * 当前部品下的机种信息
	 * 
	 * @return
	 */
	@RequestMapping("/modeldetail/{id}")
	@RequiresPermissions("masterData:part:info")
	public ResponseEntity<Object> modelInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils page = partService.partModeRelaList(id, params);

		return RD.ok(page);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:part:create")
	public ResponseEntity<Object> save(@RequestBody PartEntity part) {
		part.setPinyin(PinyinUtil.getPinYin(part.getName()));
		part.setCreateBy(getUserId().intValue());
		partService.insert(part);

		return RD.ok(part);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:part:update")
	public ResponseEntity<Object> update(@RequestBody PartEntity part) {
		part.setUpdateBy(getUserId().intValue());
		partService.updatePinAndDataById(part);

		return RD.ok(part);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:part:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("part", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.INTERNAL_SERVER_ERROR(reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("part", ids[i].longValue());
			}
		}
		partService.deleteByIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出部件")
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
		PageUtils pageUtils = partService.queryPage(params);
		List<PartEntity> partEntities = (List<PartEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		// 字典项数据
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (PartEntity item : partEntities) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("part", item, dict);
			if(arr.get("part.common")=="true"){
				arr.put("part.common","是");
			}else if(arr.get("part.common")=="false"){
				arr.put("part.common","否");
			}
			dataList.add(arr);
		}
		// 返回excel数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "part", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "部品信息.xlsx", data);
//		return RD.build();
	}

	/**
	 * 导入
	 * 
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/import")
	public ResponseEntity<Object> importExcel(@RequestBody Map<String, Object> map) {
		map.put("userID", getUserId().intValue());
		ResponseEntity<Object> responseEntity = partService.partImport(map);
		return responseEntity;
	}

}
