package io.apj.modules.masterData.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;

import io.apj.modules.masterData.entity.ModelEntity;
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
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.PartService;
import io.apj.modules.sys.controller.AbstractController;
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
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:part:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = partService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:part:info")
    public RD info(@PathVariable("id") Integer id){
		PartEntity part = partService.selectById(id);

        return RD.build().put("data", part);
    }
	/**
	 * 当前部品下的机种信息
	 * @return
	 */
	@RequestMapping("/modeldetail/{id}")
	@RequiresPermissions("masterData:part:info")
	public ResponseEntity<Object> modelInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params){
//		PartEntity part = partService.selectById(id);
		List<ModelEntity> page = modelPartRelaService.selectModelByPartId(id, params);

		return RD.ok( page);
	}

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:part:save")
    public RD save(@RequestBody PartEntity part){
    	part.setCreateBy(getUserId().intValue());
		partService.insert(part);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:part:update")
    public RD update(@RequestBody PartEntity part){
		partService.updateById(part);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:part:delete")
    public RD delete(@RequestBody Integer[] ids){
		partService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }
    
    /**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出设备信息")
	@RequestMapping(value = "/exportExcel",produces="application/json;charset=UTF-8")
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
	public RD importExcel(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<PartEntity> partEntityList = new ArrayList<>();
		for (int i = 0; i < maps.size(); i++) {
			PartEntity partEntity = new PartEntity();
			// deviceMap
			Map<String, Object> deviceMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 设备
				if (keyStrs[0].equals("part")) {
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
			DataUtils.transMap2Bean2(deviceMap, partEntity);
			ValidatorUtils.validateEntity(partEntity, i);
			partEntity.setCreateBy(getUserId().intValue());
			partEntityList.add(partEntity);
		}
		try {
			partService.insertBatch(partEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException("批量导入失败", 500);
		}
		return RD.build().put("code", 0);
	}

}
