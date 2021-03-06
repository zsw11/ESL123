package io.apj.modules.masterData.controller;

import java.util.*;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.WorkstationService;

import javax.servlet.http.HttpServletResponse;

/**
 * 工位
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstation")
public class WorkstationController extends AbstractController {
	@Autowired
	private WorkstationService workstationService;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:workstation:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = workstationService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:workstation:info")
	public RD info(@PathVariable("id") Integer id) {
		WorkstationEntity workstation = workstationService.selectById(id);

		return RD.build().put("data", workstation);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:workstation:create")
	public RD save(@RequestBody WorkstationEntity workstation) {
		workstation.setPinyin(PinyinUtil.getPinYin(workstation.getName()));
		workstation.setCreateBy(getUserId().intValue());
		workstationService.insert(workstation);

		return RD.build();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:workstation:update")
	public RD update(@RequestBody WorkstationEntity workstation) {
		workstation.setUpdateBy(getUserId().intValue());
		workstationService.updatePinAndDataById(workstation);

		return RD.build();
	}

	/**
	 * 工位机种关系表list
	 */
	@RequestMapping("/modelDetail/{id}")
	public ResponseEntity<Object> modelDetailList(@PathVariable("id") Integer id,
			@RequestParam Map<String, Object> params) {
		PageUtils page = workstationService.modelDetailList(id, params);

		return RD.ok(page);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:workstation:delete")
	public RD delete(@RequestBody Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("workstation", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.build().put("msg", reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("workstation", ids[i].longValue());
			}
		}
		workstationService.deleteByIds(Arrays.asList(ids));

		return RD.build();
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出工位")
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
		PageUtils pageUtils = workstationService.queryPage(params);
		List<WorkstationEntity> workstationEntities = (List<WorkstationEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (WorkstationEntity item : workstationEntities) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("workstation", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "workstation", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "工位.xlsx", data);
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
		ResponseEntity<Object> responseEntity = workstationService.workstationImport(map);
		return responseEntity;
	}

}
