package io.apj.modules.basic.controller;

import java.nio.charset.Charset;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.google.gson.*;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.modules.basic.entity.JobEntity;
import io.apj.modules.basic.service.JobService;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.SysDictService;
import io.apj.modules.sys.service.SysUserService;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import cn.hutool.core.util.PinyinUtil;
import io.apj.modules.basic.entity.StaffEntity;
import io.apj.modules.basic.service.StaffService;
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
 * @date 2018-12-11 13:28:08
 */
@RestController
@RequestMapping("api/v1/staff")
public class StaffController extends AbstractController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysDictService sysDictService;
    @Value("classpath:static/filedFilter.json")
    private Resource filedFilter;
    @Autowired
    private SysUserService userService;
    @Value("classpath:static/query-configuration/staff.json")
    private Resource staffData;
    @Autowired
    private JobService jobService;
    @Autowired
    private SysDeptService sysDeptService;
//	@Value("classpath:static/query-configuration/areaData.json")
//	private Resource areaData;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("basic:staff:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
        PageUtils page = staffService.queryPage(params);

        return RD.ok(page);
    }

    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("basic:staff:info")
    public RD info(@PathVariable("id") Long id) {
        StaffEntity staff = staffService.selectById(id);
		SysDeptEntity sysDeptEntity = sysDeptService.selectOne(new EntityWrapper<SysDeptEntity>().eq("id", staff.getDeptId()));
        StaffEntity staffEntity = deptId(sysDeptEntity.getId(),id);
        return RD.build().put("data", staffEntity);
    }

	/**
	 * 递归dept
	 * @param deptId
	 * @param id
	 * @return
	 */
    public StaffEntity deptId(Long deptId, Long id){
		StaffEntity staff = staffService.selectById(id);
		SysDeptEntity sysDeptEntity = sysDeptService.selectById(deptId);
		if (sysDeptEntity.getDeptType().equals("bloc")) {
			staff.setCenter(sysDeptEntity.getName());
            return staff;
        }else if (sysDeptEntity.getDeptType().equals("center")) {
            staff.setCenter(sysDeptEntity.getName());
            return staff;
        } else {
			SysDeptEntity sysDeptEntity1 = sysDeptService.selectOne(new EntityWrapper<SysDeptEntity>().eq("id", sysDeptEntity.getParentId().intValue()));
            staff = deptId(sysDeptEntity1.getId(),id);
        }
        return staff;

    }

    /**
     * 保存
     */
    @SysLog("保存人员")
    @RequestMapping("/create")
//	@RequiresPermissions("basic:staff:create")
    public RD save(@RequestBody StaffEntity staff) {
        staff.setCreateBy(getUserId());
        staff.setCreateAt(new Date());
        staff.setUpdateAt(new Date());
        staff.setPinyin(PinyinUtil.getPinYin(staff.getName()));
        ValidatorUtils.validateEntity(staff);
        staffService.save(staff);
        // 添加引用表
        insertTableReference("basic_staff", staff.getId(), "basic_job", staff.getJobId(), false);
        insertTableReference("basic_staff", staff.getId(), "sys_dept", staff.getDeptId(), false);

        return RD.build();
    }

    /**
     * 修改
     */
    @SysLog("修改人员")
    @RequestMapping("/update")
    @RequiresPermissions("basic:staff:update")
    public ResponseEntity<Object> update(@RequestBody StaffEntity staff) {
        staff.setUpdateBy(getUserId());
        staff.setUpdateAt(new Date());
        staff.setPinyin(PinyinUtil.getPinYin(staff.getName()));
        ValidatorUtils.validateEntity(staff);
        staffService.update(staff);
        // 更新引用表,ifUpdate仅需一次，否则最终只会存在最后一条
        insertTableReference("basic_staff", staff.getId(), "basic_job", staff.getJobId(), true);
        insertTableReference("basic_staff", staff.getId(), "sys_dept", staff.getDeptId(), false);

        return RD.ok(staff);
    }

    /**
     * 保存
     */
    @SysLog("保存人员")
    @RequestMapping("/saveOrUpdate")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("basic:staff:save")
    public RD saveOrUpdate(@RequestBody StaffEntity staff) {
        SysUserEntity user = staff.getUserEntity();
        if (user != null) {
            user.setCreateAt(new Date());
            user.setCreateBy(getUserId());
            userService.save(user);
            staff.setUserId(user.getId());
        }
        if (staff.getId() == 0) {
            staff.setCreateAt(new Date());
            staff.setUpdateAt(new Date());
            staff.setCreateBy(getUserId());
            staff.setPinyin(PinyinUtil.getPinYin(staff.getName()));
            ValidatorUtils.validateEntity(staff);
            staffService.save(staff);
            // 添加引用表
            insertTableReference("basic_staff", staff.getId(), "basic_job", staff.getJobId(), false);
            insertTableReference("basic_staff", staff.getId(), "sys_dept", staff.getDeptId(), false);
        } else {
            staff.setUpdateAt(new Date());
            staff.setUpdateBy(getUserId());
            staff.setPinyin(PinyinUtil.getPinYin(staff.getName()));

            staffService.update(staff);
            // 更新引用表,ifUpdate仅需一次，否则最终只会存在最后一条
            insertTableReference("basic_staff", staff.getId(), "basic_job", staff.getJobId(), true);
            insertTableReference("basic_staff", staff.getId(), "sys_dept", staff.getDeptId(), false);
        }
        return RD.build().put("code", 200);
    }

    /**
     * 删除
     */
    @SysLog("删除人员")
    @RequestMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("basic:staff:delete")
    public R delete(@RequestBody Long[] ids) {
        // 检查是否存在引用
        for (int i = 0; i < ids.length; i++) {
            List<ReferenceEntity> referenceEntities = deleteCheckReference("basic_staff", ids[i]);
            if (!referenceEntities.isEmpty()) {
                for (ReferenceEntity reference : referenceEntities) {
                    return R.error("表名：" + reference.getByEntity() + "，id=" + reference.getById() + " 在表："
                            + reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
                }
            } else {
                // 删除引用表关系
                deleteTableReference("basic_staff", ids[i]);
            }
        }
        staffService.deleteBatch(ids);
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
//                new FileReader(ResourceUtils.getURL("classpath:static/query-configuration/staff.json").getPath()));
        String jsonStr = IOUtils.toString(staffData.getInputStream(), Charset.forName("UTF-8"));
        Gson gson = new Gson();
        JsonArray fieldFilter = gson.fromJson(jsonStr, JsonArray.class);
        map.put("sqlFilter", getSqlFilter(getUser()));
        Map<String, Object> listmap = staffService.advancedSearch(map, fieldFilter);
        List<StaffEntity> list = (List<StaffEntity>) listmap.get("list");
        for (StaffEntity item : list) {
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
    @RequestMapping("/selectStaffByName")
    public R selectStaffByName(@RequestParam String name, @RequestParam Boolean isHQ) {
        EntityWrapper<SysDeptEntity> deptWrapper = new EntityWrapper<>();
        deptWrapper.eq("dept_type", "headquarters");
        List<SysDeptEntity> headquartersDeptList = deptService.selectList(deptWrapper);
        List<Long> headquartersIds = new ArrayList<Long>();
        for (SysDeptEntity item : headquartersDeptList) {
            headquartersIds.add(item.getId());
        }
        EntityWrapper<StaffEntity> staffWrapper = new EntityWrapper<>();
        if (isHQ) {
            staffWrapper.in("dept_id", headquartersIds);
        } else {
            staffWrapper.notIn("dept_id", headquartersIds);
        }
        staffWrapper.notIn("id", getStaffId()).andNew().like("name", name).or().like("pinyin", name);
        List<StaffEntity> list = staffService.selectList(staffWrapper);
        return R.ok().put("list", list);
    }

    /**
     * 根据部门id查询属于该部门的人员
     */
    @RequestMapping("/selectStaffListByDeptId")
    public R selectStaffListByDeptId(@RequestParam String deptId) {
        EntityWrapper<StaffEntity> staffWrapper = new EntityWrapper<>();
        staffWrapper.eq("dept_id", deptId);
        List<StaffEntity> list = staffService.selectList(staffWrapper);
        return R.ok().put("list", list);
    }

    /**
     * 导出excel
     *
     * @return
     * @throws Exception
     */
    @SysLog("导出人员信息")
    @RequestMapping(value = "/exportExcel", produces = "application/json;charset=UTF-8")
    public R exportExcel(HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception {
        // 过滤字段，前端传
        List<String> list = (List<String>) map.get("exportAttributes");
        // 查询类型
        String type = map.get("filterType").toString();
        List<StaffEntity> staffEntities;
        // 判断查询类型为高级查询还是普通查询
        if (type.equals("list") || type.equals("all")) {
            // 普通查询
            Map<String, Object> params = (Map<String, Object>) map.get("filters");
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("page", "1");
            params.put("limit", "9999999");
            PageUtils pageUtils = staffService.queryPage(params);
            staffEntities = (List<StaffEntity>) pageUtils.getData();
        } else {
            // 高级查询
            map.put("limit", 999999);
            map.put("page", 1);
            String jsonStr = IOUtils.toString(staffData.getInputStream(), Charset.forName("UTF-8"));
            Gson gson = new Gson();
            JsonArray fieldFilter = gson.fromJson(jsonStr, JsonArray.class);
            map.put("sqlFilter", getSqlFilter(getUser()));
            Map<String, Object> listmap = staffService.advancedSearch(map, fieldFilter);
            staffEntities = (List<StaffEntity>) listmap.get("list");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        HashMap<String, String> dictMap = sysDictService.getDictDetail();
//		String jsonStr = IOUtils.toString(areaData.getInputStream(), Charset.forName("UTF-8"));
//		Gson gson = new Gson();
//		JsonObject areaDataFilter = gson.fromJson(jsonStr, JsonObject.class);
        Map<Integer, String> deptMap = new HashMap<>();
        List<SysDeptEntity> sysDeptEntityList = deptService.selectList(new EntityWrapper<SysDeptEntity>().isNull("delete_at"));
        for(SysDeptEntity item : sysDeptEntityList){
            deptMap.put(item.getId().intValue(),item.getName());
        }
        for (StaffEntity item : staffEntities) {
            item.setDeptName(deptMap.get(item.getDeptId().intValue()));
            // 处理数据源
            Map<String, Object> arr = DataUtils.dataChange("staff", item, dictMap);
            // 处理其他数据源
//            JobEntity jobEntity;
//            if (item.getJob() != null) {
//                jobEntity = item.getJob();
//            } else {
//                jobEntity = new JobEntity();
//            }
//            Map<String, Object> arr2 = DataUtils.dataChange("job", jobEntity, dictMap);
//            SysDeptEntity sysDeptEntity;
//            if (item.getDept() != null) {
//                sysDeptEntity = item.getDept();
//            } else {
//                sysDeptEntity = new SysDeptEntity();
//            }
//            Map<String, Object> arr3 = DataUtils.dataChange("dept", sysDeptEntity, dictMap);
//            arr2.putAll(arr3);
//            arr.putAll(arr2);
              dataList.add(arr);
        }
        // 返回excel数据
        Map<String, Object> param = DataUtils.rtlExcelData(list, "staff", dataList);
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
        List<StaffEntity> staffEntityList = new ArrayList<>();
        Set<String> codeCheck = new HashSet<>();
        for (int i = 0; i < maps.size(); i++) {
            StaffEntity staffEntity = new StaffEntity();
            // staffMap
            Map<String, Object> staffMap = new HashMap<>();
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
                    staffEntity.setDeptId(sysDeptEntity.getId());
                }
                // 工作岗位
                if (keyStrs[0].equals("job")) {
                    if (keyStrs[1].equals("code")) {
                        Wrapper<JobEntity> jobEntityWrapper = new EntityWrapper<JobEntity>().eq(keyStrs[1], value);
                        JobEntity jobEntity = jobService.selectOne(jobEntityWrapper);
                        if (jobEntity == null) {
                            return R.error("第" + (i + 1) + "行，该工作岗位不存在！");
                        }
                        staffEntity.setJobId(jobEntity.getId());
                    }
                }
                // 人员信息
                if (keyStrs[0].equals("staff")) {
                    // 判断人员编码唯一
                    if (keyStrs[1].equals("code")) {
                        // 检查导入数据重复
                        if (!codeCheck.add(value.toString())) {
                            return R.error(500, "第" + (i + 1) + "行，导入数据人员编码【" + value.toString() + "】存在重复！");
                        }
                        Wrapper<StaffEntity> staffEntityWrapper = new EntityWrapper<StaffEntity>().eq(keyStrs[1],
                                value);
                        StaffEntity staffEntity1 = staffService.selectOne(staffEntityWrapper);
                        if (staffEntity1 != null) {
                            return R.error("第" + (i + 1) + "行，该人员编码已存在！");
                        }
                    }
                    staffMap.put(keyStrs[1], value);
                }
            }
            // 先提取日期
            String birthDate = (String) staffMap.get("birthDate");
            String employmentDate = (String) staffMap.get("employmentDate");
            staffMap.remove("birthDate");
            staffMap.remove("employmentDate");
            // map转实体
            DataUtils.transMap2Bean2(staffMap, staffEntity);
            // 设置回时间
            if (birthDate != null)
                if (birthDate.length() < 10 && birthDate != "") {
                    return R.error("第" + (i + 1) + "行，生日日期格式错误。正确格式为XXXX-XX-XX");
                }
            staffEntity.setEmploymentDate(DateUtils.stringToDate(employmentDate, "yyyy-mm-dd"));
            staffEntity.setPinyin(PinyinUtil.getPinYin(staffEntity.getName()));
            staffEntity.setCreateBy(getUserId());
            staffEntity.setCreateAt(new Date());
            staffEntity.setUpdateAt(new Date());
            // 校验
            ValidatorUtils.validateEntity(staffEntity, i);
            staffEntityList.add(staffEntity);
        }
        try {
            staffService.insertBatch(staffEntityList, Constant.importNum);
        } catch (MybatisPlusException e) {
            throw new RRException(e.getMessage(), 500);
        }

        // 添加引用表
        for (StaffEntity staff : staffEntityList) {
            insertTableReference("basic_staff", staff.getId(), "basic_job", staff.getJobId(), false);
            insertTableReference("basic_staff", staff.getId(), "sys_dept", staff.getDeptId(), false);
        }
        return R.ok();
    }

}
