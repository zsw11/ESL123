package io.apj.modules.file.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.file.entity.SysFileEntity;

import java.util.Map;

/**
 * 
 * @author HarryHua
 * @date 2019年4月30日 下午11:39:33 
 * @Description: 文件管理
 * @throws
 */
public interface SysFileService extends IService<SysFileEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

