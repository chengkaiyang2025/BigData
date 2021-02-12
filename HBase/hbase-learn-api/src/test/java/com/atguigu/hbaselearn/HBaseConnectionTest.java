package com.atguigu.hbaselearn;


import org.apache.hadoop.hbase.client.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HBaseConnectionTest {
    private HBaseConnection hBaseConnection;

    @Before
    public void prepare() throws IOException {
        hBaseConnection = new HBaseConnection("slave01.prd.yzf,slave02.prd.yzf,slave03.prd.yzf");

    }
    @Test
    public void list(){
        hBaseConnection.list();
    }
    @Test
    public void isTableExists() throws IOException {
        // 4.2.2 判断表是否存在
        System.out.println("表stu是否存在"+String.valueOf(hBaseConnection.isTableExists("stu")));
    }
    @Test
    public void isTableDisabled() throws IOException {
        // 4.2.2 判断表是否存在
        System.out.println("表stu是否禁用"+String.valueOf(hBaseConnection.isTableDisabled("stu")));
    }
    @Test
    public void createTable() throws IOException {
        // 4.2.3 创建表
        hBaseConnection.createTable("stu", Arrays.asList("info1","info2"));
    }

    @Test
    public void dropTable() {
        hBaseConnection.dropTable("stu3");
        list();
    }


    @Test
    public void putByRowkey() throws IOException {
        for(int i = 1001;i<1100;i++){
            Random random = new Random();
            hBaseConnection.putByRowKey("stu",String.valueOf(i),"info1","name","yangck"+String.valueOf(i));
            hBaseConnection.putByRowKey("stu",String.valueOf(i),"info1","gender", random.nextBoolean()? "male" : "female");
            hBaseConnection.putByRowKey("stu",String.valueOf(i),"info2","tel", String.valueOf(random.nextInt(10)));
        }

    }

    @Test
    public void deleteTableByRowKey(){
        List<String> rowKeys = new ArrayList<String>();
        for(int i = 1001;i<1098;i++){
            rowKeys.add(String.valueOf(i));
        }
        hBaseConnection.deleteTableByRowKey("stu",rowKeys);
        scanAll();
    }
    @Test
    public void scanAll() {
        hBaseConnection.scanAll("stu");
    }

    @Test
    public void getByRowkey() {
        hBaseConnection.getByRowkey("stu","1001");
    }
    @Test
    public void getByRowkey2() {
        hBaseConnection.getByRowkey("stu","1001","info1","name");
    }
    @After
    public void close() throws IOException {
        hBaseConnection.close();
    }
}