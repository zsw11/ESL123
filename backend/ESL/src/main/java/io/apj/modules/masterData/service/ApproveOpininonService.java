package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ApproveOpininonEntity;
import io.apj.modules.report.entity.ApproveEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 常用审批意见
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ApproveOpininonService extends IService<ApproveOpininonEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 软删除实体对象
     * @param ApproveOpininonEntity
     */
    void deleteList(List<ApproveOpininonEntity> ApproveOpininonEntity);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<ApproveOpininonEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(ApproveOpininonEntity approveOpininonEntity);
}

