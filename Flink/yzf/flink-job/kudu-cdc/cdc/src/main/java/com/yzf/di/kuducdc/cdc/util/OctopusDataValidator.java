package com.yzf.di.kuducdc.cdc.util;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 对章鱼报表的数据进行格式校验
 */
public class OctopusDataValidator {
    /**
     * @param data 从kafka接受的json
     * @return 返回完整kv的json
     */
    public static ObjectNode JsonSchemaValidator(ObjectNode data){
        if (data.get("id") == null)  data.put("id","NULL");
//        if (data.get("cell_location") == null)  data.put("cell_location","NULL");
//        if (data.get("cell_is_change") == null)  data.put("cell_is_change","NULL");
//        if (data.get("cell_value") == null)  data.put("cell_value","NULL");
        if (data.get("areaId") == null)  data.put("areaId","NULL");
        if (data.get("areaName") == null)  data.put("areaName","NULL");
        if (data.get("boxId") == null)  data.put("boxId","NULL");
        if (data.get("createTime") == null)  data.put("createTime","NULL");
        if (data.get("dzQyId") == null)  data.put("dzQyId","NULL");
        if (data.get("dzQyName") == null)  data.put("dzQyName","NULL");
        if (data.get("fetchDataTime") == null)  data.put("fetchDataTime","NULL");
        if (data.get("kjnd") == null)  data.put("kjnd","NULL");
        if (data.get("kjqj") == null)  data.put("kjqj","NULL");
        if (data.get("nsqxdm") == null)  data.put("nsqxdm","NULL");
        if (data.get("parentBoxId") == null)  data.put("parentBoxId","NULL");
        if (data.get("qyId") == null)  data.put("qyId","NULL");
        if (data.get("qyName") == null)  data.put("qyName","NULL");
        if (data.get("sbszId") == null)  data.put("sbszId","NULL");
        if (data.get("sheetName") == null)  data.put("sheetName","NULL");
        if (data.get("systemId") == null)  data.put("systemId","NULL");
        return data;
    }

    /** 返回202103、202111这样标准的kjnd_kjqj
     * @param kjnd
     * @param kjqj
     * @return
     */
    public static String DateValidator(String kjnd, String kjqj){
        if(kjqj.length() == 1){
            return kjnd + "0" + kjqj;
        }else return kjnd + kjqj;
    }
}
