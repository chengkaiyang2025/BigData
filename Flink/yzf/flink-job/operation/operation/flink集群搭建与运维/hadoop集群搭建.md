使用flink

```shell
## 获取hadoop的安装路径
pwd
## 打开/etc/profile.d/my_env.sh文件
# HADOOP_HOME
export HADOOP_HOME=/opt/hadoop-3.1.3
export HADOOP_MAPRED_HOME=/opt/hadoop-3.1.3
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin

export HADOOP_CLASSPATH=$(hadoop classpath)
export FLINK_HOME=/opt/flink-1.12.4/

## 让文件生效
source /etc/profile
## 验证是否安装成功
hadoop version
## 重启
reboot

```


|      | flink1         | flink2                   | flink3                  |
| ---- | ----------------- | --------------------------- | -------------------------- |
| HDFS | NameNode,DataNode | DataNode                    | SecondaryNameNode,DataNode |
| YARN | NodeManager       | ResourceManager,NodeManager | NodeManager                |
4.3 配置集群

(1) 核心配置文件 core-site.xml

```shell
cd $HADOOP_HOME/etc/hadoop
vim core-site.xml	
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <!-- 指定 NameNode 的地址 -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://flink1:8020</value>
    </property>
    <!-- 指定 hadoop 数据的存储目录 -->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/data/hadoop/data</value>
    </property>
    <!-- 配置 HDFS 网页登录使用的静态用户为 atguigu -->
    <property>
        <name>hadoop.http.staticuser.user</name>
        <value>hadoop</value>
    </property>
</configuration>
```



(2) HDFS配置文件 hdfs-site.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <!-- nn web 端访问地址-->
    <property>
        <name>dfs.namenode.http-address</name>
        <value>flink1:9870</value>
    </property>
    <!-- 2nn web 端访问地址-->
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>flink3:9868</value>
    </property>
</configuration>
```



(3) YARN配置文件 yarn-site.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <!-- 指定 MR 走 shuffle -->
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <!-- 指定 ResourceManager 的地址-->
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>flink2</value>
    </property>
    <property>
        <name>yarn.nodemanager.vmem-check-enabled</name>
        <value>false</value>
        <description>Whether virtual memory limits will be enforced for containers</description>
    </property>
    <property>
        <name>yarn.nodemanager.vmem-pmem-ratio</name>
        <value>4</value>
        <description>Ratio between virtual memory to physical memory when setting memory limits for containers</description>
    </property>
    <!-- 环境变量的继承 -->
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
 			<value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>

```



(4) MapReducer配置文件 mapred-site.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <!-- 指定 MapReduce 程序运行在 Yarn 上 -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.env</name>
        <value>HADOOP_MAPRED_HOME=/opt/hadoop-3.1.3</value>
    </property>
    <property>
        <name>mapreduce.map.env</name>
        <value>HADOOP_MAPRED_HOME=/opt/hadoop-3.1.3</value>
    </property>
    <property>
        <name>mapreduce.reduce.env</name>
        <value>HADOOP_MAPRED_HOME=/opt/hadoop-3.1.3</value>
    </property>
</configuration>
```



#### 5、群起集群

```shell
## 配置workers
vim /opt/hadoop-3.1.3/etc/hadoop/workers
### 注意:该文件中添加的内容结尾不允许有空格,文件中不允许有空行。
flink1
flink2
flink3
### 同步所有节点配置文件
xsync /opt/hadoop-3.1.3/etc
```
(5) 分发配置文件

```shell
scp -r /opt/hadoop-3.1.3/etc/hadoop/ flink2:/opt/hadoop-3.1.3/etc
scp -r /opt/hadoop-3.1.3/etc/hadoop/ flink3:/opt/hadoop-3.1.3/etc
scp -r /opt/hadoop-3.1.3/etc/hadoop/ flink2:/opt/hadoop-3.1.3/etc
scp -r /opt/hadoop-3.1.3/etc/hadoop/ flink3:/opt/hadoop-3.1.3/etc
```
(1)如果是第一次启动

```shell
flink1机器上:hdfs namenode -format
```

(2)启动hdfs

```shell
flink1 sbin/start-dfs.sh
```

(3)启动yarn

```shell
flink2 sbin/start-yarn.sh
```

hdfs:namenode

http://flink1:9870

yarn:resourceManager

http://flink2:8088

#### 6、集群基本测试

上传小文件
htop

```shell
hadoop fs -mkdir /input
hadoop fs -put $HADOOP_HOME/wcinput/word.txt /input
pwd
/opt/hadoop-3.1.3/data/dfs/data/current/BP-1470048873-172.37.4.198-1621906661709/current/finalized/subdir0/subdir0
hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount /input /output
```


#### 7、配置历史服务器

```shell
vim mapred-site.xml
<!-- 历史服务器端地址 -->
<property>
<name>mapreduce.jobhistory.address</name>
<value>flink3:10020</value>
</property>
<!-- 历史服务器 web 端地址 -->
<property>
<name>mapreduce.jobhistory.webapp.address</name>
<value>flink3:19888</value>
</property>
## 分发配置
xsync $HADOOP_HOME/etc/hadoop/mapred-site.xml
在hadoop0启动：mapred --daemon start historyserver
```

8、配置日志聚集

概念：应用运行完成以后，将程序运行日志上传到HDFS系统上。

```shell
vim yarn-site.xml
<!-- 开启日志聚集功能 -->
<property>
<name>yarn.log-aggregation-enable</name>
<value>true</value>
</property>
<!-- 设置日志聚集服务器地址 -->
<property>
<name>yarn.log.server.url</name>
<value>http://flink1:19888/jobhistory/logs</value>
</property>
<!-- 设置日志保留时间为 7 天 -->
<property>
<name>yarn.log-aggregation.retain-seconds</name>
<value>604800</value>
</property>

#### 8、编写hadoop集群常用脚本

```shell
flink1 cd /home/flink/bin
vim myhadoop.sh



#!/bin/bash
if [ $# -lt 1 ]
then
    echo "No Args Input..."
    exit ;
fi
case $1 in
"start")
        echo " =================== 启动 hadoop 集群 ==================="
        echo " --------------- 启动 hdfs ---------------"
        ssh flink1 "/opt/hadoop-3.1.3/sbin/start-dfs.sh"
        echo " --------------- 启动 yarn ---------------"
        ssh flink2 "/opt/hadoop-3.1.3/sbin/start-yarn.sh"
        echo " --------------- 启动 historyserver ---------------"
        ssh flink3 "/opt/hadoop-3.1.3/bin/mapred --daemon start historyserver"
        echo " =================== 启动 hadoop 集群 完成==================="
;;
"stop")
        echo " =================== 关闭 hadoop 集群 ==================="
        echo " --------------- 关闭 historyserver ---------------"
        ssh flink3 "/opt/hadoop-3.1.3/bin/mapred --daemon stop historyserver"
        echo " --------------- 关闭 yarn ---------------"
        ssh flink2 "/opt/hadoop-3.1.3/sbin/stop-yarn.sh"
        echo " --------------- 关闭 hdfs ---------------"
        ssh flink1 "/opt/hadoop-3.1.3/sbin/stop-dfs.sh"
        echo " =================== 关闭 hadoop 集群 完成==================="

;;
*)
    echo "Input Args Error..."
;;
esac


## 执行权限
chmod +x myhadoop.sh
```

```shell
vim jpsall


#!/bin/bash
for host in flink1 flink2 flink3
do
              echo "=============== $host ==============="
              ssh $host jps
done

chmod +x jpsall
```

```
# mv /opt/hadoop-3.1.3/share/hadoop/common/lib/guava-27.0-jre.jar /opt/hadoop-3.1.3/share/hadoop/common/lib/guava-27.0-jre.jar.backup
# mv /opt/hadoop-3.1.3/share/hadoop/hdfs/lib/guava-27.0-jre.jar /opt/hadoop-3.1.3/share/hadoop/hdfs/lib/guava-27.0-jre.jar.backup
# mv /opt/hive/lib/guava-27.0-jre.jar /opt/hive/lib/guava-27.0-jre.jar.backup
# cp /home/flink/gua/guava-20.0.jar /opt/hadoop-3.1.3/share/hadoop/common/lib/
# cp /home/flink/gua/guava-20.0.jar /opt/hadoop-3.1.3/share/hadoop/hdfs/lib/
# cp /home/flink/gua/guava-20.0.jar /opt/hive/lib/
# scp -r /opt/hadoop-3.1.3/share/hadoop/common/lib/ flink2:/opt/hadoop-3.1.3/share/hadoop/common
# scp -r /opt/hadoop-3.1.3/share/hadoop/common/lib/ flink3:/opt/hadoop-3.1.3/share/hadoop/common
# scp -r /opt/hadoop-3.1.3/share/hadoop/hdfs/lib/ flink2:/opt/hadoop-3.1.3/share/hadoop/hdfs
# scp -r /opt/hadoop-3.1.3/share/hadoop/hdfs/lib/ flink3:/opt/hadoop-3.1.3/share/hadoop/hdfs

# 将lib下的包分发到flink2、flink3中
scp -r /opt/flink-1.12.4/lib/ flink2:/opt/flink-1.12.4
scp -r /opt/flink-1.12.4/lib/ flink3:/opt/flink-1.12.4

# find /opt/hadoop-3.1.3 -name *guava*


错误：
在部署的时候遇到了这个问题，是因为flink中guava不兼容的问题，参考下面这两个帖子可以解决
解决方案
https://blog.csdn.net/weixin_44500374/article/details/113244560
https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/connectors/table/hive/overview/
```shell

at org.apache.flink.shaded.netty4.io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.shaded.netty4.io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) [flink-dist_2.12-1.12.4.jar:1.12.4]
        at java.lang.Thread.run(Thread.java:748) [?:1.8.0_301]
Caused by: java.lang.NoSuchMethodError: com.google.common.base.Preconditions.checkArgument(ZLjava/lang/String;Ljava/lang/Object;)V
        at org.apache.hadoop.conf.Configuration.set(Configuration.java:1357) ~[hadoop-common-3.1.3.jar:?]
        at org.apache.hadoop.conf.Configuration.set(Configuration.java:1338) ~[hadoop-common-3.1.3.jar:?]
        at org.apache.hadoop.mapred.JobConf.setJar(JobConf.java:518) ~[hadoop-mapreduce-client-core-3.1.3.jar:?]
        at org.apache.hadoop.mapred.JobConf.setJarByClass(JobConf.java:536) ~[hadoop-mapreduce-client-core-3.1.3.jar:?]
        at org.apache.hadoop.mapred.JobConf.<init>(JobConf.java:430) ~[hadoop-mapreduce-client-core-3.1.3.jar:?]
        at org.apache.hadoop.hive.conf.HiveConf.initialize(HiveConf.java:5141) ~[flink-sql-connector-hive-3.1.2_2.12-1.12.4.jar:1.12.4]
        at org.apache.hadoop.hive.conf.HiveConf.<init>(HiveConf.java:5109) ~[flink-sql-connector-hive-3.1.2_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.table.catalog.hive.HiveCatalog.createHiveConf(HiveCatalog.java:230) ~[flink-sql-connector-hive-3.1.2_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.table.catalog.hive.HiveCatalog.<init>(HiveCatalog.java:169) ~[flink-sql-connector-hive-3.1.2_2.12-1.12.4.jar:1.12.4]
        at org.apache.flink.table.catalog.hive.factories.HiveCatalogFactory.createCatalog(HiveCatalogFactory.java:97) ~[flink-sql-connector-hive-3.1.2_2.12-1.12.4.jar:1.12.4]
        at com.ververica.flink.table.gateway.context.ExecutionContext.createCatalog(ExecutionContext.java:326) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext.lambda$null$5(ExecutionContext.java:508) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at java.util.HashMap.forEach(HashMap.java:1289) ~[?:1.8.0_301]
        at com.ververica.flink.table.gateway.context.ExecutionContext.lambda$initializeCatalogs$6(ExecutionContext.java:507) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext.wrapClassLoader(ExecutionContext.java:211) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext.initializeCatalogs(ExecutionContext.java:506) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext.initializeTableEnvironment(ExecutionContext.java:455) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext.<init>(ExecutionContext.java:151) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext.<init>(ExecutionContext.java:113) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        at com.ververica.flink.table.gateway.context.ExecutionContext$Builder.build(ExecutionContext.java:703) ~[flink-sql-gateway-0.3-SNAPSHOT.jar:?]
        ... 46 more
2021-07-27 16:10:52
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.301-b09 mixed mode):

"flink-rest-server-netty-worker-thread-1" #14 daemon prio=5 os_prio=0 tid=0x00007f175000c800 nid=0x4cb2 runnable [0x00007f1774164000]
   java.lang.Thread.State: RUNNABLE
        at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
        at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
        at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
        at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
        - locked <0x000000008204d858> (a org.apache.flink.shaded.netty4.io.netty.channel.nio.SelectedSelectionKeySet)
        - locked <0x000000008204e958> (a java.util.Collections$UnmodifiableSet)
        - locked <0x000000008204e880> (a sun.nio.ch.EPollSelectorImpl)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:101)
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.SelectedSelectionKeySetSelector.select(SelectedSelectionKeySetSelector.java:68)
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.NioEventLoop.select(NioEventLoop.java:803)
        at org.apache.flink.shaded.netty4.io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:457)
        at org.apache.flink.shaded.netty4.io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
        at org.apache.flink.shaded.netty4.io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at java.lang.Thread.run(Thread.java:748)

"pool-2-thread-1" #13 prio=5 os_prio=0 tid=0x00007f17c17d1000 nid=0x4c95 waiting on condition [0x00007f1774265000]
   java.lang.Thread.State: TIMED_WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x0000000082463a78> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:215)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2078)
        at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1093)
        at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:809)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

"flink-rest-server-netty-boss-thread-1" #12 daemon prio=5 os_prio=0 tid=0x00007f17c17cc800 nid=0x4c94 runnable [0x00007f1774366000]
   java.lang.Thread.State: RUNNABLE
        at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
        at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
        at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
        at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
        - locked <0x00000000820d5468> (a org.apache.flink.shaded.netty4.io.netty.channel.nio.SelectedSelectionKeySet)
        - locked <0x00000000820d6568> (a java.util.Collections$UnmodifiableSet)
        - locked <0x00000000820d6490> (a sun.nio.ch.EPollSelectorImpl)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
```


