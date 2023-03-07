drop table log_ods.ods_new_dz_nginx_log_all;
CREATE external TABLE test.ods_new_dz_nginx_log_all (

request  string,
cookie_gs_id  string,
time_local_ts  string
)
PARTITIONED BY (dt STRING,hr STRING)
STORED AS parquet
location '/apps/data/warehouse/test/ods_new_dz_nginx_log_all'
TBLPROPERTIES (
  'sink.partition-commit.policy.kind'='metastore,success-file'
  -- "parquet.compression"="SNAPPY"
);
-- 必须要用 impala跑数，hive跑数会丢数据，见问题：
-- http://gitlab.yzf.net/group_di/bigdata/flink-job/issues/47
drop table flink_database.dim_dzgs_d;
CREATE TABLE flink_database.dim_dzgs_d (
  gsid STRING,
  gsmc STRING,
  gslx STRING
)
PARTITIONED BY (dt STRING)
STORED AS parquet
TBLPROPERTIES (
  'streaming-source.enable' = 'true',
  'streaming-source.partition.include' = 'latest',
  -- 线上不要设置一分钟
--  'streaming-source.monitor-interval' = '1 h',
  'streaming-source.monitor-interval' = '8 h',
  'streaming-source.partition-order' = 'partition-name'  -- option with default value, can be ignored.
);
insert overwrite table flink_database.dim_dzgs_d
partition(dt='20210826')
SELECT cast(t.gsid as string),t.gsmc,t.gslx from dwd.dwd_dim_company_info_d t
where t.dt = '20210826'
group by t.gsid,t.gsmc,t.gslx
;

drop table log_ods.ods_new_dz_nginx_log_all;
CREATE TABLE log_ods.ods_new_dz_nginx_log_all (
upstream_addr  string,
body_bytes_sent  string,
ssl_cipher  string,
source  string,
proxy_add_x_forwarded_for  string,
remote_user  string,
request  string,
request_time  double,
time_local  string,
http_user_agent  string,
http_referer  string,
remote_addrx  string,
nginx  string,
scheme  string,
status  string,
upstream_addr_nm  string,
x_forwarded_for  string,
cookie_gs_id  string,
cookie_phone  string,
cookie_user_id  string,
cookie_user_name  string,
ua_browser_version_info  string,
ua_device_type  string,
ua_os_family  string,
ua_os_name  string,
ua_type  string,
ua_ua_family  string,
ua_ua_name  string,
time_local_ts  string,
gsmc string,
gslx string
)
PARTITIONED BY (dt STRING)
STORED AS parquet
--location '/apps/data/warehouse/log_ods/ods_new_dz_nginx_log_all'
TBLPROPERTIES (
  'sink.partition-commit.policy.kind'='metastore,success-file',
   "parquet.compression"="SNAPPY"
);