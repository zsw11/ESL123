package io.apj.modules.masterData.controller;

import cn.hutool.core.util.PinyinUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;
import io.apj.modules.masterData.service.WorkstationTypeNodeService;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 工位类型节点
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:28
 */
@RestController
@RequestMapping("/api/v1/workstationtypenode")
public class WorkstationTypeNodeController extends AbstractController {
	@Autowired
	private WorkstationTypeNodeService workstationTypeNodeService;

	/**
	 * 列表
	 *
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:workstationtypenode:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = workstationTypeNodeService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * nodeType列表
	 * 
	 * @return
	 */
	@RequestMapping("/listnodetype/{id}")
	public ResponseEntity<Object> listNodeType(@PathVariable Integer id) {
		EntityWrapper<WorkstationTypeNodeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("workstation_type_id", id).isNull("delete_at");
		List<WorkstationTypeNodeEntity> workstationTypeEntityList= workstationTypeNodeService.selectList(entityWrapper);
		WorkstationTypeNodeEntity workstationTypeNodeEntityList= workstationTypeNodeService.selectOne(entityWrapper);
		int idP = workstationTypeNodeEntityList.getId();
		return RD.ok(workstationTypeNodeService.listAllNodeType(idP)) ;

	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:workstationtypenode:info")
	public RD info(@PathVariable("id") Integer id) {
		WorkstationTypeNodeEntity workstationTypeNode = workstationTypeNodeService.selectById(id);

		return RD.build().put("data", workstationTypeNode);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:workstationtypenode:create")
	public RD save(@RequestBody WorkstationTypeNodeEntity workstationTypeNode) {
		workstationTypeNode.setPinyin(PinyinUtil.getPinYin(workstationTypeNode.getName()));
		workstationTypeNode.setCreateBy(getUserId().intValue());
		workstationTypeNodeService.insert(workstationTypeNode);

		return RD.build();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:workstationtypenode:update")
	public RD update(@RequestBody WorkstationTypeNodeEntity workstationTypeNode) {
		workstationTypeNode.setPinyin(PinyinUtil.getPinYin(workstationTypeNode.getName()));
		workstationTypeNodeService.updateById(workstationTypeNode);

		return RD.build();
	}

	/**
	 * 删除
	 *
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:workstationtypenode:delete")
	public RD delete(@RequestBody Integer[] ids) {
		workstationTypeNodeService.deleteBatchIds(Arrays.asList(ids));

		return RD.build();
	}

}
