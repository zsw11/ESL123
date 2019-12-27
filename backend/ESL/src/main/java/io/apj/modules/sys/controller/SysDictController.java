package io.apj.modules.sys.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.sys.entity.SysDictTypeEntity;
import io.apj.modules.sys.service.SysDictTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.sys.entity.SysDictEntity;
import io.apj.modules.sys.service.SysDictService;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;

/**
 * 字典项
 *
 * @author samchen
 *
 * @date 2018-12-05 14:16:20
 */
@RestController
@RequestMapping("/api/v1/dict")
public class SysDictController extends AbstractController {
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private SysDictTypeService sysDictTypeService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dict:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = sysDictService.queryPage(params); 

		return RD.listReturn(page.getData(), page.getTotalCount());
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:dict:info")
	public R info(@PathVariable("id") Long id) {
		SysDictEntity sysDict = sysDictService.selectById(id);

		return R.ok().put("sysDict", sysDict);
	}

	/**
	 * 信息
	 */
	@GetMapping("/detail")
	@RequiresPermissions("sys:dict:info")
	public ResponseEntity<Object> detail(@RequestParam Long id) {
		SysDictEntity sysDict = sysDictService.selectById(id);
		return RD.ok(sysDict);
	}

	@GetMapping("/detailbycode")
//	@RequiresPermissions("sys:dict:info")
	public RD detailbycode(@RequestParam String dictCode, @RequestParam String code) {
		long dictTypeId = sysDictTypeService.selectOne(new EntityWrapper<SysDictTypeEntity>().eq("type",dictCode)).getId();
		String dictName = sysDictService.selectOne(new EntityWrapper<SysDictEntity>().eq("dict_type_id",dictTypeId).eq("code",code)).getName();
		return RD.build().put("dictName",dictName);
	}


	/**
	 * 保存
	 */
	@SysLog("保存字典项")
	@RequestMapping("/create")
//	@RequiresPermissions("sys:dict:create")
	public ResponseEntity<Object> save(@RequestBody SysDictEntity sysDict) {
		sysDict.setCreateAt(new Date());
		sysDict.setUpdateAt(new Date());
		sysDict.setCreateBy(getUserId());
		sysDict.setIfLock(false);
		sysDictService.insert(sysDict);

		return RD.ok(RD.build().put("status", 200));
	}

	/**
	 * 修改
	 */
	@SysLog("修改字典项")
	@RequestMapping("/update")
	@RequiresPermissions("sys:dict:update")
	public R update(@RequestBody SysDictEntity sysDict) {

		SysDictEntity dict = sysDictService.selectById(sysDict.getId());
		if (dict == null) {
			return R.error(400, "该字典项不存在！");
		}
		if (dict.isIfLock()) {
			return R.error(403, "该字典项已锁定，不可修改！");
		}
		sysDict.setCreateAt(dict.getCreateAt());
		sysDict.setCreateBy(dict.getCreateBy());
		sysDict.setUpdateAt(new Date());
		sysDict.setUpdateBy(getUserId());
		sysDictService.updateById(sysDict);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除字典项")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:dict:delete")
	public R delete(@RequestBody Long[] ids) {

		// 逻辑删除
		List<SysDictEntity> list = sysDictService.selectBatchIds(Arrays.asList(ids));
		for (SysDictEntity item : list) {
			if (item.isIfLock()) {
				return R.error(403, "字典项" + item.getCode() + "已锁定，不可删除！");
			}
			item.setDeleteAt(new Date());
		}
		sysDictService.updateBatchById(list, list.size());
//			sysDictService.deleteBatchIds(Arrays.asList(ids));

		return R.ok();
	}

}
