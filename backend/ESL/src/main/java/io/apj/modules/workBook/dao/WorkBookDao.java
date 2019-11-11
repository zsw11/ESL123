package io.apj.modules.workBook.dao;

import io.apj.modules.workBook.entity.WorkBookEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 分析表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:03
 */
@Mapper
public interface WorkBookDao extends BaseMapper<WorkBookEntity> {
	
}
