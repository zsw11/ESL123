package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 手顺
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
public interface OperationGroupOperationService extends IService<OperationGroupOperationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 软删除实体对象
     * @param operationGroupOperationEntityList
     */
    void deleteList(List<OperationGroupOperationEntity> operationGroupOperationEntityList);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<OperationGroupOperationEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(OperationGroupOperationEntity operationGroupOperationEntity);
}

