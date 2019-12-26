package io.apj.modules.workBook.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.common.annotation.SysLog;
import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.workBook.entity.WorkOperationsEntity;
import io.apj.modules.workBook.service.WorkOperationsService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 分析表明细
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:02
 */
@RestController
@RequestMapping("/api/v1/workoperations")
public class WorkOperationsController extends AbstractController {
    @Autowired
    private WorkOperationsService workOperationsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("workBook:workoperations:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = workOperationsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("workBook:workoperations:info")
    public R info(@PathVariable("id") Integer id){
		WorkOperationsEntity workOperations = workOperationsService.selectById(id);

        return R.ok().put("workOperations", workOperations);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("workBook:workoperations:create")
    public R save(@RequestBody WorkOperationsEntity workOperations){
		workOperationsService.insert(workOperations);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("workBook:workoperations:update")
    public R update(@RequestBody WorkOperationsEntity workOperations){
		workOperationsService.updateById(workOperations);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("workBook:workoperations:delete")
    public R delete(@RequestBody Integer[] ids){
		workOperationsService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 导入
     *
     * @param map
     * @return
     */
    @RequestMapping("/import")
    public RD importExcel(@RequestBody Map<String, Object> map) {
        map.put("userID", getUserId().intValue());
        workOperationsService.workOperationImport(map);
        return RD.build().put("code", 200);
    }

    /**
     * 导出excel
     *
     * @return
     * @throws Exception
     */
    @SysLog("导出分析表明细")
    @RequestMapping(value = "/exportExcel", produces = "application/json;charset=UTF-8")
    public RD exportExcel(HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
        map.put("userId",getUserId().intValue());
        workOperationsService.workOperationExport(map,response);
        return RD.build().put("code", 200);
    }

}
