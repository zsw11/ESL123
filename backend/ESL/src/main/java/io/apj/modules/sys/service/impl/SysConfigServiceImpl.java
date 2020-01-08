/**
 * Copyright 2019 爱浦京产品
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.apj.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import io.apj.common.exception.RRException;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.sys.dao.SysConfigDao;
import io.apj.modules.sys.entity.SysConfigEntity;
import io.apj.modules.sys.redis.SysConfigRedis;
import io.apj.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {
	@Autowired
	private SysConfigRedis sysConfigRedis;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String paramKey = (String) params.get("paramKey");

		Page<SysConfigEntity> page = this.selectPage(new Query<SysConfigEntity>(params).getPage(),
				new EntityWrapper<SysConfigEntity>().like(StringUtils.isNotBlank(paramKey), "param_key", paramKey)
						.eq("status", 1).isNull("delete_at"));

		return new PageUtils(page);
	}

	@Override
	public void save(SysConfigEntity config) {
		this.insert(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysConfigEntity config) {
		SysConfigEntity c = this.selectById(config.getId());
		config.setCreateAt(c.getCreateAt());
		config.setCreateBy(c.getCreateBy());
		config.setDeleteAt(c.getDeleteAt());
		this.updateAllColumnById(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateValueByKey(String key, String value) {
		baseMapper.updateValueByKey(key, value);
		sysConfigRedis.delete(key);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
//		for (Long id : ids) {
//			SysConfigEntity config = this.selectById(id);
//			sysConfigRedis.delete(config.getParamKey());
//		}

//		this.deleteBatchIds(Arrays.asList(ids));
		// 逻辑删除
		List<SysConfigEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (SysConfigEntity item : list) {
			item.setDeleteAt(new Date());
		}
		this.updateBatchById(list, list.size());
	}

	@Override
	public String getValue(String key) {
		SysConfigEntity config = sysConfigRedis.get(key);
		if (config == null) {
			config = baseMapper.queryByKey(key);
			SysConfigEntity c = this.selectById(config.getId());
			config.setCreateAt(c.getCreateAt());
			config.setCreateBy(c.getCreateBy());
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getParamValue();
	}

	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if (StringUtils.isNotBlank(value)) {
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败");
		}
	}
}
