package io.apj.modules.sys.controller;

import java.util.Map;
import java.util.Date;

import io.apj.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.sys.entity.FileReferenceEntity;
import io.apj.modules.sys.service.FileReferenceService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.validator.ValidatorUtils;


/**
 * 文件引用关系表
 *
 * @author samchen
 *
 * @date 2019-02-22 16:10:49
 */
@RestController
@RequestMapping("sys/filereference")
public class FileReferenceController extends AbstractController{
    @Autowired
    private FileReferenceService fileReferenceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:filereference:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = fileReferenceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:filereference:info")
    public R info(@PathVariable("id") Long id){
			FileReferenceEntity fileReference = fileReferenceService.selectById(id);

        return R.ok().put("fileReference", fileReference);
    }

    /**
     * 保存
     */
    @SysLog("保存文件引用关系表")
    @RequestMapping("/save")
    @RequiresPermissions("sys:filereference:save")
    public R save(@RequestBody FileReferenceEntity fileReference){
    		fileReference.setCreateAt(new Date());
    		ValidatorUtils.validateEntity(fileReference);
			fileReferenceService.save(fileReference);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改文件引用关系表")
    @RequestMapping("/update")
    @RequiresPermissions("sys:filereference:update")
    public R update(@RequestBody FileReferenceEntity fileReference){
    		ValidatorUtils.validateEntity(fileReference);
			fileReferenceService.update(fileReference);

        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除文件引用关系表")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:filereference:delete")
    public R delete(@RequestBody Long[] ids){
			fileReferenceService.deleteBatch(ids);

        return R.ok();
    }

}
