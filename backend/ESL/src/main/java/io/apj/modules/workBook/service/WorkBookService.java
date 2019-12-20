package io.apj.modules.workBook.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.workBook.entity.WorkBookEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
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

	PageUtils queryPage(Map<String, Object> params) throws ParseException;

	ResponseEntity<Object> updateWorkBook(Map<String, Object> workBook);

	List<Integer> updateOperation(Map<String, Object> params);

	void createReports(Map<String, Object> params);

	WorkBookEntity getLastVersion(Integer modelId, String stlst, Integer phaseId);

	WorkBookEntity detailWithOperations(Integer id);

	void updateAll(Map<String, Object> params);

	/**
	 * 根据Wrapper条件删除所有符合条件的记录
	 * 
	 * @param wrapper
	 * @return
	 */
	void deleteByWrapper(Wrapper wrapper);

    void download(Map<String, Object> params, HttpServletResponse response) throws IOException;
}
