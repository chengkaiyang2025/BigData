
create table friend{
    name string,
    friends array<string>,
    children map<string,int>
    address struct<street:string,city:string>
    };
load data inpath '' into table friend;

select cast('1' as int) + 2, 1 + 2;