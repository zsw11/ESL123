package io.apj.modules.masterData.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.WorkstationTypeNodeDao;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;
import io.apj.modules.masterData.service.WorkstationTypeNodeService;

@Service("workstationTypeNodeService")
public class WorkstationTypeNodeServiceImpl extends ServiceImpl<WorkstationTypeNodeDao, WorkstationTypeNodeEntity>
		implements WorkstationTypeNodeService {
	@Autowired
	private WorkstationTypeNodeDao workstationTypeNodeDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<WorkstationTypeNodeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.isNull("delete_at").like(params.get("remark") != null && params.get("remark") != "", "remark",
				(String) params.get("remark"));
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}
		Page<WorkstationTypeNodeEntity> page = this.selectPage(new Query<WorkstationTypeNodeEntity>(params).getPage(),
				entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public ResponseEntity<JSONArray> listAllNodeType(Integer id) {
		List data = workstationTypeNodeDao.findAll();
		JSONArray array = new JSONArray();
		TreeUtils.setNodeTypeTree(id, data, array);
		return ResponseEntity.ok(array);
	}

}