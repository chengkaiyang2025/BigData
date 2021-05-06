package com.atguigu;

import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;

public class hdfs {
    private FileSystem fs;
    private Configuration conf = new Configuration();
    @Before
    public void b() throws IOException, InterruptedException {
        // hdfs getconf -confKey fs.defaultFS
        conf.set("fs.defaultFS","hdfs://master.prd.yzf:8020");
        fs = FileSystem.get(URI.create("hdfs://master.prd.yzf:8020/test/"),conf,"root");
        System.out.println("获得hdfs链接");
    }

    @Test
    public void makeDir() throws IOException {
        fs.mkdirs(new Path("/test/hdfstest"));
    }
    @Test
    public void put() throws IOException {
        fs.copyFromLocalFile(new Path("src/main/resources/UserBehavior.csv"),new Path("/test/hdfstest/"));
    }

    @Test
    public void get() throws IOException {
        fs.copyToLocalFile(new Path("/test/hdfstest/UserBehavior.csv"),new Path("/tmp"));
    }

    @Test
    public void rename() throws IOException {
        fs.rename(new Path("/test/abc"),new Path("/test/abcd"));
    }

    @Test
    public void delete() throws IOException {
        boolean delete = fs.deleteOnExit(new Path("/test/hdfstest/UserBehavior.csv"));
        if(delete){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }
    @Test
    public void append(){

    }

    @Test
    public void ls() throws IOException {
        FileStatus[] fileStatuses = fs.listStatus(new Path("/test/hdfstest"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        for(FileStatus fileStatus:fileStatuses){
            if(fileStatus.isFile()){
                System.out.println("以下为文件信息：");
                System.out.println(fileStatus.getPath());
                System.out.println(fileStatus.getLen());
                System.out.println(fileStatus.getBlockSize());
                System.out.println(fileStatus.getOwner());
                System.out.println(fileStatus.getLen());
                System.out.println(sdf.format(fileStatus.getAccessTime()));
                System.out.println(sdf.format(fileStatus.getModificationTime()));
                System.out.println(fileStatus.getReplication());
            }else{
                System.out.println("以下为文件夹信息：");
                System.out.println(fileStatus.getPath());
            }
        }
    }

    @Test
    public void lsRecursive() throws IOException {
        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/apps/data/warehouse/dwd"), true);

        while (files.hasNext()){
            LocatedFileStatus next = files.next();
            System.out.println("=========================");
            System.out.println(next.getPath());

            System.out.println("块信息：");
            BlockLocation[] blockLocations = next.getBlockLocations();

            for (BlockLocation blockLocation : blockLocations) {
                String[] hosts = blockLocation.getHosts();
                System.out.println(blockLocation.getLength());
                System.out.println("块在");
                for (String host : hosts) {
                    System.out.println(host);
                }
            }
        }
    }
    @Test
    public void other() throws IOException {
        FileChecksum fileChecksum = fs.getFileChecksum(new Path("/test/hdfstest/UserBehavior.csv"));
        System.out.println(fileChecksum.toString());
        long defaultBlockSize = fs.getDefaultBlockSize();
        System.out.println(defaultBlockSize);
    }

    @After
    public void close() throws IOException {
        fs.close();
        System.out.println("链接关闭");
    }
}
