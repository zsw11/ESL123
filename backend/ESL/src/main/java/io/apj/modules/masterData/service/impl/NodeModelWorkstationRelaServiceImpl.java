package io.apj.modules.masterData.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.NodeModelWorkstationRelaDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.NodeModelWorkstationRelaEntity;
import io.apj.modules.masterData.entity.WorkstationEntity;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.NodeModelWorkstationRelaService;
import io.apj.modules.masterData.service.WorkstationService;


@Service("nodeModelWorkstationRelaService")
public class NodeModelWorkstationRelaServiceImpl extends ServiceImpl<NodeModelWorkstationRelaDao, NodeModelWorkstationRelaEntity> implements NodeModelWorkstationRelaService {

	@Autowired
	private ModelService modelService;
	@Autowired
	private WorkstationService workstationService;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	EntityWrapper<NodeModelWorkstationRelaEntity> entityWrapper = new EntityWrapper<>();
    	entityWrapper.isNull("delete_at").eq(StringUtils.isNotEmpty((CharSequence) params.get("workstationTypeNodeId")), "workstation_type_node_id", Integer.valueOf(params.get("workstationTypeNodeId").toString()));
    	
        Page<NodeModelWorkstationRelaEntity> page = this.selectPage(
                new Query<NodeModelWorkstationRelaEntity>(params).getPage(),entityWrapper.orderBy("create_at")
        );

        if(page.getRecords() != null && page.getRecords().size()>0 ){
        	for(NodeModelWorkstationRelaEntity entity: page.getRecords()){
        		List<Integer> workstationIdList = new ArrayList<>(); 
        		List<WorkstationEntity> workstationList = new ArrayList<>();
        		String workstationName = "";
        		
        		if(entity.getModelId() != null){
        			ModelEntity model = modelService.selectById(entity.getModelId());
        			entity.setModelEntity(model);
        			entity.setModelName(model.getName());
        		}
        		
        		if(entity.getWorkstationIds() != null && entity.getWorkstationIds().split(",").length>0){
        			String[] workstationIds = entity.getWorkstationIds().split(",");
        			for(String id : workstationIds){
        				workstationIdList.add(Integer.valueOf(id));
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
        			entity.setWorkstationIdList(workstationIdList);
        			entity.setWorkstationList(workstationList);
        			entity.setWorkstationName(workstationName);
        		}
        	}
        }
        
        return new PageUtils(page);
    }

}