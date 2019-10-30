package io.apj.modules.file.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.file.dao.SysFileDao;
import io.apj.modules.file.entity.SysFileEntity;
import io.apj.modules.file.service.SysFileService;

@Service("sysFileService")
public class SysFileServiceImpl extends ServiceImpl<SysFileDao, SysFileEntity>
		implements SysFileService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<SysFileEntity> page = this.selectPage(
				new Query<SysFileEntity>(params).getPage()
		);

		return new PageUtils(page);
	}

}
