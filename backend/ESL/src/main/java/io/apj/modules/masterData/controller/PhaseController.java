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

import io.apj.modules.masterData.entity.PhaseEntity;
import io.apj.modules.masterData.service.PhaseService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;



/**
 * 生产阶段
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/phase")
public class PhaseController extends AbstractController {
    @Autowired
    private PhaseService phaseService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:phase:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = phaseService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:phase:info")
    public RD info(@PathVariable("id") Integer id){
		PhaseEntity phase = phaseService.selectById(id);

        return RD.build().put("data", phase);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:phase:save")
    public RD save(@RequestBody PhaseEntity phase){
        phase.setCreateBy(getUserId().intValue());
		phaseService.insert(phase);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:phase:update")
    public RD update(@RequestBody PhaseEntity phase){
		phaseService.updateById(phase);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:phase:delete")
    public RD delete(@RequestBody Integer[] ids){
		phaseService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
