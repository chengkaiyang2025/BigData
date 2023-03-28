drop table yck_database.emp;
create table if not exists yck_database.emp( empno int,
                                ename string, job string, mgr int,
                                hiredate string, sal double,
                                comm double, deptno int)
    row format delimited fields terminated by ',';
insert overwrite table yck_database.emp
select 369,"SMITH","CLERK",7902,1980-12-17 ,800.00,20,20
union all select 7499,"ALLEN","SALESMAN",7698,1981-2-20,1600.00,300.00,30
union all select 7521,"WARD","SALESMAN",7698,1981-2-22,1250.00,500.00,30
union all select 7566,"JONES","MANAGER ",7839,1981-4-2,2975.00,500.00,20
union all select 7654,"MARTIN","SALESMAN",7698,1981-9-28,1250.00,1400.00,30
union all select 7698,"BLAKE","MANAGER ",7839,1981-5-1,2850.00,1400.00,30
union all select 7782,"CLARK","MANAGER ",7839,1981-6-9,2450.00,1400.00,10
union all select 7788,"SCOTT","ANALYST ",7566,1987-4-19,3000.00,1400.00,20
union all select 7839,"KING","PRESIDENT",333,1981-11-17,5000.00,1410.00,10
union all select 7844,"TURNER","SALESMAN",7698,1981-9-8,1500.00,0.00,30
union all select 7876,"ADAMS","CLERK",7788,1987-5-23,1100.00,140.00,20
union all select 7900,"JAMES","CLERK",7698,1981-12-3,950.00,400.00,30
union all select 7902,"FORD","ANALYST ",7566,1981-12-3,3000.00,1500.00,20
union all select 7934,"MILLER","CLERK",7782,1982-1-23,1300.00,3100.00,10;
-- chapter 6

select * from yck_database.emp;

create table if not exists yck_database.dept( deptno int,
                                              dname string, loc int
)
    row format delimited fields terminated by ',';
insert overwrite table yck_database.dept
select 10,"ACCOUNTING",1700
union all select 20,"RESEARCH",1800
union all select 30,"SALES",1900
union all select 40,"OPERATIONS",1700;


select d.deptno,d.dname,c.avg_sal from yck_database.dept d
    left join
    (select e.deptno,avg(e.sal) as avg_sal from yck_database.emp  e group by  e.deptno
     having avg(e.sal)  > 2000
    ) c
    on d.deptno = c.deptno;
-- 排序：
-- 全局排序只有一个 Reducer，order by

select * from yck_database.emp e order by e.sal desc;

-- sort by 与 distribute by 配合，放到 reducer 中，在 reducer 总保持一致。
set mapreduce.job.reduces = 4;
select e.ename,e.job,e.sal from yck_database.emp e
    distribute by e.job sort by e.sal;

select d.dname,e.ename,e.job,e.sal
from yck_database.emp e
left join yck_database.dept d on e.deptno = d.deptno
distribute by e.deptno sort by e.sal desc;

--
select d.dname,a.* from
    (select e.ename,e.job,e.sal,e.deptno
     from yck_database.emp e
         distribute by e.deptno sort by e.sal desc) a
        left join yck_database.dept d on a.deptno = d.deptno;



-- chapter  8 列统计
select
    sum(case when e.job = 'MANAGER' then 1 else 0 end) as MANAGER,
    sum(case when e.job = 'SALESMAN' then 1 else 0 end) as SALESMAN,
from yck_database.emp e ;

-- 8.1、collect_list 聚合。将列聚合为数组。group by
select
    e.deptno,
    concat_ws("|", collect_list(e.ename))
from yck_database.emp e
group by e.deptno;

-- 8.2、collect_set 聚合，将列聚合为set。group by
select
    e.deptno,
    concat_ws("|",collect_set(e.job))
from yck_database.emp e
group by e.deptno;

-- 8.3、LATERAL view explode 将 set 打散
    select deptno,job_type from
    (
        select
            e.deptno,
            concat_ws(",",collect_set(e.job)) as j

        from yck_database.emp e
        group by e.deptno
    ) a
        LATERAL VIEW
            explode(split(j,",")) job_type as job_type;
--
-- 8.3、LATERAL view explode 将 list 打散
select name,place
from
    (
select "yck" as name,"BIT,YZJ,YZF" as location
union all select "wsn" as name,"shf,abc school" as location)
a lateral view
        explode(split(location,","))  place as place;
drop table yck_database.movie;
create table if not exists yck_database.movie(
    name string,
    category string,
    producer string
)
row format delimited fields terminated by ',';

insert overwrite table yck_database.movie
select "Forest Gump","history|human|sports","China|US|JP"
union all select "Star War","sci-fy|history","US|Canada"
union all select "Star War 2","human|sci-fy","China|Canada";

select
    m.name,c
from yck_database.movie m
         lateral view
             explode(split(m.category,'\\|')) c1 as c;
select
    m.name,pr.c1,pr.c2
from yck_database.movie m
         lateral view explode(split(m.producer,'\\|')) pr as c1
         lateral view explode(split(m.category,'\\|')) pr as c2


-- 8.5 ！开窗函数 + ！排名！ + ！前百分比！ + ！位移！
-- partition by

select
    e.ename,d.dname,e.sal,
    row_number() over (partition by d.dname order by e.sal desc)
from yck_database.emp e left join yck_database.dept d
                                  on e.deptno = d.deptno;


create table yck_database.business( name string,
                       orderdate string,
                       cost int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
insert overwrite table yck_database.business
select "jack","2017-01-01",10 union all
select "tony","2017-01-02",15 union all
select "jack","2017-02-03",23 union all
select "tony","2017-01-04",29 union all
select "jack","2017-01-05",46 union all
select "jack","2017-04-06",42 union all
select "tony","2017-01-07",50 union all
select "jack","2017-01-08",55 union all
select "mart","2017-04-08",62 union all
select "mart","2017-04-09",68 union all
select "neil","2017-05-10",12 union all
select "mart","2017-04-11",75 union all
select "neil","2017-06-12",80 union all
select "mart","2017-04-13",94;
--（1）查询在 2017 年 4 月份购买过的顾客及总人数
select
    t.name,count(1) over()
from yck_database.business
t where t.orderdate like '2017-04%'
group by t.name;
--（2）查询顾客的购买明细及它的购买总额
select
    t.name,t.orderdate,t.cost,sum(t.cost) over(partition by t.name)
from yck_database.business t;
--（3）上述的场景, 将每个顾客的cost 按照日期进行累加

-- CURRENT ROW：当前行
-- n PRECEDING：往前 n 行数据n FOLLOWING：往后 n 行数据UNBOUNDED：起点，
-- UNBOUNDED PRECEDING 表示从前面的起点，
-- UNBOUNDED FOLLOWING 表示到后面的终点

select name,orderdate,cost,
       sum(cost) over() as sample1,--所有行相加
       sum(cost) over(partition by name) as sample2,--按 name 分组，组内数据相加sum(cost) over(partition by name order by orderdate) as sample3,--按 name 分组，组内数据累加
       sum(cost) over(partition by name order by orderdate rows between UNBOUNDED PRECEDING and current row ) as sample4 ,--和 sample3 一样,由起点到当前行的聚合
       sum(cost) over(partition by name order by orderdate rows between 1 PRECEDING and current row) as sample5, --当前行和前面一行做聚合sum(cost) over(partition by name order by orderdate rows between 1 PRECEDING AND 1 FOLLOWING ) as sample6,--当前行和前边一行及后面一行
       sum(cost) over(partition by name order by orderdate rows between current row and UNBOUNDED FOLLOWING ) as sample7 --当前行及后面所有行
from yck_database.business;
-- LAG(col,n,default_val)：往前第 n 行数据
select name,orderdate,cost,
       lag(orderdate,1,'1900-01-01') over(partition by name order by orderdate ) as time1,
       lag(orderdate,2) over (partition by name order by orderdate) as time2
from yck_database.business;
-- LEAD(col,n, default_val)：往后第 n 行
--（4）查询每个顾客上次的购买时间
-- NTILE(n)：把有序窗口的行分发到指定数据的组中，各个组有编号，编号从 1 开始，对于每一行，NTILE 返回此行所属的组的编号。注意：n 必须为 int 类型。
--（5）查询前 20%时间的订单信息


-- 分区，增加分区、删除分区；
-- 分桶

-- 10.4.1小表大表 Join（MapJOIN） 开启 map join
-- 10.4.2大表 Join 大表 关联字段的空key 过滤。join nvl(a,rand())
-- 10.4.3Group By 开启Map端join，combine，也就是 cobin
-- 10.4.4Count(Distinct) 去重统计 用group by代替key by
-- 10.4.6行列过滤，子查询只拿需要的列
