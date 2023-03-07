select *
from nginx_log_input_table
where `fields-set` is null
and `status` not in ('200','304')
and `fields-log_es_index` = 'fintax-web'
and `request` is not null
and `offset` is not null




