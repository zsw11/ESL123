package io.apj.modules.masterData.service.impl;

import cn.hutool.core.util.PinyinUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import io.apj.common.utils.PageUtils;
import io.apj.common.utils.Query;
import io.apj.modules.masterData.dao.PartDao;
import io.apj.modules.masterData.entity.ModelEntity;
import io.apj.modules.masterData.entity.ModelPartRelaEntity;
import io.apj.modules.masterData.entity.PartEntity;
import io.apj.modules.masterData.service.ModelPartRelaService;
import io.apj.modules.masterData.service.ModelSeriesService;
import io.apj.modules.masterData.service.ModelService;
import io.apj.modules.masterData.service.PartService;
import io.apj.modules.masterData.vo.ModelVo;
import io.apj.modules.sys.service.SysDeptService;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service("partService")
public class PartServiceImpl extends ServiceImpl<PartDao, PartEntity> implements PartService {
    @Autowired
    private ModelPartRelaService modelPartRelaService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelSeriesService modelSeriesService;
    @Autowired
    private SysDeptService deptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<PartEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.isNull("delete_at").orderBy("update_at",false);
        if (params.get("name") != null && params.get("name") != "") {
            params.put("name", ((String) params.get("name")).replace('*', '%'));
            entityWrapper.andNew(
                    "pinyin like '%" + params.get("name") + "%' " + "or name like '%" + params.get("name") + "%'");
        }
        Page<PartEntity> page = this.selectPage(new Query<PartEntity>(params).getPage(), entityWrapper);

        return new PageUtils(page);
    }

    @Override
    public PageUtils partModeRelaList(Integer id, Map<String, Object> params) {
        Page<Map<String,Object>> page  = new Page<>(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("limit").toString()));
        String modelName = (String) params.get("name");
        String code = (String) params.get("code");
        int modelSeriesId = 0;
        int deptId = 0;
        if((String) params.get("modelSeriesId")!=null && (String) params.get("modelSeriesId")!=""){
             modelSeriesId = Integer.parseInt((String) params.get("modelSeriesId"));
        }
        if((String) params.get("deptId")!=null && (String) params.get("deptId")!=""){
            deptId = Integer.parseInt((String) params.get("deptId"));
        }
        return new PageUtils(page.setRecords(this.baseMapper.selectpartModel(id, page, modelName, deptId, modelSeriesId,code)));

    }

    @Override
    public void deleteList(List<PartEntity> partList) {
        for(PartEntity item : partList){
            item.setDeleteAt(new Date());
        }
        this.updateAllColumnBatchById(partList);
    }

    @Override
    public void deleteByIds(Collection<? extends Serializable> ids) {
        List<PartEntity> partEntityList = this.selectBatchIds(ids);
        for(PartEntity item : partEntityList){
            item.setDeleteAt(new Date());
        }
        this.updateAllColumnBatchById(partEntityList);
    }

    @Override
    public void deleteByWrapper(Wrapper<PartEntity> wrapper) {
        List<PartEntity> partEntityList = this.selectList(wrapper);
        for(PartEntity item: partEntityList){
            item.setDeleteAt(new Date());
        }
        this.updateAllColumnBatchById(partEntityList);
    }

    @Override
    public void updatePinAndDataById(PartEntity part) {
        part.setPinyin(PinyinUtil.getPinYin(part.getName()));
        part.setUpdateAt(new Date());
        this.updateById(part);
    }

}