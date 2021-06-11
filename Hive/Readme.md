# 说明

教程来自[bilibili尚硅谷](https://www.bilibili.com/video/BV1EZ4y1G7iL)

# 第二章 hive安装





```xml
士大夫
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
<!-- jdbc 连接的 URL -->
<property>
<name>javax.jdo.option.ConnectionURL</name>
<value>jdbc:mysql://hadoop102:3306/metastore?useSSL=false</value>
</property>
<!-- jdbc 连接的 Driver-->
<property>
<name>javax.jdo.option.ConnectionDriverName</name>
<value>com.mysql.jdbc.Driver</value>
</property>
<!-- jdbc 连接的 username-->
<property>
<name>javax.jdo.option.ConnectionUserName</name>
<value>root</value>
</property>
<!-- jdbc 连接的 password -->
<property>
<name>javax.jdo.option.ConnectionPassword</name>
<value>000000</value>
</property>
<!-- Hive 元数据存储版本的验证 -->
<property>
<name>hive.metastore.schema.verification</name>
<value>false</value>
</property>
<!--元数据存储授权-->
<property>
<name>hive.metastore.event.db.notification.api.auth</name>
<value>false</value>
</property>
<!-- Hive 默认在 HDFS 的工作目录 -->
<property>
<name>hive.metastore.warehouse.dir</name>
<value>/user/hive/warehouse</value>
</property>
</configuration>
```



```shell
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/opt/module/hive/lib/log4j-slf4j-impl-2.10.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/opt/module/hadoop-3.1.3/share/hadoop/common/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Exception in thread "main" java.lang.NoSuchMethodError: com.google.common.base.Preconditions.checkArgument(ZLjava/lang/String;Ljava/lang/Object;)V
	at org.apache.hadoop.conf.Configuration.set(Configuration.java:1357)
	at org.apache.hadoop.conf.Configuration.set(Configuration.java:1338)
	at org.apache.hadoop.mapred.JobConf.setJar(JobConf.java:518)
	at org.apache.hadoop.mapred.JobConf.setJarByClass(JobConf.java:536)
	at org.apache.hadoop.mapred.JobConf.<init>(JobConf.java:430)
	at org.apache.hadoop.hive.conf.HiveConf.initialize(HiveConf.java:5141)
	at org.apache.hadoop.hive.conf.HiveConf.<init>(HiveConf.java:5104)
	at org.apache.hive.beeline.HiveSchemaTool.<init>(HiveSchemaTool.java:96)
	at org.apache.hive.beeline.HiveSchemaTool.main(HiveSchemaTool.java:1473)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.apache.hadoop.util.RunJar.run(RunJar.java:318)
	at org.apache.hadoop.util.RunJar.main(RunJar.java:232)

https://kontext.tech/column/hadoop/415/hive-exception-in-thread-main-javalangnosuchmethoderror-comgooglecommon
```









```xml
1)在 hive-site.xml 文件中添加如下配置信息

<!-- 指定存储元数据要连接的地址 -->
<property>
<name>hive.metastore.uris</name>
<value>thrift://hadoop102:9083</value>
</property>
<!-- 指定 hiveserver2 连接的 host -->
<property>
<name>hive.server2.thrift.bind.host</name>
<value>hadoop102</value>
</property>
<!-- 指定 hiveserver2 连接的端口号 -->
<property>
<name>hive.server2.thrift.port</name>
<value>10000</value>
</property>
<property>
<name>hive.cli.print.header</name>
<value>true</value>
</property>
<property>
<name>hive.cli.print.current.db</name>
<value>true</value>
</property>
```







```shell

#!/bin/bash
HIVE_LOG_DIR=$HIVE_HOME/logs
if [ ! -d $HIVE_LOG_DIR ]
then
mkdir -p $HIVE_LOG_DIR
fi
#检查进程是否运行正常,参数 1 为进程名,参数 2 为进程端口
function check_process()
{
pid=$(ps -ef 2>/dev/null | grep -v grep | grep -i $1 | awk '{print
$2}')
ppid=$(netstat -nltp 2>/dev/null | grep $2 | awk '{print $7}' | cut -d '/' -f 1)
echo $pid
[[ "$pid" =~ "$ppid" ]] && [ "$ppid" ] && return 0 || return 1
}
function hive_start()
{
metapid=$(check_process HiveMetastore 9083)
cmd="nohup hive --service metastore >$HIVE_LOG_DIR/metastore.log 2>&1
&"
[ -z "$metapid" ] && eval $cmd || echo "Metastroe 服务已启动"
server2pid=$(check_process HiveServer2 10000)
cmd="nohup hiveserver2 >$HIVE_LOG_DIR/hiveServer2.log 2>&1 &"
[ -z "$server2pid" ] && eval $cmd || echo "HiveServer2 服务已启动"
}
function hive_stop()
{
metapid=$(check_process HiveMetastore 9083)
[ "$metapid" ] && kill $metapid || echo "Metastore 服务未启动"
server2pid=$(check_process HiveServer2 10000)
[ "$server2pid" ] && kill $server2pid || echo "HiveServer2 服务未启动"
}
case $1 in
"start")
hive_start
;;
"stop")
hive_stop
;;
"restart")
hive_stop
sleep 2
hive_start
;;
"status")
check_process HiveMetastore 9083 >/dev/null && echo "Metastore 服务运行
正常" || echo "Metastore 服务运行异常"
check_process HiveServer2 10000 >/dev/null && echo "HiveServer2 服务运
行正常" || echo "HiveServer2 服务运行异常"
;;
*)
echo Invalid Args!
echo 'Usage: '$(basename $0)' start|stop|restart|status'
;;
esac

```

# 第三章 hive的数据结构







3、数据结构



```json
{
"name": "songsong",
"friends": ["bingbing" , "lili"] ,
//列表 Array,
"children": {
//键值 Map,
"xiao song": 18 ,
"xiaoxiao song": 19
}
"address": {
//结构 Struct,
"street": "hui long guan",
"city": "beijing"
}
}
```



```csv
songsong,bingbing_lili,xiao song:18_xiaoxiao song:19,hui long guan_beijing
yangyang,caicai_susu,xiao yang:18_xiaoxiao yang:19,chao yang_beijing
```

```sql
create table test(
	name string,
    friends array<string>,
    children map<string,int>,
    address struct<street:string, city:string>
)
row format delimited fields terminated by ','
collection items terminated by '_'
map keys terminated by ':'
lines terminated by '\n';

```



```tsv
load data local inpath '/opt/module/hive/datas/test.txt' into table test;
```



# 第四章 ddl

## 4.5.1、管理表

```csv
1001,ss1
1002,ss2
1003,ss3
1004,ss4
1005,ss5
1006,ss6
1007,ss7
1008,ss8
1009,ss9
1010,ss10
1011,ss11
1012,ss12
1013,ss13
1014,ss14
1015,ss15
1016,ss16

```

```shell
# 将txt文件上传到hdfs上。
hadoop fs -put students.txt /user/hive/warehouse/student/
```



```sql
# 创建表结构
create table if not exists student(
id int,name string)
row format delimited fields terminated by ','
stored as textfile
location '/user/hive/warehouse/student';
# create as
create table if not exists student2 as select * from student;
# like语义只会创建表结构
create table if not exists student3 like student;
desc formatted student;

```

## 4.5.2、外部表

dept.csv

```csv
10,ACCOUNTING ,1700
20,RESEARCH,1800
30,SALES,1900
40,OPERATIONS,1700
```

```shell


```

```sql
create external table if not exists dept(
deptno int,
dname string,
    loc int
)
row format delimited fields terminated by ','
# location '/user/hive/warehouse/dept' ;

load data local inpath '/opt/module/hive/datas/dept.txt' into table dept;

```

sql

```sql
7369,SMITH,CLERK,1980/12/17,800,,20
7499,ALLEN,SALESMAN,1981/2/20,1600,300,30
7521,WARD,SALESMAN,1981/2/22,1250,500,30
7566,JONES,MANAGER,1981/4/2,2975,,20
7654,MARTIN,SALESMAN,1981/9/28,1250,1400,30
7698,BLAKE,MANAGER,1981/5/1,2850,,30
7782,CLARK,MANAGER,1981/6/9,2450,,10
7788,SCOTT,ANALYST,1987/4/19,3000,,20
7839,KING,PRESDENT,1981/11/17,5000,,10
7844,TURNER,SALESMAN,1981/9/8,1500,0,30
7876,ADAMS,CLERK,1987/5/23,1100,,20
7900,JAMES,CLERK,1981/12/3,950,,30
7902,FORD,ANALYST,1981/12/3,3000,,20
7934,MILLER,CLERK,1982/1/23,1300,,10
```

```sql
create external table if not exists emp(
empno int,
ename string,
job string,
hiredate string,
sal double,
comm double,
deptno int)
row format delimited fields terminated by ',';
load data local inpath '/opt/module/hive/datas/emp.txt' into table emp;
```





## 4.5.3、管理表与外部表转化

```sql
alter table student2 set tblproperties('EXTERNAL'='TRUE');
```

# 第五章、DML数据操作

## 5.1、数据导入

### 5.1.1、load的syntax

```sql
load data [local] path '' [overwrite] into table student [partition (partcol1=val1)]
```

#### 5.1.4 创建表时通过 Location 指定加载数据路径

```sql
create external table if not exists student5(id int,name string)
row format delimited fields terminated by ','
location '/student'
```

## 5.2、数据导出

将查询结果导出到本地

```sql
insert overwrite local directory '/opt/module/hive/datas/student_export' 
row format delimited fields terminated by ','
select * from student;
```



# 第六章

### 6.4.5 sort by 与distribute by

Distribute By: 在有些情况下,我们需要控制某个特定行应该到哪个 reducer,通常是为
了进行后续的聚集操作。distribute by 子句可以做这件事。distribute by 类似 MR 中 partition
(自定义分区),进行分区,结合 sort by 使用。

```sql
set mapreduce.job.reducers=3;
insert overwrite local directory '/opt/module/hive/datas/distribute-result' row format delimited fields terminated by ','
select * from emp distribute by deptno sort by empno desc;
```

# 第七章

## 7.1 分区表

### 7.1.1、分区表基本操作

数据准备

dept_20200401.log

```csv
10,ACCOUNTING,1700
20,RESEARCH,1800
```

dept_20200402.log

```csv
30,SALES,1900
40,OPERATIONS,1700
```

dept_20200403.log

```csv
50,TEST,200
60,DEV,1900
```

1、内部分区表

```sql
create table dept_partition(
    deptno int,
    dname string,
    loc string
)
partitioned by (day string)
row format delimited fields terminated by ',';
```

从本地文件加载数据到hive

```shell
load data local inpath '/opt/module/hive/datas/dept_20200401.log' overwrite into table dept_partition partition(day='20200401');
load data local inpath '/opt/module/hive/datas/dept_20200402.log' overwrite into table dept_partition partition(day='20200402');
load data local inpath '/opt/module/hive/datas/dept_20200402.log' overwrite into table dept_partition partition(day='20200403');
```

查看表

```sql
select * from dept_partition;
show partitions dept_partition;		
desc formatted dept_partition;


```



## 7.2、分桶表

```csv
1001,ss1
1002,ss2
1003,ss3
1004,ss4
1005,ss5
1006,ss6
1007,ss7
1008,ss8
1009,ss9
1010,ss10
1011,ss11
1012,ss12
1013,ss13
1014,ss14
1015,ss15
1016,ss16
```

创建分桶表

```sql
create table stu_buck(id int,name string)
clustered by (id) into 4 buckets
row format delimited fields terminated by ',';
```

加载数据

```shell
load data inpath '/input/student.txt' into table stu_buck;
```

加载的时候会有四个reducers，number of reducers。按照key的hashcode mod 桶的数量，分配数据。

不要使用本地模式，避免找不到数据。





















