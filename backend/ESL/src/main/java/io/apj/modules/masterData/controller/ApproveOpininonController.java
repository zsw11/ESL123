package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonElement;

import cn.hutool.core.util.PinyinUtil;
import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.ApproveOpininonEntity;
import io.apj.modules.masterData.service.ApproveOpininonService;
import io.apj.common.utils.PageUtils;

/**
 * 常用审批意见
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:48:29
 */
@RestController
@RequestMapping("/api/v1/approveopininon")
public class ApproveOpininonController extends AbstractController {
	@Autowired
	private ApproveOpininonService approveOpininonService;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("masterData:approveopininon:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = approveOpininonService.queryPage(params);
		return RD.ok(page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/detail/{id}")
	@RequiresPermissions("masterData:approveopininon:info")
	public RD info(@PathVariable("id") Integer id) {
		ApproveOpininonEntity approveOpininon = approveOpininonService.selectById(id);

		return RD.build().put("data", approveOpininon);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/create")
	@RequiresPermissions("masterData:approveopininon:create")
	public RD save(@RequestBody ApproveOpininonEntity approveOpininon) {
		approveOpininon.setCreateBy(getUserId().intValue());
		approveOpininon.setPinyin(PinyinUtil.getPinYin(approveOpininon.getApproveOperation()));
		approveOpininonService.insert(approveOpininon);

		return RD.build().put("code", 200);
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("masterData:approveopininon:update")
	public RD update(@RequestBody ApproveOpininonEntity approveOpininon) {
		approveOpininon.setPinyin(PinyinUtil.getPinYin(approveOpininon.getApproveOperation()));
		approveOpininonService.updateById(approveOpininon);

		return RD.build().put("code", 200);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("masterData:approveopininon:delete")
	public RD delete(@RequestBody Integer[] ids) {
		approveOpininonService.deleteBatchIds(Arrays.asList(ids));

		return RD.build().put("code", 200);
	}

}
