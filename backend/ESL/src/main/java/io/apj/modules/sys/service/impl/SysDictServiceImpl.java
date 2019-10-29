package io.apj.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.modules.sys.entity.SysDictTypeEntity;
import io.apj.modules.sys.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.sys.dao.SysDictDao;
import io.apj.modules.sys.entity.SysDictEntity;
import io.apj.modules.sys.service.SysDictService;

@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

	@Autowired
	private SysDictTypeService sysDictTypeService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		EntityWrapper<SysDictEntity> entityWrapper = new EntityWrapper<>();
		entityWrapper
				.like(StringUtils.isNotEmpty((CharSequence) params.get("dictTypeId")), "dict_type_id",
						(String) params.get("dictTypeId"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("type")), "code", (String) params.get("type"))
				.like(StringUtils.isNotEmpty((CharSequence) params.get("name")), "name", (String) params.get("name"));
		Page<SysDictEntity> page = this.selectPage(new Query<SysDictEntity>(params).getPage(), entityWrapper);

		return new PageUtils(page);
	}

	/**
	 * 获得字典map
	 *
	 * @return
	 */
	public HashMap<String, String> getDictDetail() {
		HashMap<String, String> hashMap = new HashMap<>();
		// 查询字典表类型
		Wrapper<SysDictTypeEntity> sysDictTypeEntityWrapper = new EntityWrapper<SysDictTypeEntity>();
		List<SysDictTypeEntity> dicsType = sysDictTypeService.selectList(sysDictTypeEntityWrapper);
		for (SysDictTypeEntity dictTypeEntity : dicsType) {
			String type = dictTypeEntity.getType();
			Wrapper<SysDictEntity> sysDictEntityWrapper = new EntityWrapper<SysDictEntity>()
					.eq("dict_type_id", dictTypeEntity.getId());
			List<SysDictEntity> dics = this.selectList(sysDictEntityWrapper);
			for (SysDictEntity dictEntity : dics) {
				String key = type + "_" + dictEntity.getCode();
				String value = dictEntity.getName();
				hashMap.put(key, value);
			}
		}

		return hashMap;
	}

}
