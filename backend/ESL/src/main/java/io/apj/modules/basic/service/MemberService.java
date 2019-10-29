package io.apj.modules.basic.service;

import com.baomidou.mybatisplus.service.IService;
import com.google.gson.JsonArray;
import io.apj.common.utils.PageUtils;
import io.apj.modules.basic.entity.MemberEntity;

import java.util.Map;

/**
 * 人员信息
 *
 * @author Royluo
 *
 * @date 2018-12-11 13:22:28
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
	 * 保存配置信息
	 */
    public void save(MemberEntity member);

    /**
	 * 更新配置信息
	 */
	public void update(MemberEntity member);

	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);

	/**
	 * 高级查询
	 */
	public Map<String,Object> advancedSearch(Map<String, Object> map,JsonArray fieldFilter);
}

