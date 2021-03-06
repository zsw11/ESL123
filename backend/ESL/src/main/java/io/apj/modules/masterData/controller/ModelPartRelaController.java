package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonElement;
import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
	 * 
	 * @return
	 */
	@RequestMapping("/list")
//    @RequiresPermissions("masterData:modelpartrela:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = modelPartRelaService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
//    @RequiresPermissions("masterData:modelpartrela:info")
	public RD info(@PathVariable("id") Integer id) {
		ModelPartRelaEntity modelPartRela = modelPartRelaService.selectById(id);

		return RD.build().put("data", modelPartRela);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@Transactional
//    @RequiresPermissions("masterData:modelpartrela:create")
	public RD save(@RequestBody ModelPartRelaEntity modelPartRela) {
		modelPartRela.setCreateBy(getUserId().intValue());
		modelPartRelaService.insert(modelPartRela);
		insertTableReference("model", modelPartRela.getModelId().longValue(), "part",
				modelPartRela.getPartId().longValue(), false);
		insertTableReference("part", modelPartRela.getPartId().longValue(), "model",
				modelPartRela.getModelId().longValue(), false);
		return RD.build().put("status", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
//    @RequiresPermissions("masterData:modelpartrela:update")
	public RD update(@RequestBody ModelPartRelaEntity modelPartRela) {
		modelPartRelaService.updateById(modelPartRela);
		insertTableReference("model", modelPartRela.getModelId().longValue(), "part",
				modelPartRela.getPartId().longValue(), true);
		insertTableReference("part", modelPartRela.getPartId().longValue(), "model",
				modelPartRela.getModelId().longValue(), true);
		return RD.build().put("status", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
//    @RequiresPermissions("masterData:modelpartrela:delete")
	public RD delete(@RequestBody Integer[] ids) {
		for(int i = 0; i < ids.length; i++){
			ModelPartRelaEntity modelPartRelaEntity = modelPartRelaService.selectById(ids[i]);
			deleteTableReference("model", modelPartRelaEntity.getModelId().longValue());
			deleteTableReference("part", modelPartRelaEntity.getPartId().longValue());
		}
		modelPartRelaService.deleteBatchIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

}
