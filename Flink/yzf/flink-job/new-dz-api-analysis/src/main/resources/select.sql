select
    base_url,
    status,
    count(1) as num,
    tumble_end(event_time,interval '20' second) as cnt_time
from nginx_log_input_table
where `fields-set` is null
and status not in ('200','304')
and base_url is not null
and status is not null
and `fields-log_es_index` = 'fintax-web'
group by
    base_url,
    status,
    tumble(event_time,interval '20' second)
having count(1) > 20




