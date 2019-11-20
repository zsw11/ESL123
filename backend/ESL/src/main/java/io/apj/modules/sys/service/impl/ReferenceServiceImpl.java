package io.apj.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.utils.DataUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;

import io.apj.modules.sys.dao.ReferenceDao;
import io.apj.modules.sys.entity.ReferenceEntity;
import io.apj.modules.sys.service.ReferenceService;

@Service("referenceService")
public class ReferenceServiceImpl extends ServiceImpl<ReferenceDao, ReferenceEntity> implements ReferenceService {

	@Autowired
	ReferenceService referenceService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<ReferenceEntity> page = this.selectPage(new Query<ReferenceEntity>(params).getPage(),
				new EntityWrapper<ReferenceEntity>());

		return new PageUtils(page);
	}

	@Override
	public void save(ReferenceEntity reference) {
		this.insert(reference);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ReferenceEntity reference) {
		ReferenceEntity entity = this.selectById(reference.getId());
		this.updateAllColumnById(reference);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		// 物理删除
		List<ReferenceEntity> list = this.selectBatchIds(Arrays.asList(ids));
		for (ReferenceEntity item : list) {
			this.deleteById(item);
		}
	}

	@Override
	public List<ReferenceEntity> advancedSerach(Map<String, Object> params) {
		return baseMapper.advancedSerach(params);
	}

	@Override
	public void insertTableReference(Object model) {
		// 获得类名
		String entityName = model.getClass().getSimpleName();
		// 主表Id
		Long mainId = null;
		try {
			Field[] fields = model.getClass().getDeclaredFields();
			for (int j = 0; j < fields.length; j++) { // 遍历所有属性
				Field f = fields[j];
				f.setAccessible(true);
				if (f.getName().equals("id")) {
					mainId = (Long) f.get(model);
				}
				// 判断是人员表
				if (entityName.equals("StaffEntity")) {
					// 跳过创建Id和更新Id
					if (f.getName().equals("createBy") || f.getName().equals("updateBy")) {
						continue;
					}
					// 属性名是Id结尾的
					if (f.getName().endsWith("Id") && f.get(model) != null) {
						ReferenceEntity reference = new ReferenceEntity();
						reference.setMainEntity(entityName);
						reference.setMainId(mainId);
						reference.setByEntity(f.getName().substring(0, f.getName().length() - 2) + "Entity");
						reference.setById((Long) f.get(model));
						this.insert(reference);
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 引用表保存和更新
	 *
	 * @param mainEntity
	 * @param mainId
	 * @param byEntity
	 * @param byId
	 * @param ifUpdate
	 */
	public void insertTableReference(String mainEntity, Long mainId, String byEntity, Long byId, Boolean ifUpdate) {
		if (ifUpdate) {
			// 先删除对应主表的引用数据，再保存引用数据
			Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>()
					.eq("main_entity", mainEntity).eq("main_id", mainId);
			referenceService.delete(referenceEntityWrapper);
		}
		if (byId != null) {
			ReferenceEntity reference = new ReferenceEntity();
			reference.setMainEntity(mainEntity);
			reference.setMainId(mainId);
			reference.setByEntity(byEntity);
			reference.setById(byId);
			referenceService.save(reference);
		}
	}

	/**
	 * 查询引用表数据
	 *
	 * @param byEntity
	 * @param byId
	 * @return
	 */
	public List<ReferenceEntity> deleteCheckReference(String byEntity, Long byId) {
		// 查询引用表数据
		Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>().eq("by_entity", byEntity)
				.eq("by_id", byId);
		List<ReferenceEntity> referenceEntities = referenceService.selectList(referenceEntityWrapper);
		return referenceEntities;
	}

	/**
	 * 删除此表的引用数据
	 *
	 * @param mainEntity
	 * @param mainId
	 */
	public void deleteTableReference(String mainEntity, Long mainId) {
		// 删除对应主表的引用数据
		Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>()
				.eq("main_entity", mainEntity).eq("main_id", mainId);
		referenceService.delete(referenceEntityWrapper);
	}

	/**
	 * 准确的删除一条
	 * 
	 * @param mainEntity
	 * @param mainId
	 * @param byEntity
	 * @param byId
	 */
	public void deleteOneReference(String mainEntity, Long mainId, String byEntity, Long byId) {

		// 先删除对应主表的引用数据，再保存引用数据
		Wrapper<ReferenceEntity> referenceEntityWrapper = new EntityWrapper<ReferenceEntity>()
				.eq("main_entity", mainEntity).eq("main_id", mainId).eq("by_entity", byEntity).eq("by_id", byId);
		referenceService.delete(referenceEntityWrapper);

	}
}
