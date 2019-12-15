package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.WorkstationTypeEntity;
import io.apj.modules.masterData.entity.WorkstationTypeNodeEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 工位类型
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface WorkstationTypeService extends IService<WorkstationTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<WorkstationTypeEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(WorkstationTypeEntity workstationTypeEntity);
}

