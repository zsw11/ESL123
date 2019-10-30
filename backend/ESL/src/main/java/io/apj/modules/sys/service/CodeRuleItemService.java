package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.CodeRuleItemEntity;

import java.util.Map;
import java.util.List;

/**
 * 编码规则子项
 *
 * @author Sam
 *
 * @date 2018-12-18 11:34:01
 */
public interface CodeRuleItemService extends IService<CodeRuleItemEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
	 * 保存配置信息
	 */
    public void save(CodeRuleItemEntity codeRuleItem);

    /**
	 * 更新配置信息
	 */
	public void update(CodeRuleItemEntity codeRuleItem);

	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);

	/**
	 * 高级查询
	 */
	public List<CodeRuleItemEntity> advancedSearch(Map<String, Object> params);
}

