package io.apj.modules.workBook.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.workBook.entity.WorkBookEntity;

import java.util.Map;

/**
 * 分析表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:03
 */
public interface WorkBookService extends IService<WorkBookEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
