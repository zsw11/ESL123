package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonElement;
import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;
import io.apj.modules.masterData.service.WorkstationTypeNodeService;
import io.apj.common.utils.PageUtils;


/**
 * 工位类型节点
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstationtypenode")
public class WorkstationTypeNodeController extends AbstractController {
    @Autowired
    private WorkstationTypeNodeService workstationTypeNodeService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:workstationtypenode:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
        PageUtils page = workstationTypeNodeService.queryPage(params);
        return RD.ok(page);
    }

    /**
     * nodeType列表
     * @return
     */
    @RequestMapping("/listNodeType")
    public ResponseEntity<JSONArray> listNodeType() {
        return workstationTypeNodeService.listAllNodeType();

    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:workstationtypenode:info")
    public RD<JsonElement> info(@PathVariable("id") Integer id) {
        WorkstationTypeNodeEntity workstationTypeNode = workstationTypeNodeService.selectById(id);

        return RD.build().put("data", workstationTypeNode);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:workstationtypenode:save")
    public RD<JsonElement> save(@RequestBody WorkstationTypeNodeEntity workstationTypeNode) {
        workstationTypeNode.setCreateBy(getUserId().intValue());
        workstationTypeNodeService.insert(workstationTypeNode);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:workstationtypenode:update")
    public RD<JsonElement> update(@RequestBody WorkstationTypeNodeEntity workstationTypeNode) {
        workstationTypeNodeService.updateById(workstationTypeNode);

        return RD.build();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:workstationtypenode:delete")
    public RD<JsonElement> delete(@RequestBody Integer[] ids) {
        workstationTypeNodeService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
