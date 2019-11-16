package io.apj.modules.sys.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.modules.sys.entity.SysDictEntity;
import io.apj.modules.sys.entity.SysDictTypeEntity;
import io.apj.modules.sys.service.SysDictService;
import io.apj.modules.sys.service.SysDictTypeService;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;

/**
 * 字典类型
 *
 * @author samchen
 *
 * @date 2018-12-05 14:16:20
 */
@RestController
@RequestMapping("/api/v1/dicttype")
public class SysDictTypeController extends AbstractController {
	@Autowired
	private SysDictTypeService sysDictTypeService;

	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("sys:dicttype:list")
	public RD list(@RequestParam Map<String, Object> params) {
		PageUtils page = sysDictTypeService.queryPage(params);
		return RD.build().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("sys:dicttype:info")
	public R info(@PathVariable("id") Long id) {
		SysDictTypeEntity sysDictType = sysDictTypeService.selectById(id);

		List<SysDictEntity> sysDictList = sysDictService
				.selectList(new EntityWrapper<SysDictEntity>().eq("dict_type_id", sysDictType.getId()).isNull("delete_at"));

		sysDictType.setDictList(sysDictList);
		return R.ok().put("data", sysDictType);
	}

	/**
	 * 根据编码获取字典项列表
	 */
	@RequestMapping("/getListByType")
	public R getListByType(@RequestParam Map<String, Object> params) {

		if (params.get("type") == null)
			return R.error(403, "字典类型不能为空！");

		List<SysDictEntity> list = new ArrayList<SysDictEntity>();
		EntityWrapper<SysDictTypeEntity> wrapper = new EntityWrapper<SysDictTypeEntity>();
		wrapper.eq("type", params.get("type"));
		SysDictTypeEntity sysDictType = sysDictTypeService.selectOne(wrapper);
		if (sysDictType != null) {
			EntityWrapper<SysDictEntity> w = new EntityWrapper<SysDictEntity>();
			w.eq("dict_type_id", sysDictType.getId());
			list = sysDictService.selectList(w);
		}

		return R.ok().put("data", list);
	}

	/**
	 * 根据类型和子项编码查询
	 */
	@RequestMapping("/getListByTypeAndChildCode")
	public R getListByTypeAndChildCode(@RequestParam Map<String, Object> params) {
		if (params.get("type") == null)
			return R.error(403, "字典类型不能为空！");
		if (params.get("code") == null)
			return R.error(403, "字典子项编码不能为空！");
		SysDictTypeEntity sysDictType = sysDictTypeService
				.selectOne(new EntityWrapper<SysDictTypeEntity>().eq("type", params.get("type")));
		if (sysDictType == null)
			return R.error(404, "字典类型不存在！");
		SysDictEntity dict = sysDictService.selectOne(new EntityWrapper<SysDictEntity>().eq("code", params.get("code"))
				.eq("dict_type_id", sysDictType.getId()));
		if (dict == null)
			return R.error(404, "字典子项不存在！");

		return R.ok().put("data", dict);
	}

	/**
	 * 保存
	 */
	@SysLog("保存字典类型")
	@RequestMapping("/save")
//	@RequiresPermissions("sys:dicttype:save")
	public R save(@RequestBody SysDictTypeEntity sysDictType) {
		sysDictType.setCreateAt(new Date());
		sysDictType.setUpdateAt(new Date());
		sysDictType.setCreateBy(getUserId());
		sysDictType.setIfLock(false);
		sysDictTypeService.insert(sysDictType);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改字典类型")
	@RequestMapping("/update")
//	@RequiresPermissions("sys:dicttype:update")
	public R update(@RequestBody SysDictTypeEntity sysDictType) {

		SysDictTypeEntity dict = sysDictTypeService.selectById(sysDictType.getId());
		sysDictType.setCreateAt(dict.getCreateAt());
		sysDictType.setCreateBy(dict.getCreateBy());
		sysDictType.setUpdateAt(new Date());
		sysDictType.setIfLock(false);
		sysDictType.setUpdateBy(getUserId());

		sysDictTypeService.updateById(sysDictType);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除字典类型")
	@RequestMapping("/delete")
//	@RequiresPermissions("sys:dicttype:delete")
	public R delete(@RequestBody Long[] ids) {
//			sysDictTypeService.deleteBatchIds(Arrays.asList(ids));
		// 逻辑删除
		List<SysDictTypeEntity> list = sysDictTypeService.selectBatchIds(Arrays.asList(ids));
		for (SysDictTypeEntity item : list) {
			item.setDeleteAt(new Date());
		}
		sysDictTypeService.updateBatchById(list, list.size());

		return R.ok();
	}

}
