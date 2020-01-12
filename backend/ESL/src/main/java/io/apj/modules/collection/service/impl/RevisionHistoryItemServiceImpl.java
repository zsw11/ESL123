package io.apj.modules.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.modules.report.entity.ChangeRecordItemEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.collection.dao.RevisionHistoryItemDao;
import io.apj.modules.collection.entity.RevisionHistoryItemEntity;
import io.apj.modules.collection.service.RevisionHistoryItemService;


@Service("revisionHistoryItemService")
public class RevisionHistoryItemServiceImpl extends ServiceImpl<RevisionHistoryItemDao, RevisionHistoryItemEntity> implements RevisionHistoryItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RevisionHistoryItemEntity> page = this.selectPage(
                new Query<RevisionHistoryItemEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    public List<RevisionHistoryItemEntity> getListBySWId(Integer id) {
        List<RevisionHistoryItemEntity> list = Collections.EMPTY_LIST;
        EntityWrapper<RevisionHistoryItemEntity> ew = new EntityWrapper<>();
        ew.eq("collection_revision_history_id",id);
        return selectList(ew);
    }

	@Override
	public void generateRevisionHistoryItem(List<Integer> workBookIds, Integer revisionHistoryId) {
		List<RevisionHistoryItemEntity> list = baseMapper.generateDataByWorkBook(workBookIds);
		if (list!=null&&list.size()>0){
			for(RevisionHistoryItemEntity entity : list){
				entity.setCollectionRevisionHistoryId(revisionHistoryId);
			}
		}
	}

}
