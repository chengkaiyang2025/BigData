package com.yzf.di.kuducdc.cdc.stream;


import com.yzf.di.kuducdc.cdc.util.OctopusDataValidator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

/**
 * https://stackoverflow.com/questions/40967921/create-json-object-using-jackson-in-java
 */
public class OctopusDataValidatorTest {
    //    private final ObjectMapper mapper = new ObjectMapper();
    private ObjectNode rootNode;

    @Test
    public void make() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode childNode1 = mapper.createObjectNode();
        childNode1.put("name1", "val1");
        childNode1.put("name2", "val2");

        rootNode.set("obj1", childNode1);

        ObjectNode childNode2 = mapper.createObjectNode();
        childNode2.put("name3", "val3");
        childNode2.put("name4", "val4");

        rootNode.set("obj2", childNode2);

        ObjectNode childNode3 = mapper.createObjectNode();
        childNode3.put("name5", "val5");
        childNode3.put("name6", "val6");

        rootNode.set("obj3", childNode3);
        System.out.println(rootNode);

    }

    @Before
    public void before() {
        ObjectMapper mapper = new ObjectMapper();

        rootNode = mapper.createObjectNode();

        ObjectNode childNode1 = mapper.createObjectNode();

        childNode1.put("areaId", "0");
        childNode1.put("areaName", "全国");
        childNode1.put("boxId", "778944412830113793");

        ArrayNode cell = mapper.createArrayNode();
        ObjectNode cell_0 = mapper.createObjectNode();
        cell_0.put("isChange","NOCHANGE");
        cell_0.put("location","A1");
        cell_0.put("value","1");
        cell.add(cell_0);

        ObjectNode cell_1 = mapper.createObjectNode();
        cell_1.put("isChange","NOCHANGE");
        cell_1.put("location","A2");
        cell_1.put("value","1");
        cell.add(cell_1);

        ObjectNode cell_2 = mapper.createObjectNode();
        cell_2.put("isChange","NOCHANGE");
        cell_2.put("location","A4");
        cell_2.put("value","Y");
        cell.add(cell_2);

        ObjectNode cell_3 = mapper.createObjectNode();
        cell_3.put("isChange","NOCHANGE");
        cell_3.put("location","B1");
        cell_3.put("value","0");
        cell.add(cell_3);

        childNode1.set("cells", cell);

        childNode1.put("createTime", "2021-05-132 15:22:58");
        childNode1.put("dzQyId", "722839913816526849");
        childNode1.put("dzQyName", "测试");
        childNode1.put("fetchDataTime", 1620804122206L);
        childNode1.put("id", "840244025863835648_2021_4_778944412830113793_778944412834308097_sheet1");
        childNode1.put("kjnd", "2021");
        childNode1.put("kjqj", "4");
        childNode1.put("nsqxdm", "4");
        childNode1.put("parentBoxId", "778941549412327424");
        childNode1.put("qyId", "840244025863835648");
        childNode1.put("qyName", "青岛小企业年报测试");
        childNode1.put("sbszId", "3702040002");
        childNode1.put("sheetName", "sheet1");
        childNode1.put("systemId", "498812948580401153");

        rootNode.put("key", "1");
        rootNode.set("value", childNode1);
    }

    /**
     * 完全正确的json
     */
    @Test
    public void jsonSchemaValidator_rightJson() {

//        System.out.println(rootNode);
        System.out.println(OctopusDataValidator.JsonSchemaValidator(rootNode));
    }

    /**
     * 少一个key，非主键的json
     */
    @Test
    public void jsonSchemaValidator_missKeyJson() {


    }

    /**
     * 少一个key，主键的json
     */
    @Test
    public void jsonSchemaValidator_missRowKeyJson() {

    }

    /**
     * value中为Null，或数据类型不一致
     */
    @Test
    public void jsonSchemaValidator_wrongValueJson() {

    }

    @Test
    public void jsonSchemaValidator_OtherJson() {
        rootNode.put("sdf", "2");
        System.out.println(OctopusDataValidator.JsonSchemaValidator(rootNode));
    }
}
