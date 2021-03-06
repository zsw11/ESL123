package io.apj.modules.masterData.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.*;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.OpertaionGroupService;

import javax.servlet.http.HttpServletResponse;

/**
 * 手顺组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/opertaiongroup")
public class OpertaionGroupController extends AbstractController {
	@Autowired
	private OpertaionGroupService opertaionGroupService;
	@Autowired
	private OperationGroupOperationService operationGroupOperationService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:opertaiongroup:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = opertaionGroupService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:opertaiongroup:info")
	public RD info(@PathVariable("id") Integer id) {
		OpertaionGroupEntity opertaionGroup = opertaionGroupService.selectById(id);
		opertaionGroup.setDeptName(sysDeptService.selectById(opertaionGroup.getDeptId()).getName());
		List<OperationGroupOperationEntity> operationGroupOperationEntities = operationGroupOperationService
				.selectList(new EntityWrapper<OperationGroupOperationEntity>().eq("operation_group_id", id).isNull("delete_at"));
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("operationGroup", opertaionGroup);
		item.put("operations", operationGroupOperationEntities);
		return RD.build().put("data", item);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:opertaiongroup:create")
	public RD save(@RequestBody Map<String, Object> map)
			throws InvocationTargetException, IllegalAccessException, IntrospectionException {
		OpertaionGroupEntity opertaionGroup = new OpertaionGroupEntity();
		DataUtils.transMap2Bean2((Map<String, Object>) map.get("operationGroup"), opertaionGroup);
		opertaionGroup.setDeptId(getUserDeptId().intValue());
		opertaionGroup.setCreateBy(getUserId().intValue());
		opertaionGroupService.insertOpGroup(map, opertaionGroup);
		return RD.build();
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:opertaiongroup:update")
	public RD update(@RequestBody Map<String, Object> map)
			throws IllegalAccessException, IntrospectionException, InvocationTargetException {
		Map<String, Object> operationGroup = (Map<String, Object>) map.get("operationGroup");
//		OpertaionGroupEntity opertaionGroupEntity = new OpertaionGroupEntity();
//		opertaionGroupEntity.setCode((String) operationGroup.get("code"));
//		opertaionGroupEntity.setId((Integer) operationGroup.get("id"));
//		opertaionGroupEntity.setRemark((String) operationGroup.get("remark"));
		map.put("updateBy",getUserId().intValue());
//		opertaionGroupEntity.setUpdateBy(getUserId().intValue());
		opertaionGroupService.UpdataOpertaionGroup(map);

		return RD.build();
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:opertaiongroup:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("opertaiongroup", ids[i].longValue());
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return RD.INTERNAL_SERVER_ERROR(reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("opertaiongroup", ids[i].longValue());
			}
		}
		opertaionGroupService.deleteByIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出手顺组合")
	@RequestMapping(value = "/exportExcel", produces = "application/json;charset=UTF-8")
	public void exportExcel(HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
		// 过滤字段，前端传
		List<String> list = (List<String>) map.get("exportAttributes");
		// 查询类型
		String type = map.get("filterType").toString();
		// 普通查询
		Map<String, Object> params = (Map<String, Object>) map.get("filters");
		if (null == params) {
			params = new HashMap<>();
		}
		params.put("page", "1");
		params.put("limit", "9999999");
		PageUtils pageUtils = opertaionGroupService.queryPage(params);
		List<OpertaionGroupEntity> OpertaionGroupEntity = (List<OpertaionGroupEntity>) pageUtils.getData();
		// 处理数据源
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dict = sysDictService.getDictDetail();
		for (OpertaionGroupEntity item : OpertaionGroupEntity) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("opertaionGroup", item, dict);
			dataList.add(arr);
		}
		// 返回excel格式数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "opertaionGroup", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "手顺组合.xlsx", data);
	}

}
