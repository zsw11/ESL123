package io.apj.modules.masterData.controller;

import java.util.*;

import com.google.gson.JsonElement;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.*;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.common.utils.RD;

import javax.servlet.http.HttpServletResponse;

/**
 * 手顺
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/operationgroupoperation")
public class OperationGroupOperationController extends AbstractController {
	@Autowired
	private OperationGroupOperationService operationGroupOperationService;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
//    @RequiresPermissions("masterData:operationgroupoperation:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = operationGroupOperationService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:operationgroupoperation:info")
	public RD info(@PathVariable("id") Integer id) {
		OperationGroupOperationEntity operationGroupOperation = operationGroupOperationService.selectById(id);

		return RD.build().put("data", operationGroupOperation);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:operationgroupoperation:create")
	public RD save(@RequestBody OperationGroupOperationEntity operationGroupOperation) {
		operationGroupOperation.setPinyin(PinyinUtil.getPinYin(operationGroupOperation.getOperation()));
		operationGroupOperationService.insert(operationGroupOperation);

		return RD.build().put("status", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:operationgroupoperation:update")
	public RD update(@RequestBody OperationGroupOperationEntity operationGroupOperation) {
		operationGroupOperationService.updatePinAndDataById(operationGroupOperation);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:operationgroupoperation:delete")
	public RD delete(@RequestBody Integer[] ids) {
		operationGroupOperationService.deleteByIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}



}
