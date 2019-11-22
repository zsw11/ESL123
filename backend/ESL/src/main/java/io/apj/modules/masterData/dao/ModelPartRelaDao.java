package io.apj.modules.masterData.dao;

import com.baomidou.mybatisplus.plugins.Page;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 机种部品关系
 * 
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
@Mapper
public interface ModelPartRelaDao extends BaseMapper<ModelPartRelaEntity> {
    List<Map<String,Object>> selectModelByPartId(Integer id, Page<Map<String,Object>> page);
	
}
