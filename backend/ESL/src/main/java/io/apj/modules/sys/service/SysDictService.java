package io.apj.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.sys.entity.SysDictEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典项
 *
 * @author samchen
 * 
 * @date 2018-12-05 14:16:20
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    HashMap<String, String> getDictDetail();
}

