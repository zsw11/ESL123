package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.JsonElement;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.service.OperationGroupOperationService;
import io.apj.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.OpertaionGroupService;
import io.apj.common.utils.PageUtils;


/**
 * 手顺组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
@RestController
@RequestMapping("/api/v1/opertaiongroup")
public class OpertaionGroupController extends AbstractController {
    @Autowired
    private OpertaionGroupService opertaionGroupService;
    @Autowired
    private OperationGroupOperationService operationGroupOperationService;

    /**
     * 列表
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("masterData:opertaiongroup:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = opertaionGroupService.queryPage(params);
        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    @RequiresPermissions("masterData:opertaiongroup:info")
    public RD info(@PathVariable("id") Integer id){
		OpertaionGroupEntity opertaionGroup = opertaionGroupService.selectById(id);

        return RD.build().put("data", opertaionGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    @RequiresPermissions("masterData:opertaiongroup:create")
    @Transactional
    public RD save(@RequestBody OpertaionGroupEntity opertaionGroup, @RequestBody OperationGroupOperationEntity operationGroupOperationEntity){
        opertaionGroupService.insert(opertaionGroup);
        EntityWrapper<OpertaionGroupEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("code",opertaionGroup.getCode());
        OpertaionGroupEntity opertaionGroupEntity =  opertaionGroupService.selectOne(entityWrapper);
        // 主表id
        int id = opertaionGroupEntity.getId();
        List<OperationGroupOperationEntity> operationGroupOperationEntities = operationGroupOperationService.selectList(new EntityWrapper<OperationGroupOperationEntity>().eq("operation_group_id",id));
        operationGroupOperationEntity.setOperationGroupId(id);
        operationGroupOperationService.insertBatch(operationGroupOperationEntities);
        return RD.build().put("code", 200);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:opertaiongroup:update")
    @Transactional
    public RD update(@RequestBody OpertaionGroupEntity opertaionGroup, @RequestBody OperationGroupOperationEntity operationGroupOperationEntity){
        //更新主表
		opertaionGroupService.updateById(opertaionGroup);
		//刪除子表
        EntityWrapper<OperationGroupOperationEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("operationGroupId", opertaionGroup.getId());
        operationGroupOperationService.delete(entityWrapper);
        //主id
        int id = opertaionGroup.getId();
        List<OperationGroupOperationEntity> operationGroupOperationEntities = operationGroupOperationService.selectList(new EntityWrapper<OperationGroupOperationEntity>().eq("operation_group_id",id));
        operationGroupOperationEntity.setOperationGroupId(id);
        //插入子表
        operationGroupOperationService.insertBatch(operationGroupOperationEntities);
        return RD.build().put("code", 200);
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:opertaiongroup:delete")
    public RD delete(@RequestBody Integer[] ids){
		opertaionGroupService.deleteBatchIds(Arrays.asList(ids));

        return RD.build().put("code", 200);
    }

}
