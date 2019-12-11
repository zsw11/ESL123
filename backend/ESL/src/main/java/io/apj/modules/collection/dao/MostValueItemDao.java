package io.apj.modules.collection.dao;

import io.apj.modules.collection.entity.MostValueItemEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * Collection - MOST Value 表
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:31:29
 */
@Mapper
public interface MostValueItemDao extends BaseMapper<MostValueItemEntity> {
    MostValueItemEntity generateDataByWorkBook(Integer workBookId);

}
