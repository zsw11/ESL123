package io.apj.modules.masterData.dao;

import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 机种部品关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ModelPartRelaDao extends BaseMapper<ModelPartRelaEntity> {
    List<ModelEntity> selectModelByPartId(int id);
	
}
