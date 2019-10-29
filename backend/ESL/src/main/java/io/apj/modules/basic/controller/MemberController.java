package io.apj.modules.basic.controller;

import java.nio.charset.Charset;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.modules.basic.entity.JobEntity;
import io.apj.modules.basic.service.JobService;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.SysDictService;
import io.apj.modules.sys.service.SysUserService;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import cn.hutool.core.util.PinyinUtil;
import io.apj.modules.basic.entity.MemberEntity;
import io.apj.modules.basic.service.MemberService;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.SysDeptEntity;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.common.annotation.SysLog;
import io.apj.common.validator.ValidatorUtils;

/**
 * 人员信息
 *
 * @author Royluo
 *
 * @date 2018-12-11 13:28:08
 */
@RestController
@RequestMapping("basic/member")
public class MemberController extends AbstractController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private SysDictService sysDictService;
	@Value("classpath:static/filedFilter.json")
	private Resource filedFilter;
	@Autowired
	private SysUserService userService;
	@Value("classpath:static/query-configuration/member.json")
	private Resource memberData;
	@Autowired
	private JobService jobService;
	@Value("classpath:static/query-configuration/areaData.json")
	private Resource areaData;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("basic:member:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = memberService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("basic:member:info")
	public R info(@PathVariable("id") Long id) {
		MemberEntity member = memberService.selectById(id);

		return R.ok().put("member", member);
	}

	/**
	 * 保存
	 */
	@SysLog("保存人员")
	@RequestMapping("/save")
	@RequiresPermissions("basic:member:save")
	public R save(@RequestBody MemberEntity member) {
		member.setCreateBy(getUserId());
		member.setCreateAt(new Date());
		member.setUpdateAt(new Date());
		member.setPinyin(PinyinUtil.getPinYin(member.getName()));
		ValidatorUtils.validateEntity(member);
		memberService.save(member);
		// 添加引用表
		insertTableReference("basic_member", member.getId(), "basic_job", member.getJobId(), false);
		insertTableReference("basic_member", member.getId(), "sys_dept", member.getDeptId(), false);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改人员")
	@RequestMapping("/update")
	@RequiresPermissions("basic:member:update")
	public R update(@RequestBody MemberEntity member) {
		member.setUpdateBy(getUserId());
		member.setUpdateAt(new Date());
		member.setPinyin(PinyinUtil.getPinYin(member.getName()));
		ValidatorUtils.validateEntity(member);
		memberService.update(member);
		// 更新引用表,ifUpdate仅需一次，否则最终只会存在最后一条
		insertTableReference("basic_member", member.getId(), "basic_job", member.getJobId(), true);
		insertTableReference("basic_member", member.getId(), "sys_dept", member.getDeptId(), false);

		return R.ok();
	}

	/**
	 * 保存
	 */
	@SysLog("保存人员")
	@RequestMapping("/saveOrUpdate")
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("basic:member:save")
	public R saveOrUpdate(@RequestBody MemberEntity member) {
		SysUserEntity user = member.getUserEntity();
		if (user != null) {
			user.setCreateAt(new Date());
			user.setCreateBy(getUserId());
			userService.save(user);
			member.setUserId(user.getId());
		}
		if (member.getId() == 0) {
			member.setCreateAt(new Date());
			member.setUpdateAt(new Date());
			member.setCreateBy(getUserId());
			member.setPinyin(PinyinUtil.getPinYin(member.getName()));
			ValidatorUtils.validateEntity(member);
			memberService.save(member);
			// 添加引用表
			insertTableReference("basic_member", member.getId(), "basic_job", member.getJobId(), false);
			insertTableReference("basic_member", member.getId(), "sys_dept", member.getDeptId(), false);
		} else {
			member.setUpdateAt(new Date());
			member.setUpdateBy(getUserId());
			member.setPinyin(PinyinUtil.getPinYin(member.getName()));
			memberService.update(member);
			// 更新引用表,ifUpdate仅需一次，否则最终只会存在最后一条
			insertTableReference("basic_member", member.getId(), "basic_job", member.getJobId(), true);
			insertTableReference("basic_member", member.getId(), "sys_dept", member.getDeptId(), false);
		}
		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除人员")
	@RequestMapping("/delete")
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("basic:member:delete")
	public R delete(@RequestBody Long[] ids) {
		// 检查是否存在引用
		for (int i = 0; i < ids.length; i++) {
			List<ReferenceEntity> referenceEntities = deleteCheckReference("basic_member", ids[i]);
			if (!referenceEntities.isEmpty()) {
				for (ReferenceEntity reference : referenceEntities) {
					return R.error("表名：" + reference.getByEntity() + "，id=" + reference.getById() + " 在表："
							+ reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
				}
			} else {
				// 删除引用表关系
				deleteTableReference("basic_member", ids[i]);
			}
		}
		memberService.deleteBatch(ids);
		return R.ok();
	}

	/**
	 * 高级查询
	 *
	 * @throws IOException
	 */
	@RequestMapping("/advancedSearch")
	public R advancedSearch(@RequestBody Map<String, Object> map)
			throws JsonIOException, JsonSyntaxException, IOException {
		JsonParser parser = new JsonParser();
		// 获取根目录
		int limit = (int) map.get("limit");
		int currPage = (int) map.get("page");
//        JsonArray fieldFilter = (JsonArray) parser.parse(
//                new FileReader(ResourceUtils.getURL("classpath:static/query-configuration/member.json").getPath()));
		String jsonStr = IOUtils.toString(memberData.getInputStream(), Charset.forName("UTF-8"));
		Gson gson = new Gson();
		JsonArray fieldFilter = gson.fromJson(jsonStr, JsonArray.class);
		map.put("sqlFilter", getSqlFilter(getUser()));
		Map<String, Object> listmap = memberService.advancedSearch(map, fieldFilter);
		List<MemberEntity> list = (List<MemberEntity>) listmap.get("list");
		for (MemberEntity item : list) {
			EntityWrapper<SysDeptEntity> deptWrapper = new EntityWrapper<>();
			deptWrapper.eq("dept_id", item.getDeptId());
			SysDeptEntity deptEntity = deptService.selectOne(deptWrapper);
			item.setDept(deptEntity);
		}
		PageUtils page = new PageUtils(list, (int) listmap.get("count"), limit, currPage);
		return R.ok().put("page", page);
	}

	/**
	 * 根据名字或者拼音查询人员信息
	 */
	@RequestMapping("/selectMemberByName")
	public R selectMemberByName(@RequestParam String name, @RequestParam Boolean isHQ) {
		EntityWrapper<SysDeptEntity> deptWrapper = new EntityWrapper<>();
		deptWrapper.eq("dept_type", "headquarters");
		List<SysDeptEntity> headquartersDeptList = deptService.selectList(deptWrapper);
		List<Long> headquartersIds = new ArrayList<Long>();
		for (SysDeptEntity item : headquartersDeptList) {
			headquartersIds.add(item.getDeptId());
		}
		EntityWrapper<MemberEntity> memberWrapper = new EntityWrapper<>();
		if (isHQ) {
			memberWrapper.in("dept_id", headquartersIds);
		} else {
			memberWrapper.notIn("dept_id", headquartersIds);
		}
		memberWrapper.notIn("id", getMemberId()).andNew().like("name", name).or().like("pinyin", name);
		List<MemberEntity> list = memberService.selectList(memberWrapper);
		return R.ok().put("list", list);
	}

	/**
	 * 根据部门id查询属于该部门的人员
	 */
	@RequestMapping("/selectMemberListByDeptId")
	public R selectMemberListByDeptId(@RequestParam String deptId) {
		EntityWrapper<MemberEntity> memberWrapper = new EntityWrapper<>();
		memberWrapper.eq("dept_id", deptId);
		List<MemberEntity> list = memberService.selectList(memberWrapper);
		return R.ok().put("list", list);
	}

	/**
	 * 导出excel
	 *
	 * @return
	 * @throws Exception
	 */
	@SysLog("导出人员信息")
	@RequestMapping("/exportExcel")
	public R exportExcel(HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
		// 过滤字段，前端传
		List<String> list = (List<String>) map.get("exportAttributes");
		// 查询类型
		String type = map.get("filterType").toString();
		List<MemberEntity> memberEntities;
		// 判断查询类型为高级查询还是普通查询
		if (type.equals("list") || type.equals("all")) {
			// 普通查询
			Map<String, Object> params = (Map<String, Object>) map.get("filters");
			if (params == null) {
				params = new HashMap<>();
			}
			params.put("page", "1");
			params.put("limit", "9999999");
			PageUtils pageUtils = memberService.queryPage(params);
			memberEntities = (List<MemberEntity>) pageUtils.getData();
		} else {
			// 高级查询
			map.put("limit", 999999);
			map.put("page", 1);
			String jsonStr = IOUtils.toString(memberData.getInputStream(), Charset.forName("UTF-8"));
			Gson gson = new Gson();
			JsonArray fieldFilter = gson.fromJson(jsonStr, JsonArray.class);
			map.put("sqlFilter", getSqlFilter(getUser()));
			Map<String, Object> listmap = memberService.advancedSearch(map, fieldFilter);
			memberEntities = (List<MemberEntity>) listmap.get("list");
		}
		List<Map<String, Object>> dataList = new ArrayList<>();
		HashMap<String, String> dictMap = sysDictService.getDictDetail();
		String jsonStr = IOUtils.toString(areaData.getInputStream(), Charset.forName("UTF-8"));
		Gson gson = new Gson();
		JsonObject areaDataFilter = gson.fromJson(jsonStr, JsonObject.class);
		for (MemberEntity item : memberEntities) {
			// 处理数据源
			Map<String, Object> arr = DataUtils.dataChange("member", item, dictMap, areaDataFilter);
			// 处理其他数据源
			JobEntity jobEntity;
			if (item.getJob() != null) {
				jobEntity = item.getJob();
			} else {
				jobEntity = new JobEntity();
			}
			Map<String, Object> arr2 = DataUtils.dataChange("job", jobEntity, dictMap, areaDataFilter);
			SysDeptEntity sysDeptEntity;
			if (item.getDept() != null) {
				sysDeptEntity = item.getDept();
			} else {
				sysDeptEntity = new SysDeptEntity();
			}
			Map<String, Object> arr3 = DataUtils.dataChange("dept", sysDeptEntity, dictMap, areaDataFilter);
			arr2.putAll(arr3);
			arr.putAll(arr2);
			dataList.add(arr);
		}
		// 返回excel数据
		Map<String, Object> param = DataUtils.rtlExcelData(list, "member", dataList);
		ExcelData data = new ExcelData();
		data.setTitles((List<String>) param.get("titles"));
		data.setRows((List<List<Object>>) param.get("rows"));
		// 导出
		String datetime = DateUtils.format(new Date(), "YYMMddHHmm");
		ExportExcelUtils.exportExcel(response, datetime + "人员信息.xlsx", data);
		return R.ok();
	}

	/**
	 * 导入人员信息
	 *
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping("/import")
	public R importExcel(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
		List<MemberEntity> memberEntityList = new ArrayList<>();
		Set<String> codeCheck = new HashSet<>();
		for (int i = 0; i < maps.size(); i++) {
			MemberEntity memberEntity = new MemberEntity();
			// memberMap
			Map<String, Object> memberMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String[] keyStrs = key.split("\\.");
				// 所属组织集团
				if (keyStrs[0].equals("dept")) {
					Wrapper<SysDeptEntity> sysDeptEntityWrapper = new EntityWrapper<SysDeptEntity>().eq(keyStrs[1],
							value);
					SysDeptEntity sysDeptEntity = deptService.selectOne(sysDeptEntityWrapper);
					if (sysDeptEntity == null) {
						return R.error(500, "第" + (i + 1) + "行，所属组织集团不存在！");
					}
					memberEntity.setDeptId(sysDeptEntity.getDeptId());
				}
				// 工作岗位
				if (keyStrs[0].equals("job")) {
					if (keyStrs[1].equals("code")) {
						Wrapper<JobEntity> jobEntityWrapper = new EntityWrapper<JobEntity>().eq(keyStrs[1], value);
						JobEntity jobEntity = jobService.selectOne(jobEntityWrapper);
						if (jobEntity == null) {
							return R.error("第" + (i + 1) + "行，该工作岗位不存在！");
						}
						memberEntity.setJobId(jobEntity.getId());
					}
				}
				// 人员信息
				if (keyStrs[0].equals("member")) {
					// 判断人员编码唯一
					if (keyStrs[1].equals("code")) {
						// 检查导入数据重复
						if (!codeCheck.add(value.toString())) {
							return R.error(500, "第" + (i + 1) + "行，导入数据人员编码【" + value.toString() + "】存在重复！");
						}
						Wrapper<MemberEntity> memberEntityWrapper = new EntityWrapper<MemberEntity>().eq(keyStrs[1],
								value);
						MemberEntity memberEntity1 = memberService.selectOne(memberEntityWrapper);
						if (memberEntity1 != null) {
							return R.error("第" + (i + 1) + "行，该人员编码已存在！");
						}
					}
					memberMap.put(keyStrs[1], value);
				}
			}
			// 先提取日期
			String birthDate = (String) memberMap.get("birthDate");
			String employmentDate = (String) memberMap.get("employmentDate");
			memberMap.remove("birthDate");
			memberMap.remove("employmentDate");
			// map转实体
			DataUtils.transMap2Bean2(memberMap, memberEntity);
			// 设置回时间
			if (birthDate != null)
				if (birthDate.length() < 10 && birthDate != "") {
					return R.error("第" + (i + 1) + "行，生日日期格式错误。正确格式为XXXX-XX-XX");
				}
			memberEntity.setEmploymentDate(DateUtils.stringToDate(employmentDate, "yyyy-mm-dd"));
			memberEntity.setPinyin(PinyinUtil.getPinYin(memberEntity.getName()));
			memberEntity.setCreateBy(getUserId());
			memberEntity.setCreateAt(new Date());
			memberEntity.setUpdateAt(new Date());
			// 校验
			ValidatorUtils.validateEntity(memberEntity, i);
			memberEntityList.add(memberEntity);
		}
		try {
			memberService.insertBatch(memberEntityList, Constant.importNum);
		} catch (MybatisPlusException e) {
			throw new RRException(e.getMessage(), 500);
		}

		// 添加引用表
		for (MemberEntity member : memberEntityList) {
			insertTableReference("basic_member", member.getId(), "basic_job", member.getJobId(), false);
			insertTableReference("basic_member", member.getId(), "sys_dept", member.getDeptId(), false);
		}
		return R.ok();
	}

}
