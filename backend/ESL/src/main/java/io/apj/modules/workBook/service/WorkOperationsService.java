package io.apj.modules.workBook.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 分析表明细
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:29:02
 */
public interface WorkOperationsService extends IService<WorkOperationsEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void deletebyWrapper(Wrapper<WorkOperationsEntity> wrapper);

    List<String> getWorkBookFilePaths(List<WorkBookEntity> workBookEntities) throws IOException;
}
