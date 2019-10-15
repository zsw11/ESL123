package io.apj.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;

import io.apj.modules.sys.dao.CodeRuleItemDao;
import io.apj.modules.sys.entity.CodeRuleItemEntity;
import io.apj.modules.sys.service.CodeRuleItemService;

@Service("codeRuleItemService")
public class CodeRuleItemServiceImpl extends ServiceImpl<CodeRuleItemDao, CodeRuleItemEntity>
		implements CodeRuleItemService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<CodeRuleItemEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper.orderBy("create_at", false);
		Page<CodeRuleItemEntity> page = this.selectPage(new Query<CodeRuleItemEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	@Override
	public void save(CodeRuleItemEntity codeRuleItem) {
		this.insert(codeRuleItem);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(CodeRuleItemEntity codeRuleItem) {
		CodeRuleItemEntity entity = this.selectById(codeRuleItem.getId());
		codeRuleItem.setCreateAt(entity.getCreateAt());
		codeRuleItem.setCreateBy(entity.getCreateBy());
		codeRuleItem.setDeleteAt(entity.getDeleteAt());
		this.updateAllColumnById(codeRuleItem);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 逻辑删除
		List<CodeRuleItemEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (CodeRuleItemEntity item : list) {
			item.setDeleteAt(new Date());
		}
		this.updateBatchById(list, list.size());
	}

	@Override
	public List<CodeRuleItemEntity> advancedSearch(Map<String, Object> params) {
		return baseMapper.advancedSearch(params);
	}

}
