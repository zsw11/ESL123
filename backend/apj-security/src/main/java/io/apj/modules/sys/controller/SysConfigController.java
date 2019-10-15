/**
 * Copyright 2019 爱浦京产品
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.apj.modules.sys.controller;

import io.apj.common.annotation.SysLog;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.common.utils.RD;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.sys.entity.SysConfigEntity;
import io.apj.modules.sys.service.SysConfigService;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统配置信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月4日 下午6:55:53
 */
@RestController
@RequestMapping("/api/v1/config")
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;

	@Value("classpath:static/FilterData.json")
	private Resource filterData;

	/**
	 * 所有配置列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:config:list")
	public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {
		PageUtils page = sysConfigService.queryPage(params);

		return RD.listReturn(page.getData(), page.getTotalCount());
	}

	/**
	 * 根据code查询value
	 */
	@GetMapping("/getValueByCode")
	public ResponseEntity<Object> getValueByCode(@RequestParam Map<String, Object> params) {
		if (params.get("code") == null) {
			return RD.FORBIDDEN("403", "编码不能为空！");
		}
		String vaule = sysConfigService.getValue((String) params.get("code"));
		
		return RD.ok(vaule);
	}

	/**
	 * 配置信息
	 */
//	@GetMapping("/info/{id}")
//	public ResponseEntity<Object> info(@PathVariable("id") Long id) {
//		SysConfigEntity config = sysConfigService.selectById(id);
//
//		return RD.ok(config);
//	}
//	
	/**
	 * 配置信息
	 */
	@GetMapping("/detail")
	@RequiresPermissions("sys:config:info")
	public ResponseEntity<Object> detail(@RequestParam Long id) {
		SysConfigEntity config = sysConfigService.selectById(id);
		return RD.ok(config);
	}

	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@PostMapping("/save")
	@RequiresPermissions("sys:config:create")
	public ResponseEntity<Object> save(@RequestBody SysConfigEntity config) {
		config.setCreateBy(getUserId());
		config.setCreateAt(new Date());

		ValidatorUtils.validateEntity(config);

		sysConfigService.save(config);

		return RD.ok(RD.build());
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@PostMapping("/update")
	@RequiresPermissions("sys:config:update")
	public ResponseEntity<Object> update(@RequestBody SysConfigEntity config) {
		config.setUpdateBy(getUserId());
		config.setUpdateAt(new Date());

		ValidatorUtils.validateEntity(config);

		sysConfigService.update(config);

		return RD.ok(RD.build());
	}

	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@PostMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
		sysConfigService.deleteBatch(ids);

		return RD.NO_CONTENT(RD.build());
	}

	/**
	 * 获取高级查询条件配置
	 *
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/filterConfig")
	public R filterConfig() throws IOException {
		String areaData = IOUtils.toString(filterData.getInputStream(), Charset.forName("UTF-8"));

		return R.ok().put("data", JSON.parse(areaData));
	}

}
