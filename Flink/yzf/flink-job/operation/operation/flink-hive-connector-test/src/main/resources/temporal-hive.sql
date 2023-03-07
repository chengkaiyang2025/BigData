drop table flink_database.user_info;
CREATE TABLE flink_database.user_info (
  user_id STRING,
  user_name STRING
) PARTITIONED BY (dt STRING, hr STRING)
 STORED AS parquet
 TBLPROPERTIES (
  -- using default partition-name order to load the latest partition every 12h (the most recommended and convenient way)
  'streaming-source.enable' = 'true',
  'streaming-source.partition.include' = 'latest',
  -- 线上不要设置一分钟
--  'streaming-source.monitor-interval' = '1 h',
  'streaming-source.monitor-interval' = '8 h',
  'streaming-source.partition-order' = 'partition-name'  -- option with default value, can be ignored.
);


insert overwrite table flink_database.user_info partition (dt='20210818',hr='16')
select 'user1','username20210818-16';

insert overwrite table flink_database.user_info partition (dt='20210818',hr='16')
select 'user1','yck1'
union all
select 'user2','yck2'
union all
select 'user3','yck2';

insert overwrite table flink_database.user_info partition (dt='20210818',hr='16')
select 'user0','bill0'
union all
select 'user1','bill1'
union all
select 'user2','bill2'
union all
select 'user3','bill3';

insert overwrite table flink_database.user_info partition (dt='20210818',hr='17')
select 'user1','yck-emr1'
union all
select 'user2','yck-emr2'
union all
select 'user3','yck-emr3';



drop table flink_database.dept_info;
CREATE TABLE flink_database.dept_info (
  user_id STRING,
  depart_name STRING
) PARTITIONED BY (dt STRING, hr STRING)
 STORED AS parquet
 TBLPROPERTIES (
  -- using default partition-name order to load the latest partition every 12h (the most recommended and convenient way)
  'streaming-source.enable' = 'true',
  'streaming-source.partition.include' = 'latest',
  -- 线上不要设置一分钟
--  'streaming-source.monitor-interval' = '1 h',
  'streaming-source.monitor-interval' = '1 h',
  'streaming-source.partition-order' = 'partition-name'  -- option with default value, can be ignored.
);
insert overwrite table flink_database.dept_info partition (dt='20210818',hr='17')
select 'user1','FBI'
union all
select 'user2','CIA'
union all
select 'user3','MUSS';



insert overwrite table flink_database.dept_info partition (dt='20210818',hr='18')
select 'user1','旺旺大队emr'
union all
select 'user2','喵喵大队emr'
union all
select 'user3','旺旺大队emr';

select * from flink_database.dept_info;

