DROP TABLE IF EXISTS test.tmp_yck;
CREATE TABLE  test.tmp_yck (
set_cd string NOT NULL comment 'set分区',
  id int NOT NULL COMMENT '主键 ',
  serial_no int NOT NULL COMMENT '批次号',
  request_ip string  COMMENT '请求IP',
  client_id string DEFAULT NULL,
  client_version string DEFAULT NULL,
  gs_id bigint  COMMENT '公司ID',
  user_id bigint  COMMENT '用户ID',
  login_name string  COMMENT '用户登录名',
  status_code int   COMMENT '状态码',
  remark string DEFAULT NULL COMMENT '备注',
  create_time string   COMMENT '创建时间',
  update_time string   COMMENT '更新时间',
   PRIMARY KEY (set_cd,id,serial_no)
)
partition by hash(set_cd,serial_no,id) partitions 14
COMMENT '任务批次信息'
stored as kudu;
insert into test.tmp_yck (set_cd,serial_no,id) VALUES ('set_f',1,2);

CREATE TABLE  test.tmp_yck (
set_cd string NOT NULL comment 'set分区',
  id bigint NOT NULL COMMENT '主键 ',
  serial_no bigint NOT NULL COMMENT '批次号',
  request_ip string  COMMENT '请求IP',
  client_id string DEFAULT NULL,
  client_version string DEFAULT NULL,
  gs_id bigint  COMMENT '公司ID',
  user_id bigint  COMMENT '用户ID',
  login_name string  COMMENT '用户登录名',
  status_code int   COMMENT '状态码',
  remark string DEFAULT NULL COMMENT '备注',
  create_time string   COMMENT '创建时间',
  update_time string   COMMENT '更新时间',
   PRIMARY KEY (set_cd,id,serial_no)
)
partition by hash(set_cd,serial_no,id) partitions 14
COMMENT '任务批次信息'
stored as kudu;



DROP TABLE IF EXISTS test.octopus_qushu_table_2021q2;
CREATE TABLE  test.octopus_qushu_2021q2_20210706 (
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

insert into test.octopus_qushu_table_2021q3
(
id,
cell_location,
cell_is_change,
cell_value,
area_id,
area_name,
box_id,
create_time,
dz_qy_id,
dz_qy_name,
fetch_data_time,
kjnd,
kjqj,
nsqxdm,
parent_box_id,
qy_id,
qy_name,
sbsz_id,
sheet_name,
system_id
) VALUES (
"840244025863835648_2021_4_778944412830113793_778944412834308097_sheet1",
"A2",
"NOCHANGE",
"1",
"0",
"全国",
"778944412830113793",
"2021-05-132 15:21:50",
"722839913816526849",
"测试",
1620804083847,
"2021",
"4",
"4",
"778941549412327424",
"840244025863835648",
"青岛小企业年报测试",
"3702040002",
"sheet1",
"498812948580401153"
)
