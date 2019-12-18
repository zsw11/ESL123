package io.apj.modules.masterData.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import io.apj.common.utils.PageUtils;
import io.apj.modules.masterData.entity.ReportEntity;
import io.apj.modules.masterData.entity.ReportGroupEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 报表
 *
 * @author RoyLuo
 * @email RoyLuo@apjcorp.com
 * @date 2019-11-07 10:39:25
 */
public interface ReportService extends IService<ReportEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer selectByNameTest(String name);

    /**
     *  报表审批详情，符合三个字段所在的报表组里的报表（此方法没用过）
     * @param id
     * @return
     */
    List<ReportEntity> selectApproveList(Integer id);
    /**
     * 报表属于那些报表组，并过滤(以提交的)
     * @param data
     * @return
     */
    List<ReportGroupEntity> selectReportGroup(Map<String,Object> data);

    /**
     * 软删除实体对象List
     * @param reportEntityList
     */
    void deleteList(List<ReportEntity> reportEntityList);

    /**
     * 通过ids软删除
     * @param ids
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据条件软删除
     * @param wrapper
     */
    void deleteByWrapper(Wrapper<ReportEntity> wrapper);

    /**
     * update时更新拼音和日期
     * @return
     */
    void updatePinAndDataById(ReportEntity reportEntity);
}

