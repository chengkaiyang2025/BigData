DROP TABLE IF EXISTS octopus_ods.octopus_qushu_table_202106;
CREATE TABLE  octopus_ods.octopus_qushu_table_202106 (
id string not null comment 'hbase中的rowkey',  -- 840244025863835648_2021_4_778944412830113793_778944412834308097_sheet1,
cell_location string not null comment '格子的坐标如A1',
cell_is_change string  comment '格子是否变换',
cell_value string comment '格子的值',
area_id string comment '区域id',  -- 0,
area_name string comment '区域名称',  --全国,
box_id string comment 'boxid', -- 778944412830113793,
create_time string comment '创建时间',  -- 2021-05-13215:21:50,
dz_qy_id string comment '代账公司id',  -- 722839913816526849,
dz_qy_name string comment '代账公司名称',  -- 测试,
fetch_data_time bigint comment '取数时间',  -- 1620804083847,
kjnd string comment '会计年度',  -- 2021,
kjqj string comment '会计期间',  -- 4,
nsqxdm string comment '纳税期限代码',  -- 4,
parent_box_id string comment '上级boxid',  -- 778941549412327424,
qy_id string comment '企业id',  -- 840244025863835648,
qy_name string comment '企业名称',  -- 青岛小企业年报测试,
sbsz_id string comment '申报税种id',  -- 3702040002,
sheet_name string comment 'sheet页名称',  -- sheet1,
system_id string comment '',  -- 4.98813_e+17
   PRIMARY KEY (id,cell_location)
)
partition by hash(id,cell_location) partitions 14
COMMENT '新代账hbase实时同步指标值表 2021第2季度'
stored as kudu;




DROP TABLE IF EXISTS octopus_ods.octopus_qushu_table_202105;
CREATE TABLE  octopus_ods.octopus_qushu_table_202105 (
id string not null comment 'hbase中的rowkey',  -- 840244025863835648_2021_4_778944412830113793_778944412834308097_sheet1,
cell_location string not null comment '格子的坐标如A1',
cell_is_change string  comment '格子是否变换',
cell_value string comment '格子的值',
area_id string comment '区域id',  -- 0,
area_name string comment '区域名称',  --全国,
box_id string comment 'boxid', -- 778944412830113793,
create_time string comment '创建时间',  -- 2021-05-13215:21:50,
dz_qy_id string comment '代账公司id',  -- 722839913816526849,
dz_qy_name string comment '代账公司名称',  -- 测试,
fetch_data_time bigint comment '取数时间',  -- 1620804083847,
kjnd string comment '会计年度',  -- 2021,
kjqj string comment '会计期间',  -- 4,
nsqxdm string comment '纳税期限代码',  -- 4,
parent_box_id string comment '上级boxid',  -- 778941549412327424,
qy_id string comment '企业id',  -- 840244025863835648,
qy_name string comment '企业名称',  -- 青岛小企业年报测试,
sbsz_id string comment '申报税种id',  -- 3702040002,
sheet_name string comment 'sheet页名称',  -- sheet1,
system_id string comment '',  -- 4.98813_e+17
   PRIMARY KEY (id,cell_location)
)
partition by hash(id,cell_location) partitions 14
COMMENT '新代账hbase实时同步指标值表 2021第2季度'
stored as kudu;