select
    base_url,
    status,
    count(1) as num,
    tumble_end(event_time,interval '20' second) as cnt_time
from nginx_log_format
where `fields-set` is null
and status not in ('200','304')
and `fields-log_es_index` = 'fintax-web'
and base_url not in ('/task/api/task/getTasks/v2','/task/api/task/heartbeat')
group by
    base_url,
    status,
    tumble(event_time,interval '20' second)
having count(1) >= 15


-- 2021-10-12
-- 细化报警阈值
-- 20s内错误码次数超过12次进行告警
-- 20s内错误码次数超过8次且返回时长大于3S进行告警
-- 20s内错误码次数超过5次且返回时长大于5S进行告警

-- select
--     t.base_url,
--     t.status,
--     t.num,
--     t.request_time,
--     t.cnt_time
-- from (
--     select
--         base_url,
--         status,
--         count(1) as num,
--         request_time,
--         tumble_end(event_time,interval '20' second) as cnt_time
--     from nginx_log_format
--     where `fields-set` is null
--     and status not in ('200','304')
--     and `fields-log_es_index` = 'fintax-web'
--     group by
--         base_url,
--         status,
--         request_time,
--         tumble(event_time,interval '20' second)
-- ) t
-- where t.num >= 12
-- or (t.num >= 8 and t.request_time > 3)
-- or (t.num >= 5 and t.request_time > 5)









