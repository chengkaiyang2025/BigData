-- CREATE TABLE nginx_result_output_table (
--     id STRING,
--     cnt BIGINT,
--     cnt_time BIGINT )
-- WITH (
--     'connector' = 'kafka',
--     'topic' = 'nginx_result',
--     'properties.bootstrap.servers' = '172.28.177.120:9092',
--     'format' = 'json',
--     'json.ignore-parse-errors' = 'false'
-- )

CREATE TABLE nginx_result_output_table (
    id STRING,
    cnt BIGINT)
WITH (
    'connector' = 'kafka',
    'topic' = 'nginx_result',
    'properties.bootstrap.servers' = '172.28.177.120:9092',
    'format' = 'json',
    'json.ignore-parse-errors' = 'false'
)