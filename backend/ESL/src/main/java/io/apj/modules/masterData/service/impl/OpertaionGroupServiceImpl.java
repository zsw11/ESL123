package io.apj.modules.masterData.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
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
    public ResponseEntity<Object> insertOpGroup(Map<String, Object> map) {
        OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("OpertaionGroupEntity"),true),OpertaionGroupEntity.class);
        opertaionGroupService.insert(opertaionGroup);
        EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("code", (opertaionGroup.getCode()));
        OpertaionGroupEntity opertaionGroupEntity =  opertaionGroupService.selectOne(entityWrapper);
        // 主表id,更新到子表中
        List<OperationGroupOperationEntity> operationGroupOperationEntity = JSON.parseArray(map.get("OperationGroupOperationEntity").toString(),  OperationGroupOperationEntity.class);
        int id = opertaionGroupEntity.getId();
        if(operationGroupOperationEntity!=null && operationGroupOperationEntity.size()!=0){
            for(OperationGroupOperationEntity item : operationGroupOperationEntity){
                item.setOperationGroupId(id);
            }
            operationGroupOperationService.insertBatch(operationGroupOperationEntity);
        }

        return RD.ok(opertaionGroup);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> UpdataOpertaionGroup(Map<String, Object> map) {
        OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("OpertaionGroupEntity"),true),OpertaionGroupEntity.class);
        //更新主表
        opertaionGroupService.updateById(opertaionGroup);
        //获取主表id
        int id = opertaionGroup.getId();
        //删除原来子表
        EntityWrapper<OperationGroupOperationEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("operationGroupId", id);
        List<OperationGroupOperationEntity> operationGroupOperationEntities = operationGroupOperationService.selectList(entityWrapper);
        operationGroupOperationService.deleteBatchIds(operationGroupOperationEntities);
        //遍历子表
        List<OperationGroupOperationEntity> operationGroupOperationEntities1 = JSON.parseArray(map.get("OperationGroupOperationEntity").toString(),  OperationGroupOperationEntity.class);
        if(operationGroupOperationEntities1!=null && operationGroupOperationEntities1.size()!=0){
            for(OperationGroupOperationEntity item : operationGroupOperationEntities1){
                item.setOperationGroupId(id);
            }
            operationGroupOperationService.insertBatch(operationGroupOperationEntities1);
        }

        return RD.ok(opertaionGroup);
    }

}