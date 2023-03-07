# 主要记录新代帐发生的事故
## 20210902
开始时间：13:39，结束时间：13:54
影响范围：个别企业、薪酬、
影响程度：小
说明：刘恋提出司春扬的薪酬模块有问题
截图：<img src="/data/yzf/IdeaWorkSpace/flink-job/nginx-analysis-stream/img/20210902.jpg" style="zoom:15%;" />

事故报告:

## 20210914
开始时间：21:42，结束时间：22:00
影响范围：区域、
截图：<img src="/data/yzf/IdeaWorkSpace/flink-job/nginx-analysis-stream/img/20210914.jpg" style="zoom:15%;" />
事故报告:

## 20210927
开始时间：08:44，结束时间：09:40
日志方499/500时间：08:40
499
影响范围：全国，全模块、非常严重
截图：
<img src="/data/yzf/IdeaWorkSpace/flink-job/nginx-analysis-stream/img/20210927.jpg" style="zoom:15%;" />
事故报告:

## 20210929
开始时间：08:48，结束时间：09:46
影响范围：全国，全模块、非常严重
ng日志 08:40
499
08:47 137
08:48 209 全是499、t.request_time>10,/api/fintax/application/dz/book/count出现最多的、/api/fintax/application/dz/auth/role/userMenu
/api/fintax/application/dz/book/qyName、/api/fintax/application/dz/book/home、首页，
08:49 123
08:51:00 103+/10 -> 146/10s
09:15:10->09:15:30 200+/10s 显著减少到 16/10s
截图：<img src="/data/yzf/IdeaWorkSpace/flink-job/nginx-analysis-stream/img/20210929.jpg" style="zoom:15%;" />
事故报告：
预警开始时间：08:53,08:59很多。


## 20210929
开始时间：17:30，结束时间：18:27
影响范围：全国，全模块、非常严重
截图：
事故报告：
预警开始时间：

## 20211011
开始时间：09:49 ，结束时间：10:55
日志方499/500时间：08:40
499
影响范围：全国，全模块、非常严重
截图：
<img src="/data/yzf/IdeaWorkSpace/flink-job/nginx-analysis-stream/img/20211011.jpg" style="zoom:15%;" />
事故报告:


## 20211011
开始时间：15:05 ，结束时间：15:24
日志方499/500时间：08:40
499
影响范围：全国，全模块、一般
截图：
<img src="/data/yzf/IdeaWorkSpace/flink-job/nginx-analysis-stream/img/20211011_2.jpg" style="zoom:15%;" />
事故报告:


# 目前的核心问题
1、客户成功一般在工作日早上上班后，也就是八点44之后，发现问题。现在要确定事故发生的第一时间，
2、如果能做到在事故发生的第一时间就准确的预警出来，争取出时间。
3、事故发生时候，日志上特征是什么样的。499,->500,499并且request_time>3s的，之后转为500,
    3.1、找到事故发生的前兆：
4、预警规则必须再精确，在数据全量倒放的时候，保证准确预警。
5、预警的时机多关注早上。

接口
http://daizhang99.yunzhangfang.com/api/fintax/application/dz/company/index/892767096287109121
# 结论
系统正常时候的码值数量：
早上8点到9点：
499：每十分钟 最多300左右


```roomsql
-- 查询返回码为499,同时响应时间明显大于5秒的请求，平时工作日为几千条，发现27号、29号数据为几万条，在事故发生的几十分钟内，大量的错误请求。

select substr(t.time_local,1,12),
count(1) from log_ods.new_dz_nginx_log t
where ( time_local like '%/Sep/2021%' )
-- group by t.status;
and t.status ='499'
and t.request_time>5
group by substr(t.time_local,1,12)
order by substr(t.time_local,1,12)
-- having count(1) > 2
-- order by count(1) desc
;
```


```roomsql
-- 事故前兆分析
select t.request,t.time_local,t.request_time,t.status--17到分钟，20到秒，19到10秒
from log_ods.new_dz_nginx_log t
where ( time_local like '29/Sep/2021%' )
and t.status ='499'
and t.request_time>5
and t.request not like '%/api/fintax/application/dz/notice/xx/%'
order by t.time_local
;
```

策略：
1、给出事故时候的代帐公司、用户等。
2、给出所有事故报告。

公开预警策略：
1、各个接口非200、304预警。
2、request_time预警。
3、