package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ModelToolRelaEntity;

import java.util.Map;

/**
 * 机种治工具关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ModelToolRelaService extends IService<ModelToolRelaEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Page<Map<String, Object>> selectModelByToolId(Integer id, Map<String, Object> params);
}

