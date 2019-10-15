package io.apj.modules.sys.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.annotation.SysLog;
import io.apj.common.utils.RD;
import io.apj.modules.sys.entity.CodeRuleItemEntity;
import io.apj.modules.sys.form.CodeRuleForm;
import io.apj.modules.sys.service.CodeRuleItemService;
import io.apj.modules.sys.vo.CodeRuleVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.sys.entity.CodeRuleEntity;
import io.apj.modules.sys.service.CodeRuleService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.validator.ValidatorUtils;

/**
 * 编码规则
 *
 * @author hzcong
 * @date 2019-4-29 14:38:52
 */
@RestController
@RequestMapping("/api/v1/coderule")
public class CodeRuleController extends AbstractController {
	@Autowired
	private CodeRuleService codeRuleService;
	@Autowired
	private CodeRuleItemService codeRuleItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:coderule:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = codeRuleService.queryPage(params);
//        String test = codeRuleService.getSerialNumberByCode("TEST");
		return RD.listReturn(page.getData(), page.getTotalCount());
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail")
	@RequiresPermissions("sys:coderule:update")
	public ResponseEntity<Object> info(@RequestParam("id") Long id) {
		CodeRuleEntity codeRule = codeRuleService.selectById(id);
		Wrapper<CodeRuleItemEntity> codeRuleItemEntityWrapper = new EntityWrapper<CodeRuleItemEntity>()
				.eq("code_rule_id", id);
		// 查询子表
		List<CodeRuleItemEntity> codeRuleItemEntities = codeRuleItemService.selectList(codeRuleItemEntityWrapper);
		// 组装返回视图
		CodeRuleVo codeRuleVo = new CodeRuleVo();
		BeanUtils.copyProperties(codeRule, codeRuleVo);
		codeRuleVo.setCodeRuleItems(codeRuleItemEntities);
		return RD.ok(codeRuleVo);
	}

	/**
	 * 生成流水编号（待完善······）
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/getOrderNumberByCode")
	@Transactional(rollbackFor = Exception.class)
	public R getOrderNumberByCode(@RequestParam Map<String, Object> params) {
		if (params.get("code") == null) {
			return R.error(403, "编码不能为空！");
		}
		String code = codeRuleService.getSerialNumberByCode((String) params.get("code"));

		return R.ok().put("orderNumber", code);
	}

	/**
	 * 保存
	 */
	@SysLog("保存编码规则")
	@RequestMapping("/create")
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("sys:coderule:create")
	public ResponseEntity<Object> create(@RequestBody CodeRuleForm codeRule) {
		CodeRuleEntity codeRuleEntity = new CodeRuleEntity();
		// 复制属性
		BeanUtils.copyProperties(codeRule, codeRuleEntity);
		codeRuleEntity.setCreateBy(getUserId());
		codeRuleEntity.setUpdateAt(new Date());
		codeRuleEntity.setCreateAt(new Date());
		ValidatorUtils.validateEntity(codeRule);
		// 保存主表
		codeRuleService.save(codeRuleEntity);

		int i = 0;
		List<CodeRuleItemEntity> codeRuleItemEntities = new ArrayList<>();
		// 子表更新
		for (CodeRuleItemEntity item : codeRule.getCodeRuleItems()) {
			CodeRuleItemEntity codeRuleItemEntity = new CodeRuleItemEntity();
			BeanUtils.copyProperties(item, codeRuleItemEntity);
			// 设置排序
			codeRuleItemEntity.setOrderNumber(i);
			codeRuleItemEntity.setCodeRuleId(codeRuleEntity.getId());
			i++;
			if (codeRuleItemEntity.getIfSerialKey() == null) {
				codeRuleItemEntity.setIfSerialKey(false);
			}
			codeRuleItemService.save(codeRuleItemEntity);
			codeRuleItemEntities.add(codeRuleItemEntity);
		}
		codeRuleEntity.setCurrentSerialKey(codeRuleService.initCurrentSerialKey(codeRuleItemEntities));
		codeRuleEntity.setCurrentSerial(1);
		codeRuleService.update(codeRuleEntity);
		return RD.ok(RD.build().put("status", 200));
	}

	/**
	 * 修改
	 */
	@SysLog("修改编码规则")
	@RequestMapping("/update")
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("sys:coderule:update")
	public ResponseEntity<Object> update(@RequestBody CodeRuleForm codeRule) {

		/**
		 * 更新 1.有id的就更新，没有的新增 2.与旧数据判断，如果少了的要删除
		 */
		CodeRuleEntity codeRuleEntity = new CodeRuleEntity();
		// 复制属性
		BeanUtils.copyProperties(codeRule, codeRuleEntity);
		codeRuleEntity.setUpdateBy(getUserId());

		// 查询旧数据id
		List<Long> oldIds = new ArrayList<>();
		Wrapper<CodeRuleItemEntity> codeRuleItemEntityWrapper = new EntityWrapper<CodeRuleItemEntity>()
				.eq("code_rule_id", codeRuleEntity.getId());
		List<CodeRuleItemEntity> oldCodeRuleItems = codeRuleItemService.selectList(codeRuleItemEntityWrapper);
		for (CodeRuleItemEntity oldItem : oldCodeRuleItems) {
			oldIds.add(oldItem.getId());
		}
		// 子表判断,有id的或id不为0的与数据库比较，删除一些不存在的
		int i = 0;
		for (CodeRuleItemEntity item : codeRule.getCodeRuleItems()) {
			item.setOrderNumber(i);
			i++;
			// 判断旧数据是否有这个id,有的更新，没的新增
			if (oldIds.contains(item.getId())) {
				item.setUpdateBy(getUserId());
				codeRuleItemService.update(item);
				oldIds.remove(item.getId());
			} else {
				item.setCodeRuleId(codeRuleEntity.getId());
				item.setCreateBy(getUserId());
				codeRuleItemService.save(item);
			}
		}
		// 删除已经没有的数据
		if (oldIds.size() > 0)
			codeRuleItemService.deleteBatch(oldIds.toArray(new Long[oldIds.size()]));
		// 更新主表
		codeRuleEntity.setCurrentSerialKey(codeRuleService.initCurrentSerialKey(codeRule.getCodeRuleItems()));
		codeRuleService.update(codeRuleEntity);
		return RD.ok(RD.build().put("status", 200));
	}

	/**
	 * 删除
	 */
	@SysLog("删除编码规则")
	@RequestMapping("/delete")
	@Transactional(rollbackFor = Exception.class)
	@RequiresPermissions("sys:coderule:delete")
	public ResponseEntity<Object> delete(@RequestBody Long[] ids) {

		// 删除子表
		for (Long id : ids) {
			Wrapper<CodeRuleItemEntity> codeRuleItemEntityWrapper = new EntityWrapper<CodeRuleItemEntity>()
					.eq("code_rule_id", id);
			List<CodeRuleItemEntity> codeRuleItemEntities = codeRuleItemService.selectList(codeRuleItemEntityWrapper);
			List<Long> itemIds = new ArrayList<>();
			for (CodeRuleItemEntity item : codeRuleItemEntities) {
				itemIds.add(item.getId());
			}
			if (itemIds.size() > 0) {
				codeRuleItemService.deleteBatch(itemIds.toArray(new Long[itemIds.size()]));
			}
		}
		codeRuleService.deleteBatch(ids);

		return RD.ok(RD.build().put("status", 200));
	}

	/*
	 * 高级查询
	 */
	@RequestMapping("/advancedSearch")
	public R advancedSearch(@RequestParam Map<String, Object> params) {
		List<CodeRuleEntity> list = codeRuleService.advancedSearch(params);
		PageUtils page = new PageUtils(list, 0, 10, 0);
		return R.ok().put("page", page);
	}

}
