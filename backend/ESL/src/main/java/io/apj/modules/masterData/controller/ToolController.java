package io.apj.modules.masterData.controller;

import java.util.*;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.JsonElement;
import io.apj.common.annotation.SysLog;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.ModelToolRelaService;
import io.apj.modules.sys.controller.AbstractController;
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

import io.apj.modules.masterData.entity.ToolEntity;
import io.apj.modules.masterData.service.ToolService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;


/**
 * 治工具
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/tool")
public class ToolController extends AbstractController {
    @Autowired
    private ToolService toolService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelToolRelaService modelToolRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:tool:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = toolService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:tool:info")
    public RD info(@PathVariable("id") Integer id){
		ToolEntity tool = toolService.selectById(id);
        ModelEntity modelEntity = modelService.selectById(id);
        HashMap<Object, Object> data = new HashMap<>();
        data.put("tool", tool);
        data.put("modelEntity", modelEntity);

        return RD.build().put("data", tool);
    }

    /**
     * 当前治工具下的机种信息
     * @return
     */
    @RequestMapping("/modeldetail/{id}")
    @RequiresPermissions("masterData:part:info")
    public ResponseEntity<Object> modelInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params){
        Page<Map<String,Object>> page = modelToolRelaService.selectModelByToolId(id, params);

        return RD.ok( page);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:tool:save")
    public RD save(@RequestBody ToolEntity tool){
        tool.setCreateBy(getUserId().intValue());
		toolService.insert(tool);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:tool:update")
    public RD update(@RequestBody ToolEntity tool){
		toolService.updateById(tool);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:tool:delete")
    public RD delete(@RequestBody Integer[] ids){
		toolService.deleteBatchIds(Arrays.asList(ids));

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
        //page和数据对象
        PageUtils pageUtils = toolService.queryPage(params);
        List<ToolEntity> ToolEntities = (List<ToolEntity>) pageUtils.getData();
        // 处理数据源, dataList接受所有的导出数据
        List<Map<String, Object>> dataList = new ArrayList<>();
        // 字典项数据
        HashMap<String, String> dict = sysDictService.getDictDetail();
        for (ToolEntity item : ToolEntities) {
            // 处理数据源
            Map<String, Object> arr = DataUtils.dataChange("tool", item, dict);
            dataList.add(arr);
        }
        // 返回excel数据
        Map<String, Object> param = DataUtils.rtlExcelData(list, "tool", dataList);
        ExcelData data = new ExcelData();
        data.setTitles((List<String>) param.get("titles"));
        data.setRows((List<List<Object>>) param.get("rows"));
        // 导出
        String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
        ExportExcelUtils.exportExcel(response, datetime + "治工具.xlsx", data);
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
        List<ToolEntity> toolEntityList = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            ToolEntity toolEntity = new ToolEntity();
            // deviceMap,接受每一行数据
            Map<String, Object> deviceMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String[] keyStrs = key.split("\\.");
                // 设备
                if (keyStrs[0].equals("tool")) {
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
            //map转javabean
            DataUtils.transMap2Bean2(deviceMap, toolEntity);
            ValidatorUtils.validateEntity(toolEntity, i);
            toolEntity.setCreateBy(getUserId().intValue());
            toolEntityList.add(toolEntity);
        }
        try {
            toolService.insertBatch(toolEntityList, Constant.importNum);
        } catch (MybatisPlusException e) {
            throw new RRException("批量导入失败", 500);
        }
        return RD.build().put("code", 0);
    }

}
