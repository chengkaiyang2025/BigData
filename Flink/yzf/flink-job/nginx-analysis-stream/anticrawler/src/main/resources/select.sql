
select
a.cookie_gs_id,
a.cookie_user_name,
tumble_end(ts,interval '60' second) as cnt_time,
a.web_page,
count(1) as cnt
--avg(request_time) as req_avg
from
(
    select
    t.ts,
    case
    when t.request like 'GET /portal/api/customerManager/queryZtxxKjzdAndBwbAndKjhy%'
    then '企业账套信息-基础设置-企业信息-基本信息'
    when t.request like 'GET /pingzheng/api/zhangmu/listKemuYeM%'
    then '企业账套信息-财务处理-帐薄-科目余额-科目余额'
    when t.request like 'GET /pingzheng/api/zhangmu/listKemuYe%'
    then '企业账套信息-财务处理-帐薄-科目余额'
    when t.request like 'GET /pingzheng/api/zhangmu/slhs/getKmyeSlhs%'
    then '企业账套信息-财务处理-帐薄-科目余额-数量金额科目余额表'
    when t.request like 'POST /portal/api/customerManager/queryCompanyList%'
    then '客户管理-客户信息-客户列表'
    when t.request like 'GET /portal/api/customerManager/quertQyZtxx%'
    then '企业账套信息'
    when t.request like 'POST /portal/api/index/ajax/mainOther%'
    then '主页面'
    when t.request like 'POST /portal/api/index/ajax/homeQyListByCondition%'
    then '企业列表旧接口'
    when t.request like 'POST /portal/index/book/list%'
    then '企业列表(新)接口'
    when t.request like 'POST /system/api/system/ztkm/getAllPzMjkm'
    then '企业账套信息-财务处理-凭证-查询凭证末级科目数据'
    when t.request like 'POST /system/api/system/ztkm/getAllZtkm'
    then '企业账套信息-基础设置-会计科目'
    end as web_page
    ,t.fields_set
    ,t.status
    ,t.cookie_user_name
    ,t.cookie_gs_id
    ,t.ua_os_name
    ,t.ua_device_type
    ,t.request_time
    ,t.request_length
    from
    nginx_log_anticrawler_formatter t
    where t.request like 'GET /portal/api/customerManager/queryZtxxKjzdAndBwbAndKjhy%'
    or t.request like 'GET /pingzheng/api/zhangmu/listKemuYeM%'
    or t.request like 'GET /pingzheng/api/zhangmu/listKemuYe%'
    or t.request like 'GET /pingzheng/api/zhangmu/slhs/getKmyeSlhs%'
    or t.request like 'POST /portal/api/customerManager/queryCompanyList%'
    or t.request like 'GET /portal/api/customerManager/quertQyZtxx%'
    or t.request like 'POST /portal/api/index/ajax/mainOther%'
    or t.request like 'POST /portal/api/index/ajax/homeQyListByCondition%'
    or t.request like 'POST /portal/index/book/list%'
    or t.request like 'POST /system/api/system/ztkm/getAllPzMjkm'
    or t.request like 'POST /system/api/system/ztkm/getAllZtkm'
) a
where a.cookie_user_name is not null
group by
a.cookie_gs_id,
a.cookie_user_name,
tumble(ts,interval '60' second),
a.web_page
--a.ua_os_name,
having count(1) > 20