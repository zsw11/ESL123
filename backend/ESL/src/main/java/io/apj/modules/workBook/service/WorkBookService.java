package io.apj.modules.workBook.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
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

	ResponseEntity<Object> updateWorkBook(Map<String, Object> workBook);

	List<Integer> updateOperation(Map<String, Object> params);

	void createReports(Map<String, Object> params);

	WorkBookEntity getLastVersion(Integer modelId, String stlst, Integer phaseId);

	WorkBookEntity detailWithOperations(Integer id);
}
