package io.apj.modules.workBook.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.entity.WorkOperationsEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
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

	void workOperationImport(Map<String, Object> params);

	void workOperationExport(Map<String, Object> map, HttpServletResponse response) throws Exception;

	/**
	 * 通过条件软删除
	 * @param wrapper
	 */
	void deletebyWrapper(Wrapper<WorkOperationsEntity> wrapper);

}
