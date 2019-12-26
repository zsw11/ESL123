package io.apj.modules.report.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.common.utils.RD;
import io.apj.modules.collection.entity.CompareEntity;
import io.apj.modules.collection.entity.MostValueEntity;
import io.apj.modules.collection.entity.RevisionHistoryEntity;
import io.apj.modules.collection.entity.StationTimeEntity;
import io.apj.modules.collection.service.CompareService;
import io.apj.modules.collection.service.MostValueService;
import io.apj.modules.collection.service.RevisionHistoryService;
import io.apj.modules.collection.service.StationTimeService;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.service.*;
import io.apj.modules.report.dao.ApproveDao;
import io.apj.modules.report.dao.AshcraftTableDao;
import io.apj.modules.report.entity.*;
import io.apj.modules.report.service.*;
import io.apj.modules.sys.service.SysDeptService;
import io.apj.modules.workBook.entity.WorkBookEntity;
import io.apj.modules.workBook.service.WorkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("AshcraftTableService")
public class AshcraftTableServiceImpl extends ServiceImpl<AshcraftTableDao, AshcraftTableEntity> implements AshcraftTableService {

}
