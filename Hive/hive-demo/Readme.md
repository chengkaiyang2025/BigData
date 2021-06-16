# HIVE udf的测试类方法
[stackoverflow](https://stackoverflow.com/questions/37112444/how-are-hive-udf-udaf-udtfs-written-in-java-debugged-in-an-ide-like-eclipse)
create function default.adda_yck as "com.atguigu.udf.AddA";
Cloudera推荐使用GenericUDF：
add jar /tmp/hive-demo-1.0-SNAPSHOT.jar

https://vipulpathak.com/2015/07/18/writing-a-generic-udf-in-hive/
[Cloudera recommends that you use the org.apache.hadoop.hive.ql.udf.generic.GenericUDF API instead when you are creating custom UDFs for Hive.](https://docs.cloudera.com/documentation/enterprise/latest/topics/cm_mc_hive_udf.html)