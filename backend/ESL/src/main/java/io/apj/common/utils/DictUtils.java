package io.apj.common.utils;

import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * 字典工具类
 */
public class DictUtils {

    /**
     * 根据传入字段判断字典项，返回字段项数据
     */
    public String dictFilter(String tName, String name, String dictCode, HashMap<String, String> dictMap, JsonObject areaDataFilter) {

        if(dictCode==null&&dictCode==""){
            return dictCode;
        }

        //过滤客户信息表
        if (tName.equals("CustomerEntity")) {
            return customerFilter(name, dictCode, dictMap, areaDataFilter);
        }

        //过滤设备信息表
        if (tName.equals("DeviceEntity")) {
            return deviceFilter(name, dictCode, dictMap);
        }

        //过滤岗位信息表
        if (tName.equals("JobEntity")) {
            //工程师等级
            if (name.equals("grade")) {
                return getDictData("EngineerGrade", dictCode, dictMap);
            }
        }

        //过滤人员信息表
        if (tName.equals("MemberEntity")) {
            return memberFilter(name, dictCode, dictMap, areaDataFilter);
        }

        //过滤物料信息表
        if (tName.equals("SparePartEntity")) {
            //关键件
            if (name.equals("ifKeyModule")) {
                return dictCode.equals("true") ? "是" : "否";
            }
            //是否启用限制
            if (name.equals("ifLimit")) {
                return dictCode.equals("true") ? "是" : "否";
            }
            //是否启用序列号
            if (name.equals("ifUseSerialNumber")) {
                return dictCode.equals("true") ? "是" : "否";
            }
            //是否启用批号
            if (name.equals("ifUseBatchNumber")) {
                return dictCode.equals("true") ? "是" : "否";
            }
        }

        //过滤仓库信息
        if (tName.equals("WarehouseEntity")) {
            return warehouseFilter(name,dictCode,dictMap,areaDataFilter);
        }

        /**
         * 库存模块
         */
        //入库单
        if (tName.equals("InEntity")){
            return stockInFilter(name,dictCode,dictMap);
        }
        //出库单
        if(tName.equals("OutEntity")){
            return stockOutFilter(name,dictCode,dictMap);
        }
        //盘点单
        if (tName.equals("InventorySheetEntity")){
            return inventorySheetFilter(name,dictCode,dictMap);
        }
        //调拨单
        if (tName.equals("TransferEntity")){
            return transferFilter(name,dictCode,dictMap);
        }
        //物料退回单
        if (tName.equals("SparePartReturnEntity")){
            return sparePartReturnFilter(name,dictCode,dictMap);
        }
        //发料单
        if (tName.equals("SparePartTransferEntity")){
            return sparePartTransferFilter(name,dictCode);
        }

        return dictCode;
    }


    public String getDictData(String name, String code, HashMap<String, String> dictMap) {
        StringBuilder sb = new StringBuilder();
        StringBuilder keySb = sb.append(name).append("_").append(code);
        String key = keySb.toString();
        String value = dictMap.get(key);
        return value;
    }

    //过滤客户基本信息表
    private String customerFilter(String name, String dictCode, HashMap<String, String> dictMap, JsonObject areaData) {
        //省份过滤
        if (name.equals("province")) {
            return areaData.get("86").getAsJsonObject().get(dictCode).getAsString();

        }
        //城市编号
        if (name.equals("city")) {
            return areaData.get(dictCode.substring(0, 2) + "0000").getAsJsonObject().get(dictCode).getAsString();
        }
        //客户等级
        if (name.equals("grade")) {
            return getDictData("VIPLevel", dictCode, dictMap);
        }
        return dictCode;
    }


    //过滤设备信息表
    private String deviceFilter(String name, String dictCode, HashMap<String, String> dictMap) {
        //产品类型
        if (name.equals("productType")) {
            return getDictData("DeviceType", dictCode, dictMap);
        }
        //所属公司
        if (name.equals("company")) {
            return getDictData("BelongsToCompany", dictCode, dictMap);
        }
        //生产单位
        if (name.equals("manufacturer")) {
            return getDictData("Manufacturer", dictCode, dictMap);
        }
        //销售单位
        if (name.equals("saleDept")) {
            return getDictData("SalesUnit", dictCode, dictMap);
        }
        return dictCode;
    }


    //过滤人员信息表
    private String memberFilter(String name, String dictCode, HashMap<String, String> dictMap, JsonObject areaData) {
        //性别
        if (name.equals("gender")) {
            return getDictData("Gender", dictCode, dictMap);
        }
        //家庭住址省份
        if (name.equals("familyProvince")) {
            return areaData.get("86").getAsJsonObject().get(dictCode).getAsString();
        }
        //家庭住址市
        if (name.equals("familyCity")) {
            return areaData.get(dictCode.substring(0, 2) + "0000").getAsJsonObject().get(dictCode).getAsString();
        }
        //在职状态
        if (name.equals("status")) {
            return getDictData("WorkingStatus", dictCode, dictMap);
        }
        //工作地址省份
        if (name.equals("bussinessProvince")) {
            return areaData.get("86").getAsJsonObject().get(dictCode).getAsString();
        }
        //工作住址市
        if (name.equals("bussinessCity")) {
            return areaData.get(dictCode.substring(0, 2) + "0000").getAsJsonObject().get(dictCode).getAsString();
        }
        return dictCode;
    }

    //过滤仓库
    private String warehouseFilter(String name, String dictCode, HashMap<String, String> dictMap, JsonObject areaData) {
        //仓位
        if (name.equals("stockPositionCode")) {
            return getDictData("StockPosition", dictCode, dictMap);
        }
        //省份过滤
        if (name.equals("province")) {
            return areaData.get("86").getAsJsonObject().get(dictCode).getAsString();

        }
        //城市编号
        if (name.equals("city")) {
            return areaData.get(dictCode.substring(0, 2) + "0000").getAsJsonObject().get(dictCode).getAsString();
        }
        //是否启用
        if (name.equals("ifUse")) {
            return dictCode.equals("true") ? "是" : "否";
        }
        return dictCode;
    }

    //入库单过滤
    private String stockInFilter(String name, String dictCode, HashMap<String, String> dictMap) {

        //入库类型
        if (name.equals("stockInType")){
            return getDictData("StockInType", dictCode, dictMap);
        }
        //仓位
        if (name.equals("stockPositionCode")) {
            return getDictData("StockPosition", dictCode, dictMap);
        }
        //状态
        if (name.equals("status")){
            if ("freedom".equals(dictCode)){
                return "自由";
            }
            if ("signed".equals(dictCode)){
                return "签字";
            }
            if ("submitted".equals(dictCode)){
                return "提交";
            }
            if ("approved".equals(dictCode)){
                return "审批";
            }
        }
        return dictCode;
    }

    //出库单过滤
    private String stockOutFilter(String name, String dictCode, HashMap<String, String> dictMap) {

        //出库类型
        if (name.equals("stockOutType")){
            return getDictData("StockOutType", dictCode, dictMap);
        }
        //仓位
        if (name.equals("stockPositionCode")) {
            return getDictData("StockPosition", dictCode, dictMap);
        }
        //状态
        if (name.equals("status")){
            if ("freedom".equals(dictCode)){
                return "自由";
            }
            if ("signed".equals(dictCode)){
                return "签字";
            }
            if ("submitted".equals(dictCode)){
                return "提交";
            }
            if ("approved".equals(dictCode)){
                return "审批";
            }
        }
        return dictCode;
    }


    //盘点单过滤
    private String inventorySheetFilter(String name, String dictCode, HashMap<String, String> dictMap) {
        //仓位
        if (name.equals("stockPositionCode")) {
            return getDictData("StockPosition", dictCode, dictMap);
        }
        //盘点类型
        if (name.equals("checkType")){
            return getDictData("InventorySheetType", dictCode, dictMap);
        }
        //状态
        if (name.equals("status")){
            if ("freedom".equals(dictCode)){
                return "自由";
            }
            if ("signed".equals(dictCode)){
                return "签字";
            }
            if ("submitted".equals(dictCode)){
                return "提交";
            }
            if ("approved".equals(dictCode)){
                return "审批";
            }
        }
        return dictCode;
    }


    //调拨单过滤
    private String transferFilter(String name, String dictCode, HashMap<String, String> dictMap) {
        //调拨类型
        if (name.equals("stockTransferType")){
            return getDictData("RequisitionType", dictCode, dictMap);
        }
        //状态
        if (name.equals("status")){
            if ("freedom".equals(dictCode)){
                return "自由";
            }
            if ("signed".equals(dictCode)){
                return "签字";
            }
            if ("submitted".equals(dictCode)){
                return "提交";
            }
            if ("approved".equals(dictCode)){
                return "审批";
            }
        }
        return dictCode;
    }


    //物料退回单过滤
    private String sparePartReturnFilter(String name, String dictCode, HashMap<String, String> dictMap) {
        //退回类型
        if (name.equals("returnType")){
            return getDictData("SpareParkBackType", dictCode, dictMap);
        }
        //状态
        if (name.equals("status")){
            if ("freedom".equals(dictCode)){
                return "自由";
            }
            if ("signed".equals(dictCode)){
                return "签字";
            }
            if ("submitted".equals(dictCode)){
                return "提交";
            }
            if ("approved".equals(dictCode)){
                return "审批";
            }
        }
        return dictCode;
    }

    //发料单过滤
    private String sparePartTransferFilter(String name, String dictCode) {
        //状态
        if (name.equals("status")){
            if ("freedom".equals(dictCode)){
                return "自由";
            }
            if ("signed".equals(dictCode)){
                return "签字";
            }
            if ("submitted".equals(dictCode)){
                return "提交";
            }
            if ("approved".equals(dictCode)){
                return "审批";
            }
        }
        return dictCode;
    }
}
