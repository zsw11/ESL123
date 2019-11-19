
package io.apj.modules.sys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.common.annotation.SysLog;
import io.apj.common.utils.Constant;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.common.validator.ValidatorUtils;
import io.apj.common.validator.group.AddGroup;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.service.SysDeptService;

/**
 * 部门管理
 *
 * @author sam
 *
 * @date 2018-12-17 15:23:47
 */
@RestController
@RequestMapping("/api/v1/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("basic:dept:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		List<SysDeptEntity> deptList = sysDeptService.queryListByName(params);
		HashMap<Object, Object> page = new HashMap<>();
		page.put("data", deptList);
		page.put("totalPage", deptList.size());
		return RD.ok(page);
	}

	/**
	 * 选择部门(添加、修改菜单)
	 */
	@GetMapping("/select")
//	@RequiresPermissions("sys:dept:select")
	public ResponseEntity<Object> select(@RequestParam Map<String, Object> map) {

		if (map.get("deptId") != null) { // 根据部门ID返回本身及子级部门
			List<SysDeptEntity> deptList = sysDeptService.queryListByDeptCode(map);
			return RD.ok(RD.build().put("deptList", deptList));
		}

		List<SysDeptEntity> deptList = sysDeptService.queryList(map);

// 添加一级部门
//		if (getUserId() == Constant.SUPER_ADMIN) {
//			SysDeptEntity root = new SysDeptEntity();
//			root.setDeptId(0L);
//			root.setName("一级部门");
//			root.setParentId(-1L);
//			root.setOpen(true);
//			deptList.add(root);
//		}

		return RD.ok(RD.build().put("deptList", deptList));
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@GetMapping("/info")
	@RequiresPermissions("basic:dept:list")
	public R info() {
		long deptId = 0;
		if (getUserId() != Constant.SUPER_ADMIN) {
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
			Long parentId = null;
			for (SysDeptEntity sysDeptEntity : deptList) {
				if (parentId == null) {
					parentId = sysDeptEntity.getParentId();
					continue;
				}

				if (parentId > sysDeptEntity.getParentId().longValue()) {
					parentId = sysDeptEntity.getParentId();
				}
			}
			deptId = parentId;
		}
		return R.ok().put("deptId", deptId);
	}

	/**
	 * 信息
	 */
	@GetMapping("/info/{deptId}")
	@RequiresPermissions("basic:dept:info")
	public ResponseEntity<Object> info(@PathVariable("deptId") Long deptId) {
		SysDeptEntity dept = sysDeptService.selectById(deptId);
		return RD.ok(RD.build().put("dept", dept));
//		return R.ok().put("dept", dept);
	}

	/**
	 * 保存
	 */
	@SysLog("保存部门")
	@PostMapping("/save")
	@RequiresPermissions("basic:dept:save")
	public R save(@RequestBody SysDeptEntity dept) {
		ValidatorUtils.validateEntity(dept, AddGroup.class);

		dept.setCreateAt(new Date());
		dept.setUpdateAt(new Date());
		dept.setCreateBy(getUserId());

		sysDeptService.insert(dept);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改部门")
	@PostMapping("/update")
	@RequiresPermissions("basic:dept:update")
	public R update(@RequestBody SysDeptEntity dept) {

		ValidatorUtils.validateEntity(dept, AddGroup.class);

		SysDeptEntity d = sysDeptService.selectById(dept.getDeptId());
		dept.setCreateAt(d.getCreateAt());
		dept.setCreateBy(d.getCreateBy());
		dept.setUpdateAt(new Date());
		dept.setUpdateBy(getUserId());
		sysDeptService.updateById(dept);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除部门")
	@PostMapping("/delete")
	@RequiresPermissions("basic:dept:delete")
	public R delete(@RequestBody Map<?, ?> params) {
		// 判断是否有子部门
		List<Long> deptList = sysDeptService.queryDetpIdList(Long.valueOf(params.get("deptId").toString()));
		if (deptList.size() > 0) {
			return R.error("请先删除子部门");
		}

		// 检查是否存在引用

		List<ReferenceEntity> referenceEntities = deleteCheckReference("sys_dept",
				Long.valueOf(params.get("deptId").toString()));
		if (!referenceEntities.isEmpty()) {
			for (ReferenceEntity reference : referenceEntities) {
				return R.error("表名：" + reference.getByEntity() + "，id=" + reference.getById() + " 在表："
						+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
			}
		} else {
			// 删除引用表关系
			deleteTableReference("sys_dept", Long.valueOf(params.get("deptId").toString()));
		}

//		sysDeptService.deleteById(deptId);
		SysDeptEntity dept = sysDeptService.selectById(Long.valueOf(params.get("deptId").toString()));
		dept.setDeleteAt(new Date());
		sysDeptService.updateById(dept);

		return R.ok();
	}

	/**
	 * 根据编码获取下级组织机构
	 */
	@GetMapping("/getDeptListByCode")
	public R getDeptListByCode(@RequestParam Map<String, Object> params) {

		if (params.get("deptCode") == null) {
			return R.error(403, "组织机构编码不能为空！");
		}

		List<SysDeptEntity> deptList = sysDeptService.queryListByDeptCode(params);

		return R.ok().put("deptList", deptList);
	}

	/**
	 * 根据id获取下级组织机构
	 */
	@GetMapping("/getDeptListByDeptId")
	public R getDeptListByDeptId(@RequestParam Map<String, Object> params) {

		if (params.get("deptId") == null) {
			return R.error(403, "组织机构ID不能为空！");
		}

		List<SysDeptEntity> deptList = sysDeptService.queryListByDeptCode(params);

		return R.ok().put("deptList", deptList);
	}

}
