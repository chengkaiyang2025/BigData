本文件夹主要记录flink资料

# 官方文档
## Flink概念
- flink端到端一致性保证。
  *google关键词：flink 2pc、wal; flink 端到端一致性*
  - [掘金博客](https://juejin.cn/post/6844904070260457480)，比较易于理解，其实就是要求外部存储如hive或kafka支持事务提交，有一个地方可以先缓存数据保证幂等写入。
  - [flink官网博客](https://flink.apache.org/features/2018/03/01/end-to-end-exactly-once-apache-flink.html)，比较易于
- [flink对流中的乱序数据进行排序](https://stackoverflow.com/questions/65980505/how-do-i-handle-out-of-order-events-with-apache-flink)。主要使用底层api，通过ListState或MapState，
  结合timer，来做排序。
  *google关键词：flink sort out of order stream*
## flink 状态后端
## flink 检查点

## flink rocksdb
### LSM树
 TODO
### rocksdb原理
 TODO
### 调优
- [微信文章](https://mp.weixin.qq.com/s/YpDi3BV8Me3Ay4hzc0nPQA)，里面的原理和调优写的比较好。
- [官方文档rocksdb参数说明](https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/deployment/config/#rocksdb-state-backend)、[1](https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/ops/state/large_state_tuning/#tuning-rocksdb-memory)、
# 博客
