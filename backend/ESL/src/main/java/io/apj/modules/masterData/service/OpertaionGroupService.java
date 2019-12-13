package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.RD;
import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import org.springframework.http.ResponseEntity;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
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
}

