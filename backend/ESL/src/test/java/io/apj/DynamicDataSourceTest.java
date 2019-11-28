package io.apj;


import io.apj.modules.masterData.entity.ActionEntity;
import io.apj.modules.masterData.entity.OperationGroupOperationEntity;
import io.apj.modules.masterData.entity.OpertaionGroupEntity;
import io.apj.modules.masterData.service.ActionService;
import io.apj.modules.masterData.service.OpertaionGroupService;
import io.apj.modules.sys.entity.SysUserEntity;
import io.apj.service.DataSourceTestService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {
    @Autowired
    private DataSourceTestService dataSourceTestService;
    @Autowired
    private OpertaionGroupService opertaionGroupService;


    @Test
    public void test(){
        //数据源1
        SysUserEntity user1 = dataSourceTestService.queryUser(1L);
        System.out.println(ToStringBuilder.reflectionToString(user1));

        //数据源2
        SysUserEntity user2 = dataSourceTestService.queryUser2(1L);
        System.out.println(ToStringBuilder.reflectionToString(user2));

        //数据源1
        SysUserEntity user3 = dataSourceTestService.queryUser(1L);
        System.out.println(ToStringBuilder.reflectionToString(user3));
    }
    @Test
    public void aVoid() {
        OpertaionGroupEntity opertaionGroupEntity = new OpertaionGroupEntity();
        opertaionGroupEntity.setCode("We");
        OperationGroupOperationEntity operationGroupOperationEntity = new OperationGroupOperationEntity();
        operationGroupOperationEntity.setOperation("test");
        operationGroupOperationEntity.setFrequency(2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("OpertaionGroupEntity",opertaionGroupEntity);
        map.put("OperationGroupOperationEntity",operationGroupOperationEntity);
        opertaionGroupService.insertOpGroup(map);



    }


}
