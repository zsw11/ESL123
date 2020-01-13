package io.apj.modules.masterData.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.google.gson.JsonArray;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 工位类型节点
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface WorkstationTypeNodeService extends IService<WorkstationTypeNodeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ResponseEntity<JSONArray> listAllNodeType(Integer id);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Integer[] ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<WorkstationTypeNodeEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(WorkstationTypeNodeEntity workstationTypeNodeEntity);
}

