package io.apj.modules.sys.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;

import io.apj.modules.sys.dao.SysDictTypeDao;
import io.apj.modules.sys.entity.SysDictEntity;
import io.apj.modules.sys.entity.SysDictTypeEntity;
import io.apj.modules.sys.service.SysDictTypeService;

@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeDao, SysDictTypeEntity>
		implements SysDictTypeService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<SysDictTypeEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper
				.like(StringUtils.isNotEmpty((CharSequence) params.get("type")), "type", (String) params.get("type"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("name")), "name", (String) params.get("name"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("remark")), "remark",
						(String) params.get("remark"));
		Page<SysDictTypeEntity> page = this.selectPage(new Query<SysDictTypeEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	/**
	 * 根据字典类型和子项编码查询子项名称
	 *
	 * @param type
	 * @param code
	 * @return
	 */
	@Override
	public String getNameByTypeAndCode(String type, String code) {
		String name = baseMapper.getNameByTypeAndCode(type, code);
		if (name == null) {
			return "";
		}
		return name;
	}

	/**
	 * 根据字典类型和查询子项列表
	 *
	 * @param type
	 * @return
	 */
	@Override
	public List<SysDictEntity> getListByType(String type) {
		return baseMapper.getListByType(type);
	}

}
