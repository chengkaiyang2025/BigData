CREATE TABLE nginx_log_anticrawler_formatter (
time_local STRING,
time_local_ts BIGINT,
ts AS TO_TIMESTAMP(FROM_UNIXTIME(time_local_ts / 1000, 'yyyy-MM-dd HH:mm:ss')), -- 定义事件时间
WATERMARK FOR ts AS ts - INTERVAL '10' SECOND,   -- 在ts上定义5 秒延迟的 watermark
fields_set STRING,
nginx STRING,
request STRING,
status STRING,
cookie_user_name STRING,
cookie_gs_id STRING,
ua_os_name STRING,
ua_device_type STRING,
ua_ua_family STRING,
remote_addrx STRING,
request_time DOUBLE,
request_length DOUBLE
) WITH (
'connector' = 'kafka', -- using kafka connector
--'connector.version' = 'universal',  -- kafka 版本，universal 支持 0.11 以上的版本
'topic' = 'nginx_log_anticrawler_formatter', -- kafka topic
'properties.group.id' = 'hue-yzf-stream',
'scan.startup.mode' = 'latest-offset', -- reading from the beginning
-- 生产
'properties.bootstrap.servers' = '172.16.190.35:9092,172.16.190.36:9092,172.16.190.37:9092', -- kafka broker address
-- 测试
--'properties.bootstrap.servers' = '172.24.215.5:9093', -- kafka broker address
'format' = 'json' -- the data format is json
)