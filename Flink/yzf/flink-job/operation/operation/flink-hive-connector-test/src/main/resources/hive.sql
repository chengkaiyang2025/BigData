drop table flink_database.hive_table_process;
CREATE TABLE flink_database.hive_table_process (
  user_id STRING,
  order_amount DOUBLE
)
PARTITIONED BY (dt STRING, hr STRING)
STORED AS parquet
TBLPROPERTIES (
--   'partition.time-extractor.timestamp-pattern'='$dt $hr:00:00',
--   'sink.partition-commit.trigger'='partition-time',
--   'sink.partition-commit.delay'='1 h',
--   'sink.partition-commit.watermark-time-zone'='Asia/Shanghai', -- Assume user configured time zone is 'Asia/Shanghai'
  'sink.partition-commit.policy.kind'='metastore,success-file'
); 'hdfs://HDFS43348/usr/hive/warehouse/tmp.db/tmp_dwd_dim_compant_info_d_111'


 alter table flink_database.hive_table_process add partition(dt=20210301,hr=23);
show partitions flink_database.hive_table_process;

-- drop table flink_database.hive_table_process


select * from flink_database.hive_table
;
show create table flink_database.hive_table

