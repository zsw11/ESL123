package io.apj.modules.workBook.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;

import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.workBook.entity.WorkBookEntity;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
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

	WorkBookEntity getLastVersion(Integer modelId, String stlst, Integer phaseId, String destinations, String versionNumber);

	WorkBookEntity detailWithOperations(Integer id);

	ResponseEntity<Object> updateAll(Map<String, Object> params);

	void deleteBookByIds(Integer[] id);

	List<ReportEntity> deptReports(Integer id);

	void selectLock();

	WorkBookEntity copyWorkBook(WorkBookEntity workBook, Integer workBookId);

	/**
	 * 根据Wrapper条件软删除所有符合条件的记录
	 * 
	 * @param wrapper
	 * @return
	 */
	void deleteByWrapper(Wrapper wrapper);

	/**
	 * 通过ids软删除
	 * @param ids
	 */
	void deleteByIds(Collection<? extends Serializable> ids);

	List<String> download(Map<String, Object> params, HttpServletResponse response) throws IOException;

	List<WorkBookEntity> filterUniquePhaseAndModelAndStlstOfWorkBooks(List<WorkBookEntity> workBooks);

	List<Integer> filterWorkBookIdsByPhaseAndModelAndStlst(List<WorkBookEntity> workBooks, Integer modelId, String stlst, Integer phaseId, String destinations, String versionNumber);

	/**
	 * timeValue/mu/secondConvert/remark1数据处理
	 * @param workBookId
	 * @return
	 */
	Map<String , Object> dealData(Integer workBookId);

}
