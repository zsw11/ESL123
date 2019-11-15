package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

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

import io.apj.modules.masterData.entity.DeptOperationRelaEntity;
import io.apj.modules.masterData.service.DeptOperationRelaService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 组织机构关键词关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/deptoperationrela")
public class DeptOperationRelaController extends AbstractController {
    @Autowired
    private DeptOperationRelaService deptOperationRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:deptoperationrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = deptOperationRelaService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:deptoperationrela:info")
    public R info(@PathVariable("id") Integer id){
		DeptOperationRelaEntity deptOperationRela = deptOperationRelaService.selectById(id);

        return R.ok().put("data", deptOperationRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:deptoperationrela:save")
    public R save(@RequestBody DeptOperationRelaEntity deptOperationRela){
        deptOperationRela.setCreateBy(getUserId().intValue());
		deptOperationRelaService.insert(deptOperationRela);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:deptoperationrela:update")
    public R update(@RequestBody DeptOperationRelaEntity deptOperationRela){
		deptOperationRelaService.updateById(deptOperationRela);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:deptoperationrela:delete")
    public RD delete(@RequestBody Integer[] ids){
		deptOperationRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
