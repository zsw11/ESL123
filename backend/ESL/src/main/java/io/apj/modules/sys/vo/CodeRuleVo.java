package io.apj.modules.sys.vo;

import io.apj.modules.sys.entity.CodeRuleItemEntity;

import java.util.List;

public class CodeRuleVo {

    private Long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 当前编号键
     */
    private String currentSerialKey;
    /**
     * 当前编号
     */
    private Integer currentSerial;
    /**
     * 备注
     */
    private String remark;

    private List<CodeRuleItemEntity> codeRuleItems;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentSerialKey() {
        return currentSerialKey;
    }

    public void setCurrentSerialKey(String currentSerialKey) {
        this.currentSerialKey = currentSerialKey;
    }

    public Integer getCurrentSerial() {
        return currentSerial;
    }

    public void setCurrentSerial(Integer currentSerial) {
        this.currentSerial = currentSerial;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CodeRuleItemEntity> getCodeRuleItems() {
        return codeRuleItems;
    }

    public void setCodeRuleItems(List<CodeRuleItemEntity> codeRuleItems) {
        this.codeRuleItems = codeRuleItems;
    }
}
