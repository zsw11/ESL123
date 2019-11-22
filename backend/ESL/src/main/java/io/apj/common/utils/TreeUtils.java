package io.apj.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;

import java.util.List;

public class TreeUtils {

    /**
     * 菜单树
     *
     * @param parentId
     * @param workstationTypeNodeEntityList
     * @param array
     */
    public static void setNodeTypeTree(Integer parentId, List<WorkstationTypeNodeEntity> workstationTypeNodeEntityList, JSONArray array) {
        for (WorkstationTypeNodeEntity nodeEntity : workstationTypeNodeEntityList) {
            if (parentId.equals(nodeEntity.getParentId())) {
                String string = JSONObject.toJSONString(nodeEntity);
                JSONObject parent = (JSONObject) JSONObject.parse(string);
                array.add(parent);
                if (workstationTypeNodeEntityList.stream().filter(p -> p.getParentId().equals(nodeEntity.getWorkstationId())).findAny() != null) {
                    JSONArray child = new JSONArray();
                    parent.put("child", child);
                    setNodeTypeTree(nodeEntity.getWorkstationId(), workstationTypeNodeEntityList, child);
                }
            }
        }
    }
}