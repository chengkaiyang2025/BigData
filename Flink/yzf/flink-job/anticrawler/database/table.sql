create database if not exists anticrawler collate utf8mb4_bin;

create table if not exists nginx_log_anticrawler_formatter
(
	cookie varchar(2000) null comment 'cookie',
	fields_ip varchar(200) null comment 'set的ip',
	fields_set varchar(200) null comment 'set',
	nginx varchar(200) null comment 'set',
	userId varchar(50) null comment '用户id',
	userName varchar(100) null comment '用户名称',
	request varchar(1000) null comment '请求链接',
	gsId varchar(25) null comment '公司id',
	proxy_add_x_forwarded_for varchar(500) null comment '代理',
	x_forwarded_for varchar(500) null comment '代理',
	http_referer varchar(1000) null comment 'httprefer',
	http_user_agent varchar(1000) null,
	phone varchar(25) null comment '用户名手机号',
	remote_addrx varchar(25) null comment '用户的ip',
	request_base_url varchar(500) null comment '请求的链接',
	request_params varchar(500) null comment '请求的参数',
	request_web_page varchar(500) null comment '请求的页面',
	status varchar(50) null comment '网页返回码',
	time_local varchar(50) null comment '用户访问时间',
	create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
comment 'nginx清洗后的日志';

create table if not exists nginx_log_anticrawler_request_cnt
(
	gsId varchar(50) null comment '公司id',
	lastTime varchar(50) null comment 'Flink开窗时间',
	remote_addrx varchar(50) null comment '用户访问页面的ip',
	request_cnt int null comment '访问的计数',
	request_web_page varchar(500) null comment '请求页面',
	uA_CNT int null comment '用户访问页面时候的UserAgent个数',
	userId varchar(200) null comment '用户id',
	userName varchar(200) null comment '用户名称',
	create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
comment 'Flink结果表，由nifi写入。统计请求数量';

