package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.annotation.DataFilter;
import io.apj.common.exception.RRException;
import io.apj.common.utils.*;
import io.apj.common.validator.ValidatorUtils;
import io.apj.modules.masterData.dao.ActionDao;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Service("actionService")
public class ActionServiceImpl extends ServiceImpl<ActionDao, ActionEntity> implements ActionService {
    @Autowired
    private ActionService actionService;

    @Override
    @DataFilter(subDept = true, user = true, deptId = "dept_id")
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<ActionEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at", false)
                .like(params.get("remark") != null && params.get("remark") != "", "remark",
                        (String) params.get("remark"))
                .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
        if (params.get("name") != null && params.get("name") != "") {
            params.put("name", ((String) params.get("name")).replace('*', '%'));
            entityWrapper.andNew("UPPER(pinyin) like '%" + ((String) params.get("name")).toUpperCase() + "%' "
                    + "or UPPER(name) like '%" + ((String) params.get("name")).toUpperCase() + "%'");
        }

        Page<ActionEntity> page = this.selectPage(new Query<ActionEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actionImport(Map<String, Object> map) {
        List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("data");
        for (Map<String, Object> i : maps) {
            if (StringUtils.isEmpty((CharSequence) i.get("action.name"))) {
                return RD.INTERNAL_SERVER_ERROR("导入时关键词名称为空，请检查关键词名称是否为空");
            }
        }
        List<ActionEntity> actionEntityList = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            ActionEntity actionEntity = new ActionEntity();
            // deviceMap
            Map<String, Object> deviceMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : maps.get(i).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String[] keyStrs = key.split("\\.");
                // 设备
                if (keyStrs[0].equals("action")) {
                    if (keyStrs[1].equals("common")) {
                        if (value.equals("是")) {
                            deviceMap.put(keyStrs[1], true);
                        } else {
                            deviceMap.put(keyStrs[1], false);
                        }
                        continue;
                    }
                    deviceMap.put(keyStrs[1], value);
                }
            }
            DataUtils.transMap2Bean2(deviceMap, actionEntity);
            ValidatorUtils.validateEntity(actionEntity, i);
            actionEntity.setCreateBy((Integer) map.get("userID"));
            actionEntity.setDeptId((Integer) map.get("deptId"));
            actionEntityList.add(actionEntity);
        }
        try {
            actionService.insertBatch(actionEntityList, Constant.importNum);
        } catch (MybatisPlusException e) {
            throw new RRException("关键词导入失败，请检查关键词是否重复", 500);
        }
        Map<String, Integer> data = new HashMap<String, Integer>();
        data.put("code", 200);
        return RD.success(data);
    }

    @Override
    public void deleteList(List<ActionEntity> actionEntityList) {
        for (ActionEntity item : actionEntityList) {
            item.setDeleteAt(new Date());
        }
        if (actionEntityList.size() > 0) {
            this.updateAllColumnBatchById(actionEntityList);
        }

    }

    @Override
    public void deleteByIds(Collection<? extends Serializable> ids) {
        List<ActionEntity> actionEntityList = this.selectBatchIds(ids);
        for (ActionEntity item : actionEntityList) {
            item.setDeleteAt(new Date());
        }
        if (actionEntityList.size() > 0) {
            this.updateAllColumnBatchById(actionEntityList);
        }
    }

    @Override
    public void deleteByWrapper(Wrapper<ActionEntity> wrapper) {
        List<ActionEntity> actionEntityList = this.selectList(wrapper);
        for (ActionEntity item : actionEntityList) {
            item.setDeleteAt(new Date());
        }
        if (actionEntityList.size() > 0) {
            this.updateAllColumnBatchById(actionEntityList);
        }
    }

    @Override
    public void updatePinAndDataById(ActionEntity actionEntity) {
        actionEntity.setPinyin(PinyinUtil.getPinYin(actionEntity.getName()));
        actionEntity.setUpdateAt(new Date());
        this.updateById(actionEntity);
    }

}