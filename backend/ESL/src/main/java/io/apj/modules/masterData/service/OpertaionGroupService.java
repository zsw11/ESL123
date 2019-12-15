package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import org.springframework.http.ResponseEntity;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 手顺组合
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-11 11:18:15
 */
public interface OpertaionGroupService extends IService<OpertaionGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ResponseEntity<Object> insertOpGroup(Map<String, Object> map,OpertaionGroupEntity opertaionGroup) throws InvocationTargetException, IllegalAccessException, IntrospectionException;

    ResponseEntity<Object> UpdataOpertaionGroup(Map<String, Object> map) throws IntrospectionException, InvocationTargetException, IllegalAccessException;

    /**
     * 软删除实体对象
     * @param opertaionGroupEntityList
     */
    void deleteList(List<OpertaionGroupEntity> opertaionGroupEntityList);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<OpertaionGroupEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(OpertaionGroupEntity opertaionGroupEntity);
}


