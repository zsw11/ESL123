package io.apj.modules.masterData.controller;

import java.util.*;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.*;
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

import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.service.ActionService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;

/**
 * 关键词
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/action")
public class ActionController extends AbstractController {
	@Autowired
	private ActionService actionService;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
//    @RequiresPermissions("masterData:action:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = actionService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
//	@RequiresPermissions("masterData:action:detail")
	public RD info(@PathVariable("id") Integer id) {
		ActionEntity action = actionService.selectById(id);

		return RD.build().put("data", action);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:action:create")
	public RD save(@RequestBody ActionEntity action) {
		action.setCreateBy(getUserId().intValue());
		action.setDeptId(getUserDeptId().intValue());
		action.setPinyin(PinyinUtil.getPinYin(action.getName()));
		actionService.insert(action);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:action:update")
	public RD update(@RequestBody ActionEntity action) {
		action.setDeptId(getUserDeptId().intValue());
		action.setUpdateBy(getUserId().intValue());
		actionService.updatePinAndDataById(action);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:action:delete")
	public RD delete(@RequestBody Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("action", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.build().put("msg", reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("action", ids[i].longValue());
			}
		}
		actionService.deleteByIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出关键词")
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
		PageUtils pageUtils = actionService.queryPage(params);
		List<ActionEntity> actionEntityList = (List<ActionEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (ActionEntity item : actionEntityList) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("action", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "action", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "关键字.xlsx", data);
	}
	/**
	 * 导入
	 *
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/import")
	public ResponseEntity importExcel(@RequestBody Map<String, Object> map) {
		map.put("userID", getUserId().intValue());
		map.put("deptId", getUserDeptId().intValue());
		ResponseEntity responseEntity = actionService.actionImport(map);
		return responseEntity;
	}

}
