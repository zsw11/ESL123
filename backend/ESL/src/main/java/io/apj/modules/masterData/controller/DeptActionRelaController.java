package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

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

import io.apj.modules.masterData.entity.DeptActionRelaEntity;
import io.apj.modules.masterData.service.DeptActionRelaService;
import io.apj.common.utils.PageUtils;


/**
 * 组织机构关键词关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/deptoperationrela")
public class DeptActionRelaController extends AbstractController {
    @Autowired
    private DeptActionRelaService deptActionRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:deptoperationrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = deptActionRelaService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:deptoperationrela:info")
    public RD info(@PathVariable("id") Integer id){
		DeptActionRelaEntity deptActionRela = deptActionRelaService.selectById(id);

        return RD.build().put("data", deptActionRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:deptoperationrela:create")
    public RD save(@RequestBody DeptActionRelaEntity deptActionRela){
        deptActionRela.setCreateBy(getUserId().intValue());
		deptActionRelaService.insert(deptActionRela);
        insertTableReference("dept", deptActionRela.getDeptId().longValue(), "action", deptActionRela.getActionId().longValue(), false);

        return RD.build().put("status", 200);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:deptoperationrela:update")
    public RD update(@RequestBody DeptActionRelaEntity deptActionRela){
		deptActionRelaService.updateById(deptActionRela);
        insertTableReference("dept", deptActionRela.getDeptId().longValue(), "action", deptActionRela.getActionId().longValue(), true);

        return RD.build().put("code", 200);
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:deptoperationrela:delete")
    public RD delete(@RequestBody Integer[] ids){
		deptActionRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build().put("code", 200);
    }

}
