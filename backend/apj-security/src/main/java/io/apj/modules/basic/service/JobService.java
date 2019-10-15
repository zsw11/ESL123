package io.apj.modules.basic.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.utils.PageUtils;
import io.apj.modules.basic.entity.JobEntity;

import java.util.Map;
import java.util.List;

/**
 * 岗位信息
 *
 * @author Royluo
 * 
 * @date 2018-12-11 13:22:27
 */
public interface JobService extends IService<JobEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
	 * 保存配置信息
	 */
    public void save(JobEntity job);
    
    /**
	 * 更新配置信息
	 */
	public void update(JobEntity job);
	
	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] list);
	
	/**
	 * 高级查询
	 */
	public List<JobEntity> selectList(Wrapper<JobEntity> wrapper);
	
	public EntityWrapper<JobEntity> basicSerachReturnWrapper(Map<String,Object> map);
}

