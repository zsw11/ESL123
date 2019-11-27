package io.apj.modules.masterData.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.OpertaionGroupDao;
import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.OpertaionGroupService;
import org.springframework.transaction.annotation.Transactional;


@Service("opertaionGroupService")
public class OpertaionGroupServiceImpl extends ServiceImpl<OpertaionGroupDao, OpertaionGroupEntity> implements OpertaionGroupService {
    @Autowired
    private OpertaionGroupService opertaionGroupService;
    @Autowired
    private OperationGroupOperationService operationGroupOperationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<OpertaionGroupEntity> page = this.selectPage(
                new Query<OpertaionGroupEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public RD insertOpGroup(Map<String, Object> map) {
        OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("OpertaionGroupEntity"),true),OpertaionGroupEntity.class);
        opertaionGroupService.insert(opertaionGroup);
        EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("code", (opertaionGroup.getCode()));
        OpertaionGroupEntity opertaionGroupEntity =  opertaionGroupService.selectOne(entityWrapper);
        // 主表id,更新到子表中
        int id = opertaionGroupEntity.getId();
        List<OperationGroupOperationEntity> operationGroupOperationEntities = operationGroupOperationService.selectList(new EntityWrapper<OperationGroupOperationEntity>().eq("operation_group_id", id));
        for(OperationGroupOperationEntity item : operationGroupOperationEntities){
            item.setOperationGroupId(id);
        }
        operationGroupOperationService.insertBatch(operationGroupOperationEntities);
        return RD.build();
    }

    @Override
    @Transactional
    public RD UpdataOpertaionGroup(Map<String, Object> map) {
        OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("OpertaionGroupEntity"),true),OpertaionGroupEntity.class);
        //更新主表
        opertaionGroupService.updateById(opertaionGroup);
        //获取主表id
        OpertaionGroupEntity opertaionGroupEntity = opertaionGroupService.selectOne((Wrapper<OpertaionGroupEntity>) map.get("OpertaionGroupEntity"));
        int id = opertaionGroupEntity.getId();
        //获取子表
        EntityWrapper<OperationGroupOperationEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("operationGroupId", id);
        List<OperationGroupOperationEntity> operationGroupOperationEntities =  operationGroupOperationService.selectList(entityWrapper);
        //遍历子表
        for(OperationGroupOperationEntity item : operationGroupOperationEntities){
            item.setOperationGroupId(id);
        }
        operationGroupOperationService.insertBatch(operationGroupOperationEntities);
        return RD.build();
    }

}