package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.ModelEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 机种
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ModelService extends IService<ModelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils selectBySeriesId(int id, Map<String, Object> params);

    PageUtils modelPartRelaList(int id, Map<String, Object> params);

    PageUtils modelToolRelaList(int id, Map<String, Object> params);

    /**
     * 软删除实体对象
     * @param modelEntityList
     */
    void deleteList(List<ModelEntity> modelEntityList);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<ModelEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(ModelEntity modelEntity);
}

