package com.yzf.di.kuducdc.kudutest.util;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Random;

/**
 * 随机生成章鱼报表的数据，模拟kafka中的数据。
 * 压力测试用。
 */
public class OctopusDataGenerator {
    private static ObjectMapper mapper = new ObjectMapper();
    /**随机knjd、kjqj、qyid、qy名称、id、生成cell
     * @return
     */
    public static ObjectNode randomGenerateOctopusData(){
        Random random = new Random();

        Long qyid = Math.abs(random.nextLong());
        String qymc = "测试"+qyid;
        int kjnd = 2021;
        int kjqj = random.nextInt(12 - 1 + 1) + 1;
        int cell_l = random.nextInt(100 - 70 + 1) + 70;
        String id = String.format("%s_%s_%s_778944412830113793_778944412834308097_sheet1", qyid, kjnd, kjqj);

        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode childNode1 = mapper.createObjectNode();

        childNode1.put("areaId", "0");
        childNode1.put("areaName", "全国");
        childNode1.put("boxId", "778944412830113793");

        ArrayNode cell = mapper.createArrayNode();
        for (int i = 0; i < cell_l; i++) {
            ObjectNode cell_0 = mapper.createObjectNode();
            cell_0.put("isChange","NOCHANGE");
            cell_0.put("location","A"+i);
            cell_0.put("value","v"+i);
            cell.add(cell_0);
        }


        childNode1.set("cells", cell);

        childNode1.put("createTime", "2021-05-132 15:22:58");
        childNode1.put("dzQyId", "722839913816526849");
        childNode1.put("dzQyName", "测试");
        childNode1.put("fetchDataTime", 1620804122206L);
        childNode1.put("id", id);
        childNode1.put("kjnd", kjnd);
        childNode1.put("kjqj", kjqj);
        childNode1.put("nsqxdm", "4");
        childNode1.put("parentBoxId", "778941549412327424");
        childNode1.put("qyId", qyid);
        childNode1.put("qyName", qymc);
        childNode1.put("sbszId", "3702040002");
        childNode1.put("sheetName", "sheet1");
        childNode1.put("systemId", "498812948580401153");

        rootNode.put("key", "1");
        rootNode.set("value", childNode1);
        return rootNode;
    }
}
