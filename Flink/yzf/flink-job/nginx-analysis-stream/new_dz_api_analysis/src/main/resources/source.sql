-- 线上
CREATE TABLE nginx_log_format (
    `request`                   string,
    `offset`                    bigint,
    `base_url`                  AS SPLIT_INDEX(SPLIT_INDEX(request,' ',1),'?',0),
    `upstream_addr_nm`          string,
    `request_length`            string,
    `http_cookie`               string,
--     `gsid`                      AS ParseCookie2GsId(http_cookie),
    `fields-log_es_index`       string,
    `fields-log_item`           string,
    `fields-apptype`            string,
    `fields-set`                string,
    `http_referer`              string,
    `remote_addrx`              string,
    `remote_user`               string,
    `scheme`                    string,
    `body_bytes_sent`           string,
    `status`                    string,
    `host-name`                 string,
    `x_forwarded_for`           string,
    `nginx`                     string,
    `proxy_add_x_forwarded_for` string,
    `beat-hostname`             string,
    `beat-name`                 string,
    `beat-version`              string,
    `source`                    string,
    `time_local`                string,
    `http_user_agent`           string,
    `upstream_addr`             string,
    `request_time`              double,
    `ssl_cipher`                string,
    `event_time` AS TO_TIMESTAMP(FROM_UNIXTIME(UDTimeFormat2UnixTime(time_local) / 1000, 'yyyy-MM-dd HH:mm:ss')),
     WATERMARK FOR event_time AS event_time - INTERVAL '5' SECOND
     )
WITH (
    'connector' = 'kafka',
    'topic' = 'safe_interface_log',
    'properties.bootstrap.servers' = '172.16.190.35:9092,172.16.190.36:9092,172.16.190.37:9092',
    'scan.startup.mode' = 'latest-offset',
    'properties.group.id' = 'api_analysis',
    'format' = 'json',
    'json.fail-on-missing-field' = 'false',
    'json.ignore-parse-errors' = 'true'
    )


-- 测试
-- 172.24.10.4:9092

-- 线上
-- 172.16.190.35:9092,172.16.190.36:9092,172.16.190.37:9092
-- earliest-offset
-- latest-offset


-- 线下
-- CREATE TABLE nginx_log_format (
--     `request`                   string,
--     `offset`                    bigint,
--     `base_url`                  AS SPLIT_INDEX(SPLIT_INDEX(request,' ',1),'?',0),
--     `upstream_addr_nm`          string,
--     `request_length`            string,
--     `http_cookie`               string,
-- --     `gsid`                      AS ParseCookie2GsId(http_cookie),
--     `fields-log_es_index`       string,
--     `fields-log_item`           string,
--     `fields-apptype`            string,
--     `fields-set`                string,
--     `http_referer`              string,
--     `remote_addrx`              string,
--     `remote_user`               string,
--     `scheme`                    string,
--     `body_bytes_sent`           string,
--     `status`                    string,
--     `host-name`                 string,
--     `x_forwarded_for`           string,
--     `nginx`                     string,
--     `proxy_add_x_forwarded_for` string,
--     `beat-hostname`             string,
--     `beat-name`                 string,
--     `beat-version`              string,
--     `source`                    string,
--     `time_local`                string,
--     `http_user_agent`           string,
--     `upstream_addr`             string,
--     `request_time`              double,
--     `ssl_cipher`                string,
--     `event_time` AS TO_TIMESTAMP(FROM_UNIXTIME(UDTimeFormat2UnixTime(time_local) / 1000, 'yyyy-MM-dd HH:mm:ss')),
--      WATERMARK FOR event_time AS event_time - INTERVAL '5' SECOND
--      )
-- WITH (
--     'connector' = 'kafka',
--     'topic' = 'new_nginx_log',
--     'properties.bootstrap.servers' = '172.24.10.2:9092,172.24.10.3:9092,172.24.10.4:9092,172.24.10.5:9092',
--     'scan.startup.mode' = 'latest-offset',
-- --     'scan.startup.timestamp-millis' = '1633801991000',
-- --     'scan.startup.specific-offsets' = 'partition:0,offset:58542479',
--     'properties.group.id' = 'offlineApiInfoAnalysis',
--     'format' = 'json',
--     'json.fail-on-missing-field' = 'false',
--     'json.ignore-parse-errors' = 'true'
--     )