package io.apj.modules.sys.service.impl;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.apj.common.utils.DateUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.sys.dao.CodeRuleDao;
import io.apj.modules.sys.entity.CodeRuleEntity;
import io.apj.modules.sys.entity.CodeRuleItemEntity;
import io.apj.modules.sys.service.CodeRuleItemService;
import io.apj.modules.sys.service.CodeRuleService;
import io.apj.modules.sys.entity.SysUserEntity;

@Service("codeRuleService")
public class CodeRuleServiceImpl extends ServiceImpl<CodeRuleDao, CodeRuleEntity> implements CodeRuleService {

	@Autowired
	private CodeRuleItemService codeRuleItemService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<CodeRuleEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper
				.like(!"".equals(params.get("code")) && params.get("code") != null, "code", (String) params.get("code"))
				.like(!"".equals(params.get("name")) && params.get("name") != null, "name",
						(String) params.get("name"));

		Page<CodeRuleEntity> page = this.selectPage(new Query<CodeRuleEntity>(params).getPage(), entityWrapper);
		entityWrapper.orderBy("create_at", false);
		return new PageUtils(page);
	}

	@Override
	public void save(CodeRuleEntity codeRule) {
		this.insert(codeRule);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(CodeRuleEntity codeRule) {
		CodeRuleEntity entity = this.selectById(codeRule.getId());
		codeRule.setCreateAt(entity.getCreateAt());
		codeRule.setDeleteAt(entity.getDeleteAt());
		codeRule.setCreateBy(entity.getCreateBy());
		this.updateAllColumnById(codeRule);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 逻辑删除
		List<CodeRuleEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (CodeRuleEntity item : list) {
			item.setDeleteAt(new Date());
		}
		this.updateBatchById(list, list.size());
	}

	@Override
	public List<CodeRuleEntity> advancedSearch(Map<String, Object> params) {
		return baseMapper.advancedSearch(params);
	}

	/**
	 * 格式化序号
	 *
	 * @param n 序号
	 * @param l 位数
	 * @return
	 */
	public static String retCode(int n, int l) {
		String num = n + "";
		if (num.length() < l) {
			for (int i = 0; i <= l - num.length(); i++) {
				num = "0" + num;
			}
			return num;
		}
		System.out.println("++++" + num);
		return num;
	}

	/**
	 * 初始化编号键
	 */
	public String initCurrentSerialKey(List<CodeRuleItemEntity> codeRuleItems) {
		String num = "";
		String year = DateUtils.format(new Date(), "yyyy");
		String month = DateUtils.format(new Date(), "MM");
		String day = DateUtils.format(new Date(), "dd");
		for (CodeRuleItemEntity item : codeRuleItems) {
			if (item.getIfSerialKey()) {
				switch (item.getType()) {
				case "year": {
					num += year;
					break;
				}
				case "month": {
					num += month;
					break;
				}
				case "day": {
					num += day;
					break;
				}
				case "fixed": {
					num += item.getFixedValue() == null ? "" : item.getFixedValue();
					break;
				}
				default:
					break;
				}
			}
		}
		return num;
	}

	/**
	 * 生成指定流水编码
	 */
	@Override
	public String getSerialNumberByCode(String code) {
		SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
		if (code == null) {
			return null;
		}
		CodeRuleEntity codeRule = this.selectOne(new EntityWrapper<CodeRuleEntity>().eq("code", code));
		if (codeRule == null) {
			return null;
		}
		List<CodeRuleItemEntity> codeRuleItemList = codeRuleItemService.selectList(
				new EntityWrapper<CodeRuleItemEntity>().eq("code_rule_id", codeRule.getId()).orderBy("order_number"));

		String orderNumber = "";
		int serialNumber = codeRule.getCurrentSerial();
		String serialKey = "";
		String num = "";
		String year = DateUtils.format(new Date(), "yyyy");
		String month = DateUtils.format(new Date(), "MM");
		String day = DateUtils.format(new Date(), "dd");
		for (CodeRuleItemEntity item : codeRuleItemList) {
			switch (item.getType()) {
			case "year": {
				orderNumber += year;
				break;
			}
			case "month": {
				orderNumber += month;
				break;
			}
			case "day": {
				orderNumber += day;
				break;
			}
			case "fixed": {
				orderNumber += item.getFixedValue();
				break;
			}
			case "number": {
				if (item.getBeginNumber() > serialNumber) {
					serialNumber = item.getBeginNumber();
				} else {
					serialNumber += 1;
				}

				// 判断序号键
				serialKey = this.initCurrentSerialKey(codeRuleItemList);
				// 如果编号前缀不一致，默认重新开始
				if (!serialKey.equals(codeRule.getCurrentSerialKey())) {
					serialNumber = item.getBeginNumber();
				}
				num = retCode(serialNumber, item.getNumberBit());
				break;
			}
			default:
				break;
			}
		}

		codeRule.setUpdateAt(new Date());
		codeRule.setUpdateBy(user.getId());
		codeRule.setCurrentSerialKey(serialKey);
		codeRule.setCurrentSerial(serialNumber);
		this.update(codeRule);

		return orderNumber + num;
	}

}
