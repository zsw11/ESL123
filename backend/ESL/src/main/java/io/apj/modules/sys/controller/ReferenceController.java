package io.apj.modules.sys.controller;

import java.util.Map;
import java.util.List;

import io.apj.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.ReferenceService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.validator.ValidatorUtils;


/**
 * 引用表
 *
 * @author henry
 *
 * @date 2019-01-14 14:55:47
 */
@RestController
@RequestMapping("sys/reference")
public class ReferenceController extends AbstractController {
    @Autowired
    private ReferenceService referenceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:reference:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = referenceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:reference:info")
    public R info(@PathVariable("id") Long id) {
        ReferenceEntity reference = referenceService.selectById(id);

        return R.ok().put("reference", reference);
    }

    /**
     * 保存
     */
    @SysLog("保存引用表")
    @RequestMapping("/save")
    @RequiresPermissions("sys:reference:save")
    public R save(@RequestBody ReferenceEntity reference) {
        ValidatorUtils.validateEntity(reference);
        referenceService.save(reference);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改引用表")
    @RequestMapping("/update")
    @RequiresPermissions("sys:reference:update")
    public R update(@RequestBody ReferenceEntity reference) {
        ValidatorUtils.validateEntity(reference);
        referenceService.update(reference);

        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除引用表")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:reference:delete")
    public R delete(@RequestBody Long[] ids) {
        referenceService.deleteBatch(ids);

        return R.ok();
    }

    /*
     * 高级查询
     */
    @RequestMapping("/advancedSerach")
    public R advancedSerach(@RequestParam Map<String, Object> params) {
        List<ReferenceEntity> list = referenceService.advancedSerach(params);
        PageUtils page = new PageUtils(list, 0, 10, 0);
        return R.ok().put("page", page);
    }

}
