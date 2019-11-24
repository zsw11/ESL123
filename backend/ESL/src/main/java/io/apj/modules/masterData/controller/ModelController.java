package io.apj.modules.masterData.controller;

import java.util.*;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.service.impl.SysDictServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;

/**
 * 机种
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/model")
public class ModelController extends AbstractController {
	@Autowired
	private ModelService modelService;
	@Autowired
	private SysDictServiceImpl sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:model:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = modelService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:model:info")
	public RD info(@PathVariable("id") Integer id) {
		ModelEntity model = modelService.selectById(id);

		return RD.build().put("data", model);
	}
	/**
	 *modelpartRela
	 * @return
	 */
	@RequestMapping("/partdetail/{id}")
	public ResponseEntity<Object> partInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils page = modelService.modelPartRelaList(id,params);

		return RD.ok(page);
	}
	/**
	 *modeltoolRela
	 * @return
	 */
	@RequestMapping("/tooldetail/{id}")
	public ResponseEntity<Object> toolInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils page = modelService.modelToolRelaList(id,params);

		return RD.ok(page);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:model:create")
	public RD save(@RequestBody ModelEntity model) {
		model.setCreateBy(getUserId().intValue());
		model.setPinyin(PinyinUtil.getPinYin(model.getName()));
		modelService.insert(model);

		return RD.build();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:model:update")
	public RD update(@RequestBody ModelEntity model) {
		modelService.updateById(model);

		return RD.build();
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:model:delete")
	public RD delete(@RequestBody Integer[] ids) {
		modelService.deleteBatchIds(Arrays.asList(ids));

		return RD.build();
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
		PageUtils pageUtils = modelService.queryPage(params);
		List<ModelEntity> modelEntities = (List<ModelEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (ModelEntity item : modelEntities) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("model", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "model", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "机种.xlsx", data);
	}

	/**
	 * 导入
	 *
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/import")
	public RD importExcel(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<ModelEntity> modelEntities = new ArrayList<>();
		for (int i = 0; i < maps.size(); i++) {
			ModelEntity modelEntity = new ModelEntity();
			// deviceMap
			Map<String, Object> deviceMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 设备
                if (keyStrs[0].equals("model")) {
                    if (keyStrs[1].equals("common")) {
                        if(value.equals("是")) {
                            deviceMap.put(keyStrs[1], true);
                        } else {
                            deviceMap.put(keyStrs[1], false);
                        }
                        continue;
                    }
                    deviceMap.put(keyStrs[1], value);
            }
			}
			DataUtils.transMap2Bean2(deviceMap, modelEntity);
			ValidatorUtils.validateEntity(modelEntity, i);
			modelEntity.setCreateBy(getUserId().intValue());
			modelEntities.add(modelEntity);
		}
		try {
			modelService.insertBatch(modelEntities, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("批量导入失败", 500);
		}
		return RD.build().put("code", 0);
	}

}
