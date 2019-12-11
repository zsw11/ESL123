package io.apj.modules.collection.dao;

import io.apj.modules.collection.entity.CompareItemEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

/**
 * Collection - Compare表子表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Mapper
public interface CompareItemDao extends BaseMapper<CompareItemEntity> {

    CompareItemEntity generateDataByWorkBook(Map<String, Object> params);
}
