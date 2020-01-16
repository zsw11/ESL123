package io.apj.modules.masterData.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.NodeModelWorkstationRelaEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.NodeModelWorkstationRelaService;
import io.apj.modules.masterData.service.WorkstationService;



/**
 * 报表组部门关系
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2020-01-15 18:11:00
 */
@RestController
@RequestMapping("/api/v1/nodemodelworkstationrela")
public class NodeModelWorkstationRelaController {
    @Autowired
    private NodeModelWorkstationRelaService nodeModelWorkstationRelaService;
    @Autowired
	private ModelService modelService;
	@Autowired
	private WorkstationService workstationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:list")
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params){
        PageUtils page = nodeModelWorkstationRelaService.queryPage(params);

        return RD.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/detail/{id}")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:info")
    public RD info(@PathVariable("id") Integer id){
		NodeModelWorkstationRelaEntity nodeModelWorkstationRela = nodeModelWorkstationRelaService.selectById(id);
		
		List<Integer> workstationIdList = new ArrayList<>(); 
		List<WorkstationEntity> workstationList = new ArrayList<>();
		String workstationName = "";
		
		if(nodeModelWorkstationRela.getModelId() != null){
			ModelEntity model = modelService.selectById(nodeModelWorkstationRela.getModelId());
			nodeModelWorkstationRela.setModelEntity(model);
			nodeModelWorkstationRela.setModelName(model.getName());
		}
		
		if(nodeModelWorkstationRela.getWorkstationIds() != null && nodeModelWorkstationRela.getWorkstationIds().split(",").length>0){
			String[] workstationIds = nodeModelWorkstationRela.getWorkstationIds().split(",");
			for(String wsid : workstationIds){
				workstationIdList.add(Integer.valueOf(wsid));
			}
			if(workstationIdList.size()>0){
				EntityWrapper<WorkstationEntity> workstationWrapper = new EntityWrapper<>();
				workstationWrapper.in("id", workstationIdList);
				workstationList = workstationService.selectList(workstationWrapper);
			}
			if(workstationList.size()>0){
				for(WorkstationEntity workstation : workstationList){
					workstationName = workstation.getName()+"/";
				}
			}
			nodeModelWorkstationRela.setWorkstationIdList(workstationIdList);
			nodeModelWorkstationRela.setWorkstationList(workstationList);
			nodeModelWorkstationRela.setWorkstationName(workstationName);
		}
        return RD.build().put("nodeModelWorkstationRela", nodeModelWorkstationRela);
    }

    /**
     * 保存
     */
    @RequestMapping("/create")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:save")
    public RD save(@RequestBody NodeModelWorkstationRelaEntity nodeModelWorkstationRela){
    	String workstationIds = "";
    	if(nodeModelWorkstationRela != null && nodeModelWorkstationRela.getWorkstationIdList() != null){
    		for(int i=0;i<nodeModelWorkstationRela.getWorkstationIdList().size();i++){
    			if(i==nodeModelWorkstationRela.getWorkstationIdList().size()-1){
    				workstationIds = workstationIds + nodeModelWorkstationRela.getWorkstationIdList().get(i);
    			}else{
    				workstationIds = workstationIds + nodeModelWorkstationRela.getWorkstationIdList().get(i) + ",";
    			}
    		}
    	}
    	nodeModelWorkstationRela.setWorkstationIds(workstationIds);
		nodeModelWorkstationRelaService.insert(nodeModelWorkstationRela);

        return RD.build();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:update")
    public RD update(@RequestBody NodeModelWorkstationRelaEntity nodeModelWorkstationRela){
    	String workstationIds = "";
    	if(nodeModelWorkstationRela != null && nodeModelWorkstationRela.getWorkstationIdList() != null){
    		for(int i=0;i<nodeModelWorkstationRela.getWorkstationIdList().size();i++){
    			if(i==nodeModelWorkstationRela.getWorkstationIdList().size()-1){
    				workstationIds = workstationIds + nodeModelWorkstationRela.getWorkstationIdList().get(i);
    			}else{
    				workstationIds = workstationIds + nodeModelWorkstationRela.getWorkstationIdList().get(i) + ",";
    			}
    		}
    	}
    	nodeModelWorkstationRela.setWorkstationIds(workstationIds);
		nodeModelWorkstationRelaService.updateById(nodeModelWorkstationRela);

        return RD.build();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("masterData:nodemodelworkstationrela:delete")
    public RD delete(@RequestBody Integer[] ids){
		nodeModelWorkstationRelaService.deleteBatchIds(Arrays.asList(ids));

        return RD.build();
    }

}
