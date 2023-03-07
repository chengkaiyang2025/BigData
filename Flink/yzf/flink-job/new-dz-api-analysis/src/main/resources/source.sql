CREATE TABLE nginx_log_input_table (
    `request`                   string,
    `offset`                    bigint,
    `base_url`                  AS SPLIT_INDEX(SPLIT_INDEX(request,' ',1),'?',0),
    `upstream_addr_nm`          string,
    `request_length`            string,
    `http_cookie`               string,
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
    'properties.bootstrap.servers' = '172.24.215.5:9093',
    'properties.group.id' = 'log_analysis',
    'format' = 'json',
    'json.fail-on-missing-field' = 'false',
    'json.ignore-parse-errors' = 'true',
    'scan.startup.mode' = 'latest-offset'
    )


-- CREATE TABLE input_table (
--     id           string,
--     `offset`     string,
--     fetch_time   string,
--     event_time AS TO_TIMESTAMP(FROM_UNIXTIME(UDTimeFormat2UnixTime(fetch_time) / 1000, 'yyyy-MM-dd HH:mm:ss')),
--     WATERMARK FOR event_time AS event_time - INTERVAL '10' SECOND
--      )
-- WITH (
--     'connector' = 'kafka',
--     'topic' = 'nginx_log1',
--     'properties.bootstrap.servers' = '172.26.9.81:9092',
--     'properties.group.id' = 'log_analysis',
--     'format' = 'json',
--     'json.fail-on-missing-field' = 'false',
--     'json.ignore-parse-errors' = 'true',
-- --     'json.map-null-key.mode' = 'DROP',
--     'scan.startup.mode' = 'earliest-offset'
--     )