package com.atguigu.hbaselearn;

import javafx.scene.control.Tab;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：HBase 的DDL create/drop DML get/put/scan/delete相关API
 * @date ：2021/1/12 上午11:40
 */


public class HBaseConnection {
    private String rootDir;
    private String zkServer;
    private String port;
    private Configuration conf;
    private Connection connection;
    private HBaseAdmin hBaseAdmin;
    public HBaseConnection(String zkServer) throws IOException {
        this.rootDir = rootDir;
        this.zkServer = zkServer;
        this.port = port;

        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", zkServer);
        hBaseAdmin = (HBaseAdmin) ConnectionFactory.createConnection(conf).getAdmin();
    }
    public void list(){
        System.out.println("-------------------------------------------------------------");
        try {
            for(TableName tableName:hBaseAdmin.listTableNames()){
                System.out.println(tableName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------------------------------");

    }
    /** // 4.2.2 判断表是否存在
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean isTableExists(String tableName) {
        try {
            return hBaseAdmin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isTableDisabled(String tableName) {
        try {
            return hBaseAdmin.isTableDisabled(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4.2.3 创建表
    public void createTable(String tableName, List<String> cfs) throws IOException {
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        if (cfs.size()>0){
            for(String cf : cfs){
                HColumnDescriptor colDesc = new HColumnDescriptor(cf);
                colDesc.setMaxVersions(3);
                tableDescriptor.addFamily(colDesc);
            }
        }
        try{
            hBaseAdmin.createTable(tableDescriptor);
            System.out.println(tableName+"创建成功");
        }catch (TableExistsException e){
            System.err.println(tableName+"已存在");
        }
    }
    // 4.2.4 删除表
    public void dropTable(String tableName){
        if(isTableExists(tableName)){
            try {
                hBaseAdmin.disableTable(TableName.valueOf(tableName));
                hBaseAdmin.deleteTable(TableName.valueOf(tableName));
                System.out.println(tableName+"删除成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
        }

    }
    // 4.2.5 向表中插入数据
    public void putByRowKey(String tableName,String rowKey, String cf, String cq,String value) throws IOException {
        if(isTableExists(tableName)){
            Table table = hBaseAdmin.getConnection().getTable(TableName.valueOf(tableName));
            Put p = new Put(Bytes.toBytes(rowKey));
            p.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cq),Bytes.toBytes(value));
            System.out.printf("表 %s 列族 %s 列%s，插入 %s 成功",tableName,cf,cq,value);
            table.put(p);
        }else {
            System.out.println(tableName+"不存在");
        }
    }
    // 4.2.6 删除多行数据
    public void deleteTableByRowKey(String tableName,List<String> rowKeys){
        try {
            Table table = hBaseAdmin.getConnection().getTable(TableName.valueOf(tableName));
            List<Delete> deleteList = new ArrayList<Delete>();
            for(String rowKey:rowKeys){
                Delete delete = new Delete(Bytes.toBytes(rowKey));
                deleteList.add(delete);
            }
            table.delete(deleteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 4.2.7 获取所有数据
    public void scanAll(String tableName){
        try {
            Table table = hBaseAdmin.getConnection().getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            ResultScanner resultScanner = table.getScanner(scan);
            System.out.println("表 "+tableName+"的数据为：");
            for(Result result:resultScanner){
                for(Cell cell:result.rawCells()){
                    System.out.printf("%s 行的列 %s:%s 值为: %s\n"
                    ,Bytes.toString(CellUtil.cloneRow(cell))
                    ,Bytes.toString(CellUtil.cloneFamily(cell))
                    ,Bytes.toString(CellUtil.cloneQualifier(cell))
                    ,Bytes.toString(CellUtil.cloneValue(cell))
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // 4.2.8 获取某一行数据
    public void getByRowkey(String tableName,String rowKey){
        try {
            Table table = hBaseAdmin.getConnection().getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            System.out.printf("表 %s 第 %s 行的数据如下：\n",tableName,rowKey);
            for(Cell cell:result.rawCells()){
                System.out.printf("列族：%s,列：%s 的值为 %s\n"
                        ,Bytes.toString(CellUtil.cloneFamily(cell))
                        ,Bytes.toString(CellUtil.cloneQualifier(cell))
                        ,Bytes.toString(CellUtil.cloneValue(cell))
                        );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 4.2.9 获取某一行指定“列族：列”的数据
    public void getByRowkey(String tableName, String rowKey,String cf,String cq){
        try{
            Table table = hBaseAdmin.getConnection().getTable(TableName.valueOf(tableName));

            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cq));
            Result result = table.get(get);
            System.out.printf("表 %s 第 %s 行的列%s:%s数据如下：\n",tableName,rowKey,cf,cq);
            for(Cell cell:result.rawCells()){
                System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close() throws IOException {
        if(hBaseAdmin != null){
            hBaseAdmin.close();
        }
    }
}
