package io.apj.modules.masterData.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.apj.common.utils.RD;
import io.apj.modules.sys.controller.AbstractController;
import io.apj.modules.sys.entity.ReferenceEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public RD save(@RequestBody Map<String, Object> map){
        opertaionGroupService.insertOpGroup(map);
        return RD.build();
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping("/update")
    @RequiresPermissions("masterData:opertaiongroup:update")
    public RD update(@RequestBody Map<String, Object> map){
        opertaionGroupService.UpdataOpertaionGroup(map);

        return RD.build();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("masterData:opertaiongroup:delete")
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids){
        for (int i = 0; i < ids.length; i++) {
            List<ReferenceEntity> referenceEntities = deleteCheckReference("opertaiongroup", ids[i].longValue());
            if (!referenceEntities.isEmpty()) {
                for (ReferenceEntity reference : referenceEntities) {
                    return RD.INTERNAL_SERVER_ERROR(reference.getByEntity() + "，id=" + reference.getById()
                            + " 在表：" + reference.getMainEntity() + "，id=" + reference.getMainId() + "存在引用关系，不能删除！");
                }
            } else {
                // 删除引用表关系
                deleteTableReference("opertaiongroup", ids[i].longValue());
            }
        }
		opertaionGroupService.deleteBatchIds(Arrays.asList(ids));

        return RD.NO_CONTENT(RD.build());
    }

}
