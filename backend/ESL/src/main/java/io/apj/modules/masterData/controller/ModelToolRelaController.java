package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonElement;
import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ModelToolRelaEntity;
import io.apj.modules.masterData.service.ModelToolRelaService;
import io.apj.common.utils.PageUtils;


/**
 * 机种治工具关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/modeltoolrela")
public class ModelToolRelaController extends AbstractController {
    @Autowired
    private ModelToolRelaService modelToolRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
//    @RequiresPermissions("masterData:modeltoolrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = modelToolRelaService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
//    @RequiresPermissions("masterData:modeltoolrela:info")
    public RD<JsonElement> info(@PathVariable("id") Integer id){
		ModelToolRelaEntity modelToolRela = modelToolRelaService.selectById(id);

        return RD.build().put("data", modelToolRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
//    @RequiresPermissions("masterData:modeltoolrela:save")
    public RD<JsonElement> save(@RequestBody ModelToolRelaEntity modelToolRela){
        modelToolRela.setCreateBy(getUserId().intValue());
		modelToolRelaService.insert(modelToolRela);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("masterData:modeltoolrela:update")
    public RD<JsonElement> update(@RequestBody ModelToolRelaEntity modelToolRela){
		modelToolRelaService.updateById(modelToolRela);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("masterData:modeltoolrela:delete")
    public RD<JsonElement> delete(@RequestBody Integer[] ids){
		modelToolRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
