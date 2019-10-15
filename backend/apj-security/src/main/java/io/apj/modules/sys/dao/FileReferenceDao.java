package io.apj.modules.sys.dao;

import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.FileReferenceEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

/**
 * 文件引用关系表
 * 
 * @author samchen
 * 
 * @date 2019-02-22 16:10:49
 */
@Mapper
public interface FileReferenceDao extends BaseMapper<FileReferenceEntity> {
	List<FileReferenceEntity> advancedSerach(Map<String, Object> params);
}
