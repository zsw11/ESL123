package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.common.utils.PageUtils;


/**
 * 机种部品关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/modelpartrela")
public class ModelPartRelaController extends AbstractController {
    @Autowired
    private ModelPartRelaService modelPartRelaService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
//    @RequiresPermissions("masterData:modelpartrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = modelPartRelaService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
//    @RequiresPermissions("masterData:modelpartrela:info")
    public RD info(@PathVariable("id") Integer id){
		ModelPartRelaEntity modelPartRela = modelPartRelaService.selectById(id);

        return RD.build().put("data", modelPartRela);
    }

//    /**
//     * 部品下的机种信息
//     * @return
//     */
//    @RequestMapping("/modeldetail/{id}")
////    @RequiresPermissions("masterData:modelpartrela:info")
//    public ResponseEntity<Object> modelInfo(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params){
//        PageUtils page = modelPartRelaService.selectModelByPartId(id,params);
//
//        return RD.ok(page);
//    }

    /**
     * 保存
     */
    @RequestMapping("/create")
//    @RequiresPermissions("masterData:modelpartrela:save")
    public RD save(@RequestBody ModelPartRelaEntity modelPartRela){
//        modelPartRela.setCreateBy(getUserId().intValue());
		modelPartRelaService.insert(modelPartRela);
		insertTableReference("model", modelPartRela.getModelId().longValue(), "part", modelPartRela.getPartId().longValue(), false);
        return RD.build().put("code", 200);
    }
    /**
     * 部品下新增机种保存
     */
    @RequestMapping("/createmodelpartrela")
    public RD saveRela(@RequestParam int modelId, @RequestParam int partId){
        ModelPartRelaEntity modelPartRelaEntity = new ModelPartRelaEntity();
        modelPartRelaEntity.setModelId(modelId);
        modelPartRelaEntity.setModelId(partId);

        modelPartRelaService.insertAllColumn(modelPartRelaEntity);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("masterData:modelpartrela:update")
    public RD update(@RequestBody ModelPartRelaEntity modelPartRela){
		modelPartRelaService.updateById(modelPartRela);
		insertTableReference("model", modelPartRela.getModelId().longValue(), "part", modelPartRela.getPartId().longValue(), true);
        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("masterData:modelpartrela:delete")
    public RD delete(@RequestBody Integer[] ids){
		modelPartRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
