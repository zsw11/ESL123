package io.apj.modules.masterData.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.service.SysDeptService;
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
import io.apj.modules.masterData.service.ModelSeriesService;
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
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private ModelSeriesService modelSeriesService;

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
	 * modelpartRela
	 * 
	 * @return
	 */
	@RequestMapping("/partdetail/{id}")
	public ResponseEntity<Object> partInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils page = modelService.modelPartRelaList(id, params);

		return RD.ok(page);
	}
	/**
	 * 机种工位关系表list
	 */
	@RequestMapping("/workStationDetail/{id}")
	public ResponseEntity<Object> workStationDetail(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils page = modelService.modelWorkStationList(id, params);

		return RD.ok(page);
	}

	/**
	 * modeltoolRela
	 * 
	 * @return
	 */
	@RequestMapping("/tooldetail/{id}")
	public ResponseEntity<Object> toolInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils page = modelService.modelToolRelaList(id, params);

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
		model.setDeptId(getUserDeptId().intValue());
		modelService.insert(model);
		insertTableReference("model", model.getId().longValue(), "modelSeries", model.getModelSeriesId().longValue(),
				false);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:model:update")
	public RD update(@RequestBody ModelEntity model) {
		modelService.updatePinAndDataById(model);
		insertTableReference("model", model.getId().longValue(), "modelSeries", model.getModelSeriesId().longValue(),
				false);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:model:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("model", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.INTERNAL_SERVER_ERROR(reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("model", ids[i].longValue());
			}
		}
		modelService.deleteByIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
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
		EntityWrapper<ModelSeriesEntity> modelSeriesWrapper = new EntityWrapper<>();
		modelSeriesWrapper.isNull("delete_at");
		List<ModelSeriesEntity> modelSeriesSList = modelSeriesService.selectList(modelSeriesWrapper);
		Map<Integer, String> modelSeriesMap = new HashMap<>();
		for (ModelSeriesEntity item : modelSeriesSList) {
			modelSeriesMap.put(item.getId(), item.getName());
		}
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (ModelEntity item : modelEntities) {
			item.setModelSeriesName(modelSeriesMap.get(item.getModelSeriesId()));
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
	 * @throws ParseException
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/import")
	public RD importExcel(@RequestBody Map<String, Object> map) throws ParseException {
		EntityWrapper<SysDeptEntity> deptWrapper = new EntityWrapper<>();
		deptWrapper.isNull("delete_at");
		List<SysDeptEntity> deptList = sysDeptService.selectList(deptWrapper);
		Map<String, String> deptNameMap = new HashMap<>();
		for (SysDeptEntity item : deptList) {
			deptNameMap.put(item.getName(), item.getId().toString());
		}
		EntityWrapper<ModelSeriesEntity> modelSeriesWrapper = new EntityWrapper<>();
		modelSeriesWrapper.isNull("delete_at");
		List<ModelSeriesEntity> modelSeriesSList = modelSeriesService.selectList(modelSeriesWrapper);
		Map<String, Integer> modelSeriesMap = new HashMap<>();
		for (ModelSeriesEntity item : modelSeriesSList) {
			modelSeriesMap.put(item.getName(), item.getId());
		}
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<ModelEntity> modelEntities = new ArrayList<>();
		for (int i = 0; i < maps.size(); i++) {
			ModelEntity modelEntity = new ModelEntity();
			Map<String, Object> buildMap = maps.get(i);
			buildMap.put("modelSeriesId", buildMap.get("modelSeriesName"));
			// 日期强转
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotEmpty((CharSequence) buildMap.get("model.wsTime"))) {
				modelEntity.setWsTime(ft.parse((String) buildMap.get("model.wsTime")));
			}
			if (StringUtils.isNotEmpty((CharSequence) buildMap.get("model.ampTime"))) {
				modelEntity.setAmpTime(ft.parse((String) buildMap.get("model.ampTime")));
			}
			if (StringUtils.isNotEmpty((CharSequence) buildMap.get("model.esTime"))) {
				modelEntity.setEsTime(ft.parse((String) buildMap.get("model.esTime")));
			}
			if (StringUtils.isNotEmpty((CharSequence) buildMap.get("model.mpTime"))) {
				modelEntity.setMpTime(ft.parse((String) buildMap.get("model.mpTime")));
			}
			// modelMap
			Map<String, Object> modelMap = new HashMap<>();
			buildMap.remove("model.wsTime");
			buildMap.remove("model.ampTime");
			buildMap.remove("model.esTime");
			buildMap.remove("model.mpTime");
			for (Map.Entry<String, Object> entry : buildMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 设备
				if (keyStrs[0].equals("model")) {
					if (keyStrs[1].equals("common")) {
						if (value.equals("是")) {
							modelMap.put(keyStrs[1], true);
						} else {
							modelMap.put(keyStrs[1], false);
						}
						continue;
					}
					if (keyStrs[1].equals("deptName")) {
						modelMap.put(keyStrs[1], deptNameMap.get(value));
						continue;
					}
					if (keyStrs[1].equals("modelSeriesId")) {
						modelMap.put(keyStrs[1], modelSeriesMap.get(value));
						continue;
					}
					modelMap.put(keyStrs[1], value);
				}
			}
			// map 转javabean
			DataUtils.transMap2Bean2(modelMap, modelEntity);
			modelEntity.setPinyin(PinyinUtil.getPinYin(modelEntity.getName()));
			modelEntity.setDeptId(getUserId().intValue());
			ValidatorUtils.validateEntity(modelEntity, i);
			modelEntity.setCreateBy(getUserId().intValue());
			modelEntities.add(modelEntity);
		}
		try {
			modelService.insertBatch(modelEntities, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("批量导入失败", 500);
		}
		return RD.build().put("code", 200);
	}

}
