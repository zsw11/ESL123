package io.apj.modules.basic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.google.gson.JsonArray;

import io.apj.common.annotation.DataFilter;
import io.apj.common.utils.Constant;
import io.apj.common.utils.DataUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;

import io.apj.modules.basic.dao.StaffDao;
import io.apj.modules.basic.entity.JobEntity;
import io.apj.modules.basic.entity.StaffEntity;
import io.apj.modules.basic.service.JobService;
import io.apj.modules.basic.service.StaffService;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysUserService;

@Service("staffService")
public class StaffServiceImpl extends ServiceImpl<StaffDao, StaffEntity> implements StaffService {

	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private JobService jobService;

	@Autowired
	private SysUserService sysUserService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {

		Wrapper<StaffEntity> entityWrapper = new EntityWrapper<StaffEntity>().isNull("delete_at")
				.like(StringUtils.isNotEmpty((CharSequence) params.get("code")), "code", (String) params.get("code"))
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("gender")), "gender", params.get("gender"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("mobilephone")), "mobilephone",
						(String) params.get("mobilephone"))
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("status")), "status", params.get("status"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("email")), "email", (String) params.get("email"))
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
//		if (StringUtils.isNotEmpty((CharSequence) params.get("serviceOrderDeptId"))) {
//			Long serviceOrderDeptId = Long.valueOf(params.get("serviceOrderDeptId").toString());
//			entityWrapper.eq("dept_id", serviceOrderDeptId);
//		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("deptId"))) {
			entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("keyword"))) {
			String name = (String) params.get("keyword");
			name = name.replace("'", "");
			entityWrapper.andNew("pinyin like '%" + name + "%' " + "or name like '%" + name + "%'");
		}
		if (StringUtils.isNotEmpty((CharSequence) params.get("name"))) {
			entityWrapper.andNew(
					"pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
		}
		entityWrapper.orderBy("create_at", false);
		Page<StaffEntity> page = this.selectPage(new Query<StaffEntity>(params).getPage(), entityWrapper);

		for (StaffEntity entity : page.getRecords()) {
			entity.setPerms(sysUserService.getDeptAllPerms(entity.getDeptId(), "basic:staff:"));
			SysDeptEntity dept = sysDeptService.selectById(entity.getDeptId());
			entity.setDept(dept);
			String centerN = null;
			Long ESLID =  sysDeptService.selectOne(new EntityWrapper<SysDeptEntity>().eq("name","ESL")).getId();
			SysDeptEntity sysDeptEntity = sysDeptService.selectOne(new EntityWrapper<SysDeptEntity>().eq("id", entity.getDeptId()));
			List<SysDeptEntity> sysDeptEntityList = sysDeptService.selectList(new EntityWrapper<SysDeptEntity>().eq("parent_id", ESLID));
			for(SysDeptEntity item : sysDeptEntityList){
				if(item.getId()==sysDeptEntity.getParentId()){
					centerN=item.getName();
				}else if(item.getId()==sysDeptEntity.getId()){
					centerN=item.getName();
				}else if(ESLID==sysDeptEntity.getId()){
					centerN="ESL";
				}
			}
			entity.setCenter(centerN);
//			JobEntity job = jobService.selectById(entity.getJobId());
//			entity.setJob(job);
		}

		return new PageUtils(page);
	}

	@Override
	public void save(StaffEntity staff) {
		this.insert(staff);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(StaffEntity staff) {
		StaffEntity entity = this.selectById(staff.getId());
		staff.setCreateAt(entity.getCreateAt());
		staff.setCreateBy(entity.getCreateBy());
		staff.setDeleteAt(entity.getDeleteAt());
		this.updateAllColumnById(staff);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 逻辑删除
		List<StaffEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (StaffEntity item : list) {
			item.setDeleteAt(new Date());
		}
		this.updateBatchById(list, list.size());
	}

	@Override
	public Map<String, Object> advancedSearch(Map<String, Object> map, JsonArray fieldFilter) {
		Map<String, Object> listMap = new HashMap<String, Object>();
		Map<String, String> sqlMap = DataUtils.advancedSearchJsonReturnSQL(map, fieldFilter);
		int limit = (int) map.get("limit");
		int page = (int) map.get("page") - 1;
		// 权限过滤
		String sql = "select * from (";
		sql += sqlMap.get("sql") + ") as t where" + map.get("sqlFilter");
		String sqlCount = sqlMap.get("sqlCount").replace("as result", "as result where " + map.get("sqlFilter"));
		List<StaffEntity> list = baseMapper.executeSql(sql + " limit " + limit * page + "," + limit);
		Integer count = baseMapper.executeSqlCount(sqlCount);
		listMap.put("list", list);
		listMap.put("count", count);
		return listMap;
	}

}
