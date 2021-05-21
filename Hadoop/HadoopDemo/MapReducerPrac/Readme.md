这个model的项目主要是以nginx日志为例，使用mr程序进行统计分析。


参考:[issue](https://github.com/yunzhangfang-di/Bigdata/issues/2)
-- 必须找到yarn中log4j对应的 DailyRollingFileAppender，可是集群上就没有
export HADOOP_ROOT_LOGGER=hadoop.root.logger=DEBUG,DailyRollingFileAppender,console

hdfs dfs -ls
集群运行命令：

hadoop jar MapReducerPrac-1.0-SNAPSHOT-jar-with-dependencies.jar -Dyarn.app.mapreduce.am.log.level=DEBUG -Dmapreduce.map.log.level=DEBUG -Dmapreduce.reduce.log.level=DEBUG