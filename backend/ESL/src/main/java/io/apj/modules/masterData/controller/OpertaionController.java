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

import io.apj.modules.masterData.entity.OpertaionEntity;
import io.apj.modules.masterData.service.OpertaionService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;



/**
 * 关键词
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/opertaion")
public class OpertaionController extends AbstractController {
    @Autowired
    private OpertaionService opertaionService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:opertaion:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = opertaionService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:opertaion:info")
    public R info(@PathVariable("id") Integer id){
		OpertaionEntity opertaion = opertaionService.selectById(id);

        return R.ok().put("opertaion", opertaion);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:opertaion:save")
    public R save(@RequestBody OpertaionEntity opertaion){
        opertaion.setCreateBy(getUserId().intValue());
		opertaionService.insert(opertaion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:opertaion:update")
    public R update(@RequestBody OpertaionEntity opertaion){
		opertaionService.updateById(opertaion);

        return R.ok();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:opertaion:delete")
    public RD delete(@RequestBody Integer[] ids){
		opertaionService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
