1、据刘阳光说，截止2021年5月所有报表的格子数在100万左右，
当前使用企业数量为30万左右，但是这100万个格子是分税区的。因此需要统计每家企业一个账期要多少格子，
假如需要1000个格子，则为30亿。
2、据汝丽丽说，大约有10%的企业出于调账、税筹等目的，会在年底改年初的数据。如2021年12账期改2021年1账期的数据。
因此需要保证，单kudu表不能跨年备份。
同时到2021年12月底，格子数量不会翻倍到3倍或4倍。



rowKey:
790891887698513920_2020_12_579996200418148352_579996200439119872_申报表(小规模纳税人适用)_noFloat
qyid_kjnd_kjqj_boxId_version_sheetName_isFloat
企业id，会计年度，会计期间，boxid，version，sheet页名称，是否浮动行：
cell:c1,cell:c2


kudu:
bigint qyid
int kjnd
int kjqj
bitint boxId,
version,
# 需要思考的问题
数据架构
Q1、如何保证数据尽量写入到各个tablet中？
Q2、如何保证表的数据量尽量合理？
Q3、查询方式是否为单指标查询?如只查询C4等
应用架构
Q1、如何解决因为kafka而导致的数据乱序的问题？
Q2、flink-connector版本问题、kudu版本问题是否经过了测试？
技术架构
Q1、flink是否需要使用yarn进行管理？还是使用自建的集群？

# 数据调研
每次写入、更新的时候，发送以下一条message：
areaId?dqbm
boxId
cells-isChange ?NOCHANGE代表？
cells-location 坐标
cells-value 最大值长度是多少？
为什么会有多个cells同时变更？最多有个cell

createTime?java写入的时间？
dzQyId?代账企业id
fetchDataTime? ??createTime与fetchDataTime事件时间
id 840244025863835648_2021_4_778944412830113793_778944412834308097_sheet1 sheet1 就是指标？
parentBoxId 是什么业务含义？查询是否关键

目前线上发送到kafka中是不是轮询？有几个分区
会不会有乱序问题？

如何像测试环境发送数据？
线上与线下的schema是否一致？

每月的数据量在150万-300万之间。
