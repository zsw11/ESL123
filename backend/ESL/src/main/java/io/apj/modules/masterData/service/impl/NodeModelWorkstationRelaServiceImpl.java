package io.apj.modules.masterData.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.NodeModelWorkstationRelaDao;
import io.apj.modules.masterData.entity.NodeModelWorkstationRelaEntity;
import io.apj.modules.masterData.service.NodeModelWorkstationRelaService;


@Service("nodeModelWorkstationRelaService")
public class NodeModelWorkstationRelaServiceImpl extends ServiceImpl<NodeModelWorkstationRelaDao, NodeModelWorkstationRelaEntity> implements NodeModelWorkstationRelaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	EntityWrapper<NodeModelWorkstationRelaEntity> entityWrapper = new EntityWrapper<>();
    	entityWrapper.isNull("delete_at").eq(StringUtils.isNotEmpty((CharSequence) params.get("workstationTypeNode")), "workstation_type_node_id", params);
    	
        Page<NodeModelWorkstationRelaEntity> page = this.selectPage(
                new Query<NodeModelWorkstationRelaEntity>(params).getPage()
        );

        return new PageUtils(page);
    }

}