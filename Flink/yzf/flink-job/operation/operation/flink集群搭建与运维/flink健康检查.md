# Flink健康检查

## 实现目标

能检测出线上flink的jobmanager、taskmanager运行情况、当前正在运行的job，运行失败、取消的job

参考资料：

https://blog.csdn.net/lovehave/article/details/113505954

restful接口地址https://ci.apache.org/projects/flink/flink-doc-s-master/zh/docs/ops/rest_api/#api



1、消息一

每天检查一次当前的集群健康情况

​	包括taskmanager数量等，正在运行的flink数量、slot数量

2、消息二

每分钟检测近5分钟内的失败任务，打印失败原因

3、消息三

每分钟检测近1分钟内的取消任务

4、消息四

每分钟检测一次jobmanager，如果有问题则预警

每分钟检测一次taskmanager数量的数量。

由于实现代码需要使用python工具栈，因此flink健康检查的脚本放在[这个项目托管代码](http://gitlab.yzf.net/group_di/bigdata/metadata-management)，[相关issue](http://gitlab.yzf.net/group_di/bigdata/metadata-management/issues/3)

