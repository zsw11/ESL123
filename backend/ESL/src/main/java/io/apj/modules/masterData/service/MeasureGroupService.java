package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.MeasureGroupEntity;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 常用指标组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
public interface MeasureGroupService extends IService<MeasureGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 导入
     * @return
     */
    ResponseEntity<Object> measureGroupImport(Map<String,Object> map);

    /**
     * 软删除实体对象
     * @param measureGroupEntityList
     */
    void deleteList(List<MeasureGroupEntity> measureGroupEntityList);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<MeasureGroupEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(MeasureGroupEntity measureGroupEntity);
}

