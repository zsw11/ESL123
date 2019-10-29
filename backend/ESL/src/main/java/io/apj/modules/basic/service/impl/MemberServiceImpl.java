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

import io.apj.modules.basic.dao.MemberDao;
import io.apj.modules.basic.entity.JobEntity;
import io.apj.modules.basic.entity.MemberEntity;
import io.apj.modules.basic.service.JobService;
import io.apj.modules.basic.service.MemberService;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.sys.service.SysUserService;

@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private JobService jobService;

	@Autowired
	private SysUserService sysUserService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {

		Wrapper<MemberEntity> entityWrapper = new EntityWrapper<MemberEntity>()
				.like(StringUtils.isNotEmpty((CharSequence) params.get("code")), "code", (String) params.get("code"))
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("gender")), "gender", params.get("gender"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("mobilephone")), "mobilephone",
						(String) params.get("mobilephone"))
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("deptId")), "dept_id", params.get("deptId"))
				.between(
						StringUtils.isNotEmpty((CharSequence) params.get("birthDateStart"))
								&& StringUtils.isNotEmpty((CharSequence) params.get("birthDateEnd")),
						"birth_date", params.get("birthDateStart"), params.get("birthDateEnd"))
				.ge(StringUtils.isNotEmpty((CharSequence) params.get("birthDateStart"))
						&& StringUtils.isEmpty((CharSequence) params.get("birthDateEnd")), "birth_date",
						params.get("birthDateStart"))
				.le(StringUtils.isEmpty((CharSequence) params.get("birthDateStart"))
						&& StringUtils.isNotEmpty((CharSequence) params.get("birthDateEnd")), "birth_date",
						params.get("birthDateEnd"))
				.eq(StringUtils.isNotEmpty((CharSequence) params.get("status")), "status", params.get("status"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("email")), "email", (String) params.get("email"))
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
//		if (StringUtils.isNotEmpty((CharSequence) params.get("serviceOrderDeptId"))) {
//			Long serviceOrderDeptId = Long.valueOf(params.get("serviceOrderDeptId").toString());
//			entityWrapper.eq("dept_id", serviceOrderDeptId);
//		}
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
		Page<MemberEntity> page = this.selectPage(new Query<MemberEntity>(params).getPage(), entityWrapper);

		for (MemberEntity entity : page.getRecords()) {
			entity.setPerms(sysUserService.getDeptAllPerms(entity.getDeptId(), "basic:member:"));
			SysDeptEntity dept = sysDeptService.selectById(entity.getDeptId());
			entity.setDept(dept);
			JobEntity job = jobService.selectById(entity.getJobId());
			entity.setJob(job);

		}

		return new PageUtils(page);
	}

	@Override
	public void save(MemberEntity member) {
		this.insert(member);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(MemberEntity member) {
		MemberEntity entity = this.selectById(member.getId());
		member.setCreateAt(entity.getCreateAt());
		member.setCreateBy(entity.getCreateBy());
		member.setDeleteAt(entity.getDeleteAt());
		this.updateAllColumnById(member);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 逻辑删除
		List<MemberEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (MemberEntity item : list) {
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
		List<MemberEntity> list = baseMapper.executeSql(sql + " limit " + limit * page + "," + limit);
		Integer count = baseMapper.executeSqlCount(sqlCount);
		listMap.put("list", list);
		listMap.put("count", count);
		return listMap;
	}

}
