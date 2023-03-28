// 创建数据库
create database if not exists yck_database;
// 查看数据库详情，extended，详情
desc database extended yck_database;
// 为数据库添加属性
alter database yck_database set dbproperties ('create-time'='20230326')
drop table if exists yck_database.people;

-- chapter 4 创建表
create table if not exists yck_database.people (


    name string comment "my name",
    salary double comment "salary",
    age int comment "age"
)
    comment "people table"
    partitioned by (dt string)
    stored as parquet -- sequence, textfile, rcfile
--     row format
    location "/apps/data/warehouse/yck_database/people_d"
    tblproperties ("parquet.compression" = "SNAPPY");

select * from yck_database.people;



--  插入数据
insert overwrite table yck_database.people
    partition (dt="20230226")
select "yck",11.11,20
union all select "wxn",12.11,20
union all select "wxn2",13.11,21
union all select "wxn1",12.11,20
union all select "yck",12.11,20
union all select "wxn2",14.11,20
union all select "yck",12.11,20
union all select "wxn",12.11,1
;
-- 临时表：通过 select
drop table if exists yck_database.people_analysis;
create table if not exists yck_database.people_analysis as
select p.name,sum(p.salary),max(p.salary) from yck_database.people p
         where p.salary>1 and p.dt = '20230226'
group by p.name;
select *
from yck_database.people_analysis;

-- 复制表结构
create table if not exists yck_database.people_2 like yck_database.people;

--
desc formatted yck_database.people_2;
alter table yck_database.people_2 set tblproperties ('EXTERNAL' = 'TRUE')