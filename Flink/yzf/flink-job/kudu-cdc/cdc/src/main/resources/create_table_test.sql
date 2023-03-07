
DROP TABLE IF EXISTS test.octopus_qushu_cell_20210707;
CREATE TABLE  test.octopus_qushu_cell_20210707 (
id string not null comment 'hbase中的rowkey',  -- 840244025863835648_2021_4_778944412830113793_778944412834308097_sheet1,
cell_location string not null comment '格子的坐标如A1',
cell_is_change string  comment '格子是否变换',
cell_value string comment '格子的值',
   PRIMARY KEY (id,cell_location)
)
partition by hash(id,cell_location) partitions 14
COMMENT '新代账hbase实时同步指标值表 cell压力测试 '
stored as kudu
TBLPROPERTIES (
'kudu.num_tablet_replicas' = '1'
)