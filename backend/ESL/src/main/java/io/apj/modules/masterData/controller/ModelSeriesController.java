package io.apj.modules.masterData.controller;

import java.util.*;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.service.ModelService;
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

import io.apj.modules.masterData.entity.ModelSeriesEntity;
import io.apj.modules.masterData.service.ModelSeriesService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;

/**
 * 机种系列
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/modelseries")
public class ModelSeriesController extends AbstractController {
	@Autowired
	private ModelSeriesService modelSeriesService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private ModelService modelService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:modelseries:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = modelSeriesService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:modelseries:info")
	public RD info(@PathVariable("id") Integer id) {
		ModelSeriesEntity modelSeries = modelSeriesService.selectById(id);

		return RD.build().put("data", modelSeries);
	}

	/**
	 * 机种系列下的机种
	 */
	@RequestMapping("/modeldetail/{id}")
	@RequiresPermissions("masterData:modelseries:info")
	public RD modelinfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
		PageUtils models = modelService.selectBySeriesId(id, params);

		return RD.build().put("page", models);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:modelseries:create")
	public RD save(@RequestBody ModelSeriesEntity modelSeries) {
		modelSeries.setCreateBy(getUserId().intValue());
		modelSeries.setPinyin(PinyinUtil.getPinYin(modelSeries.getName()));
		modelSeriesService.insert(modelSeries);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:modelseries:update")
	public RD update(@RequestBody ModelSeriesEntity modelSeries) {
		modelSeries.setUpdateBy(getUserId().intValue());
		modelSeriesService.updatePinAndDataById(modelSeries);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:modelseries:delete")
	public  ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("modelSeries", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.INTERNAL_SERVER_ERROR(reference.getByEntity() + "，id=" + reference.getById()
							+ " 在表：" + reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("modelSeries", ids[i].longValue());
			}
		}
		modelSeriesService.deleteByIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出机种系列")
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
		PageUtils pageUtils = modelSeriesService.queryPage(params);
		List<ModelSeriesEntity> modelSeriesEntityList = (List<ModelSeriesEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (ModelSeriesEntity item : modelSeriesEntityList) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("modelSeries", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "modelSeries", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "机种系列.xlsx", data);
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
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		for(Map<String, Object> i:maps ){
			if(StringUtils.isEmpty((CharSequence) i.get("modelSeries.name"))){
				return RD.INTERNAL_SERVER_ERROR("导入时机种系列名称为空，请检查机种系列是否为空");
			}
		}
		List<ModelSeriesEntity> modelSeriesEntityList = new ArrayList<>();
		for (int i = 0; i < maps.size(); i++) {
			ModelSeriesEntity ModelSeriesEntity = new ModelSeriesEntity();
			// deviceMap
			Map<String, Object> deviceMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 设备
                if (keyStrs[0].equals("modelSeries")) {
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
			DataUtils.transMap2Bean2(deviceMap, ModelSeriesEntity);
			ValidatorUtils.validateEntity(ModelSeriesEntity, i);
			ModelSeriesEntity.setCreateBy(getUserId().intValue());
			modelSeriesEntityList.add(ModelSeriesEntity);
		}
		try {
			modelSeriesService.insertBatch(modelSeriesEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("机种系列导入失败，请检查机种系列是否重复", 500);
		}
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("code", 200);
		return RD.success(data);
	}

}
