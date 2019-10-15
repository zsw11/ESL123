package io.apj.modules.sys.controller;

import java.util.Map;
import java.util.Date;
import java.util.List;

import io.apj.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.sys.entity.CodeRuleItemEntity;
import io.apj.modules.sys.service.CodeRuleItemService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.validator.ValidatorUtils;


/**
 * 编码规则子项
 *
 * @author Sam
 *
 * @date 2018-12-18 11:34:01
 */
@RestController
@RequestMapping("/api/v1/coderuleitem")
public class CodeRuleItemController extends AbstractController{
    @Autowired
    private CodeRuleItemService codeRuleItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:coderuleitem:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = codeRuleItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:coderuleitem:info")
    public R info(@PathVariable("id") Long id){
			CodeRuleItemEntity codeRuleItem = codeRuleItemService.selectById(id);

        return R.ok().put("codeRuleItem", codeRuleItem);
    }

    /**
     * 保存
     */
    @SysLog("保存编码规则子项")
    @RequestMapping("/save")
    @RequiresPermissions("sys:coderuleitem:save")
    public R save(@RequestBody CodeRuleItemEntity codeRuleItem){
    		codeRuleItem.setCreateBy(getUserId());
    		codeRuleItem.setCreateAt(new Date());
    		codeRuleItem.setUpdateAt(new Date());
    		ValidatorUtils.validateEntity(codeRuleItem);
			codeRuleItemService.save(codeRuleItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改编码规则子项")
    @RequestMapping("/update")
    @RequiresPermissions("sys:coderuleitem:update")
    public R update(@RequestBody CodeRuleItemEntity codeRuleItem){
    		codeRuleItem.setUpdateBy(getUserId());
    		codeRuleItem.setUpdateAt(new Date());
    		ValidatorUtils.validateEntity(codeRuleItem);
			codeRuleItemService.update(codeRuleItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除编码规则子项")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:coderuleitem:delete")
    public R delete(@RequestBody Long[] ids){
			codeRuleItemService.deleteBatch(ids);

        return R.ok();
    }

    /*
	 * 高级查询
	 */
	@RequestMapping("/advancedSearch")
	public R advancedSearch(@RequestParam Map<String, Object> params) {
		List<CodeRuleItemEntity> list = codeRuleItemService.advancedSearch(params);
		PageUtils page = new PageUtils(list, 0, 10, 0);
		return R.ok().put("page", page);
	}

}
