package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.CodeRuleEntity;
import io.apj.modules.sys.entity.CodeRuleItemEntity;

import java.util.Map;
import java.util.List;

/**
 * 编码规则
 *
 * @author Sam
 *
 * @date 2018-12-18 11:34:00
 */
public interface CodeRuleService extends IService<CodeRuleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 保存配置信息
	 */
	public void save(CodeRuleEntity codeRule);

	/**
	 * 更新配置信息
	 */
	public void update(CodeRuleEntity codeRule);

	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);

	/**
	 * 高级查询
	 */
	public List<CodeRuleEntity> advancedSearch(Map<String, Object> params);

	/**
	 * 根据编码生成流水号
	 */
	public String getSerialNumberByCode(String code);

	/**
	 *
	 */
	public String initCurrentSerialKey(List<CodeRuleItemEntity> codeRuleItems);
}
