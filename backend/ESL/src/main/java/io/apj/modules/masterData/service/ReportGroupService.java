package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;
import io.apj.modules.masterData.entity.ReportGroupReportRelaEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 报表组
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ReportGroupService extends IService<ReportGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ReportGroupReportRelaEntity> reportGroupRelaList(Integer id);

    /**
     * 软删除实体对象
     * @param reportGroupEntityList
     */
    void deleteList(List<ReportGroupEntity> reportGroupEntityList);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<ReportGroupEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(ReportGroupEntity reportGroupEntity);
}

