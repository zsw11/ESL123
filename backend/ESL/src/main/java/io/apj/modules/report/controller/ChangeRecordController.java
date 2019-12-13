package io.apj.modules.report.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.report.entity.ChangeRecordEntity;
import io.apj.modules.report.entity.ChangeRecordItemEntity;
import io.apj.modules.report.service.ChangeRecordItemService;
import io.apj.modules.report.service.ChangeRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 履历表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-26 13:23:57
 */
@RestController
@RequestMapping("/api/v1/changerecord")
public class ChangeRecordController {
	@Autowired
	private ChangeRecordService changeRecordService;

	@Autowired
	private ChangeRecordItemService changeRecordItemService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("report:changerecord:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		//判断是否还可以提交审批
        PageUtils page = changeRecordService.selectListTest(params);

		return RD.success(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("report:changerecord:detail")
	public ResponseEntity<Object> info(@PathVariable("id") Integer id) {
		ChangeRecordEntity changeRecord = changeRecordService.selectById(id);
		EntityWrapper<ChangeRecordItemEntity> ew = new EntityWrapper<>();
		ew.eq("report_change_record_id",changeRecord.getId());
		changeRecord.setItems(changeRecordItemService.selectList(ew));
		return RD.success(changeRecord);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("report:changerecord:create")
	public ResponseEntity<Object> save(@RequestBody ChangeRecordEntity changeRecord) {
		changeRecordService.insert(changeRecord);
		return RD.success(changeRecord);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("report:changerecord:update")
	public ResponseEntity<Object> update(@RequestBody ChangeRecordEntity changeRecord) {
		changeRecordService.updateEntity(changeRecord);
		return RD.success(changeRecord);
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("report:changerecord:delete")
	public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
		changeRecordService.deleteBatchIds(Arrays.asList(ids));

		return RD.NO_CONTENT(RD.build());
	}

}
