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

package io.apj.modules.file.controller;

import io.apj.common.exception.RRException;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.R;
import io.apj.modules.file.entity.SysFileEntity;
import io.apj.modules.file.service.SysFileService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author HarryHua 
 * @date 2019年4月30日 下午3:00:44 
 * @Description: 文件管理 
 * @throws
 */
@RestController
@RequestMapping("/api/v1/files")
public class SysFileController {
	
	@Autowired
	SysFileService sysFileService;
	
	@Value("${filePath}")
	String filePath;
	
	/**
	 * 上传文件
	 */
	@PostMapping("/upload")
	@RequiresPermissions("sys:file:all")
	public R uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		String uuid=UUID.randomUUID().toString().replace("-", "");
		String destFileName=uuid+suffix;
		String destFilePath = filePath+destFileName;
		File destFile = new File(destFilePath);
		try {
			file.transferTo(destFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SysFileEntity sysFileEntity=new SysFileEntity();
		sysFileEntity.setName(destFileName);
		sysFileEntity.setFilePath(destFilePath);
		sysFileEntity.setDescription(fileName);
		sysFileEntity.setCreateBy(123L);
		sysFileEntity.setCreateAt(new Date());
		sysFileService.insert(sysFileEntity);
		
		return R.ok();
	}

	@GetMapping("/search")
	@RequiresPermissions("sys:file:all")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = sysFileService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys:file:all")
	public R delete(@RequestBody Long[] ids) {
		sysFileService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

}
