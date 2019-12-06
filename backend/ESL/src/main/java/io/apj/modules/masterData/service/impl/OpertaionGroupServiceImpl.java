package io.apj.modules.masterData.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.DataUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.modules.sys.service.SysDeptService;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

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
    @Autowired
    private SysDeptService deptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at");
        if(params.get("code") != null && params.get("code") != ""){
            entityWrapper.eq("code",params.get("code"));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("usedCount"))){
            entityWrapper.eq("used_count", Integer.parseInt((String) params.get("usedCount")));
        }
        if(StringUtils.isNotEmpty((CharSequence) params.get("deptId"))){
            entityWrapper.eq("dept_id", Integer.parseInt((String) params.get("deptId")));
        }

        Page<OpertaionGroupEntity> page = this.selectPage(
                new Query<OpertaionGroupEntity>(params).getPage(), entityWrapper
        );
        for (OpertaionGroupEntity entity : page.getRecords()) {
            EntityWrapper<OperationGroupOperationEntity> operationWrapper = new EntityWrapper();
            operationWrapper.eq("operation_group_id",entity.getId());
            entity.setCount(operationGroupOperationService.selectCount(operationWrapper));
            entity.setDeptName(deptService.selectById(entity.getDeptId()).getName());;
        }

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> insertOpGroup(Map<String, Object> map) {
        OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("operationGroup"),true),OpertaionGroupEntity.class);
        opertaionGroupService.insert(opertaionGroup);
        EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("code", (opertaionGroup.getCode()));
        OpertaionGroupEntity opertaionGroupEntity =  opertaionGroupService.selectOne(entityWrapper);
        // 主表id,更新到子表中
        int id = opertaionGroupEntity.getId();
        List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("operations");
        for(int i= 0; i<maps.size(); i++) {
            OperationGroupOperationEntity operationGroupOperationEntity = new OperationGroupOperationEntity();
            DataUtils.transMap2Bean2(maps.get(i), operationGroupOperationEntity);
            operationGroupOperationEntity.setOperationGroupId(id);
            operationGroupOperationService.insert(operationGroupOperationEntity);
        }
//        List<OperationGroupOperationEntity> operationGroupOperationEntity = JSONObject.parseArray(map.get("operations").toString(),  OperationGroupOperationEntity.class);
//        int id = opertaionGroupEntity.getId();
//        if(operationGroupOperationEntity!=null && operationGroupOperationEntity.size()!=0){
//            for(OperationGroupOperationEntity item : operationGroupOperationEntity){
//                item.setOperationGroupId(id);
//            }
//            operationGroupOperationService.insertBatch(operationGroupOperationEntity);
//        }

        return RD.ok(opertaionGroup);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> UpdataOpertaionGroup(Map<String, Object> map) {
        OpertaionGroupEntity opertaionGroup = JSON.parseObject(JSONObject.toJSONString(map.get("operationGroup"),true),OpertaionGroupEntity.class);
        //更新主表
        opertaionGroupService.updateById(opertaionGroup);
        //获取主表id
        int id = opertaionGroup.getId();
        //删除原来子表
        EntityWrapper<OperationGroupOperationEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("operation_group_id", id);
        List<OperationGroupOperationEntity> operationGroupOperationEntities = operationGroupOperationService.selectList(entityWrapper);
        int ids;
        for(OperationGroupOperationEntity items: operationGroupOperationEntities){
             ids= items.getId();
            if(ids!=0){
                operationGroupOperationService.deleteById(ids);
            }
        }

        //遍历子表
//        OperationGroupOperationEntity[] operationGroupOperationEntity = (OperationGroupOperationEntity[]) map.get("operations");
        List<Map<String, Object>> maps = (List<Map<String, Object>>) map.get("operations");
        for(int i= 0; i<maps.size(); i++) {
            OperationGroupOperationEntity operationGroupOperationEntity = new OperationGroupOperationEntity();
            DataUtils.transMap2Bean2(maps.get(i), operationGroupOperationEntity);
            operationGroupOperationEntity.setOperationGroupId(id);
            operationGroupOperationService.insert(operationGroupOperationEntity);
        }

        return RD.ok(opertaionGroup);
    }

}