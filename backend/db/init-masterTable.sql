-- 常用审批意见
drop table if exists approve_opininon;
CREATE TABLE approve_opininon (
  id serial PRIMARY KEY,
  approve_operation varchar(64),
  opininon varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table approve_opininon is '常用审批意见';
comment on column approve_opininon.approve_operation is '审批操作';
comment on column approve_opininon.opininon is '审批意见';
comment on column approve_opininon.create_by is '创建者ID';
comment on column approve_opininon.create_at is '创建时间';
comment on column approve_opininon.update_by is '更新者ID';
comment on column approve_opininon.update_at is '更新时间';
comment on column approve_opininon.delete_at is '删除时间';

-- 关键词
drop table if exists opertaion;
CREATE TABLE opertaion (
  id serial PRIMARY KEY,
  name varchar(64),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table opertaion is '关键词';
comment on column opertaion.name is '名称';
comment on column opertaion.create_by is '创建者ID';
comment on column opertaion.create_at is '创建时间';
comment on column opertaion.update_by is '更新者ID';
comment on column opertaion.update_at is '更新时间';
comment on column opertaion.delete_at is '删除时间';

-- 治工具
drop table if exists tool;
CREATE TABLE tool (
  id serial PRIMARY KEY,
  name varchar(64),
  common boolean,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table tool is '治工具';
comment on column tool.name is '名称';
comment on column tool.common is '是否通用';
comment on column tool.remark is '备注';
comment on column tool.create_by is '创建者ID';
comment on column tool.create_at is '创建时间';
comment on column tool.update_by is '更新者ID';
comment on column tool.update_at is '更新时间';
comment on column tool.delete_at is '删除时间';

-- 部品
drop table if exists part;
CREATE TABLE part (
  id serial PRIMARY KEY,
  name varchar(64),
  common boolean,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table part is '部品';
comment on column part.name is '名称';
comment on column part.common is '是否通用';
comment on column part.remark is '备注';
comment on column part.create_by is '创建者ID';
comment on column part.create_at is '创建时间';
comment on column part.update_by is '更新者ID';
comment on column part.update_at is '更新时间';
comment on column part.delete_at is '删除时间';

-- 生产阶段
drop table if exists phase;
CREATE TABLE phase (
  id serial PRIMARY KEY,
  name varchar(64),
  continue_phase_id integer,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table phase is '生产阶段';
comment on column phase.name is '名称';
comment on column phase.continue_phase_id is '沿用阶段';
comment on column phase.remark is '备注';
comment on column phase.create_by is '创建者ID';
comment on column phase.create_at is '创建时间';
comment on column phase.update_by is '更新者ID';
comment on column phase.update_at is '更新时间';
comment on column phase.delete_at is '删除时间';

-- 工位
drop table if exists workstation;
CREATE TABLE workstation (
  id serial PRIMARY KEY,
  name varchar(64),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table workstation is '工位';
comment on column workstation.name is '名称';
comment on column workstation.remark is '备注';
comment on column workstation.create_by is '创建者ID';
comment on column workstation.create_at is '创建时间';
comment on column workstation.update_by is '更新者ID';
comment on column workstation.update_at is '更新时间';
comment on column workstation.delete_at is '删除时间';

-- 工位类型节点
drop table if exists workstation_type_node;
CREATE TABLE workstation_type_node (
  id serial PRIMARY KEY,
  name varchar(64),
  workstation_id integer,
  remark varchar(512),
  parent_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table workstation_type_node is '工位类型节点';
comment on column workstation_type_node.name is '名称';
comment on column workstation_type_node.workstation_id is '工位ID';
comment on column workstation_type_node.parent_id is '父节点ID';
comment on column workstation_type_node.remark is '备注';
comment on column workstation_type_node.create_by is '创建者ID';
comment on column workstation_type_node.create_at is '创建时间';
comment on column workstation_type_node.update_by is '更新者ID';
comment on column workstation_type_node.update_at is '更新时间';
comment on column workstation_type_node.delete_at is '删除时间';

-- 工位类型
drop table if exists workstation_type;
CREATE TABLE workstation_type (
  id serial PRIMARY KEY,
  name varchar(64),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table workstation_type is '工位类型';
comment on column workstation_type.name is '名称';
comment on column workstation_type.remark is '备注';
comment on column workstation_type.create_by is '创建者ID';
comment on column workstation_type.create_at is '创建时间';
comment on column workstation_type.update_by is '更新者ID';
comment on column workstation_type.update_at is '更新时间';
comment on column workstation_type.delete_at is '删除时间';

-- 报表
drop table if exists report;
CREATE TABLE report (
  id serial PRIMARY KEY,
  name varchar(64),
  form_code varchar(64),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report is '报表';
comment on column report.name is '名称';
comment on column report.form_code is '空Form标准编号';
comment on column report.remark is '备注';
comment on column report.create_by is '创建者ID';
comment on column report.create_at is '创建时间';
comment on column report.update_by is '更新者ID';
comment on column report.update_at is '更新时间';
comment on column report.delete_at is '删除时间';

-- 报表组
drop table if exists report_group;
CREATE TABLE report_group (
  id serial PRIMARY KEY,
  name varchar(64),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_group is '报表组';
comment on column report_group.name is '名称';
comment on column report_group.remark is '备注';
comment on column report_group.create_by is '创建者ID';
comment on column report_group.create_at is '创建时间';
comment on column report_group.update_by is '更新者ID';
comment on column report_group.update_at is '更新时间';
comment on column report_group.delete_at is '删除时间';

-- 机种
drop table if exists model;
CREATE TABLE model (
  id serial PRIMARY KEY,
  model_series_id integer,
  name varchar(64),
  dept_id integer,
  code varchar(64),
  WS_time date,
  ES_time date,
  AMP_time date,
  MP_time date,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table model is '机种';
comment on column model.model_series_id is '机种系列ID';
comment on column model.name is '名称';
comment on column model.dept_id is '部门ID';
comment on column model.code is '型号';
comment on column model.WS_time is 'WS时间';
comment on column model.ES_time is 'ES时间';
comment on column model.AMP_time is 'AMP时间';
comment on column model.MP_time is 'MP时间';
comment on column model.remark is '备注';
comment on column model.create_by is '创建者ID';
comment on column model.create_at is '创建时间';
comment on column model.update_by is '更新者ID';
comment on column model.update_at is '更新时间';
comment on column model.delete_at is '删除时间';

-- 机种系列
drop table if exists model_series;
CREATE TABLE model_series (
  id serial PRIMARY KEY,
  name varchar(64),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table model_series is '机种系列';
comment on column model_series.name is '名称';
comment on column model_series.remark is '备注';
comment on column model_series.create_by is '创建者ID';
comment on column model_series.create_at is '创建时间';
comment on column model_series.update_by is '更新者ID';
comment on column model_series.update_at is '更新时间';
comment on column model_series.delete_at is '删除时间';

-- 组织机构关键词关系
drop table if exists dept_operation_rela;
CREATE TABLE dept_operation_rela (
  id serial PRIMARY KEY,
  dept_id integer,
  operation_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table dept_operation_rela is '组织机构关键词关系';
comment on column dept_operation_rela.dept_id is '组织机构ID';
comment on column dept_operation_rela.operation_id is '关键词ID';
comment on column dept_operation_rela.create_by is '创建者ID';
comment on column dept_operation_rela.create_at is '创建时间';
comment on column dept_operation_rela.update_by is '更新者ID';
comment on column dept_operation_rela.update_at is '更新时间';
comment on column dept_operation_rela.delete_at is '删除时间';

-- 组织机构工位关系
drop table if exists dept_workstation_rela;
CREATE TABLE dept_workstation_rela (
  id serial PRIMARY KEY,
  dept_id integer,
  workstation_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table dept_workstation_rela is '组织机构工位关系';
comment on column dept_workstation_rela.dept_id is '组织机构ID';
comment on column dept_workstation_rela.workstation_id is '工位ID';
comment on column dept_workstation_rela.create_by is '创建者ID';
comment on column dept_workstation_rela.create_at is '创建时间';
comment on column dept_workstation_rela.update_by is '更新者ID';
comment on column dept_workstation_rela.update_at is '更新时间';
comment on column dept_workstation_rela.delete_at is '删除时间';

-- 机种治工具关系
drop table if exists model_tool_rela;
CREATE TABLE model_tool_rela (
  id serial PRIMARY KEY,
  modelId integer,
  toolId integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table model_tool_rela is '机种治工具关系';
comment on column model_tool_rela.modelId is '机种ID';
comment on column model_tool_rela.toolId is '治工具ID';
comment on column model_tool_rela.create_by is '创建者ID';
comment on column model_tool_rela.create_at is '创建时间';
comment on column model_tool_rela.update_by is '更新者ID';
comment on column model_tool_rela.update_at is '更新时间';
comment on column model_tool_rela.delete_at is '删除时间';

-- 机种部品关系
drop table if exists model_part_rela;
CREATE TABLE model_part_rela (
  id serial PRIMARY KEY,
  model_id integer,
  part_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table model_part_rela is '机种部品关系';
comment on column model_part_rela.model_id is '机种ID';
comment on column model_part_rela.part_id is '部品ID';
comment on column model_part_rela.create_by is '创建者ID';
comment on column model_part_rela.create_at is '创建时间';
comment on column model_part_rela.update_by is '更新者ID';
comment on column model_part_rela.update_at is '更新时间';
comment on column model_part_rela.delete_at is '删除时间';

-- 报表组报表关系
drop table if exists report_group_report_rela;
CREATE TABLE report_group_report_rela (
  id serial PRIMARY KEY,
  report_group_id integer,
  report_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_group_report_rela is '报表组报表关系';
comment on column report_group_report_rela.report_group_id is '报表组ID';
comment on column report_group_report_rela.report_id is '报表ID';
comment on column report_group_report_rela.create_by is '创建者ID';
comment on column report_group_report_rela.create_at is '创建时间';
comment on column report_group_report_rela.update_by is '更新者ID';
comment on column report_group_report_rela.update_at is '更新时间';
comment on column report_group_report_rela.delete_at is '删除时间';

-- 常用指标组合
drop table if exists measure_group;
CREATE TABLE measure_group (
  id serial PRIMARY KEY,
  code varchar(64),
  a0 varchar(1),
  b0 varchar(1),
  g0 varchar(1),
  a1 varchar(1),
  b1 varchar(1),
  p0 varchar(1),
  m0 varchar(1),
  x0 varchar(1),
  i0 varchar(1),
  a2 varchar(1),
  b2 varchar(1),
  p1 varchar(1),
  a3 varchar(1),
  deptId integer,
  usedCount integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table measure_group is '常用指标组合';
comment on column measure_group.code is '编码';
comment on column measure_group.a0 is 'A0';
comment on column measure_group.b0 is 'B0';
comment on column measure_group.g0 is 'G0';
comment on column measure_group.a1 is 'A1';
comment on column measure_group.b1 is 'B1';
comment on column measure_group.p0 is 'P0';
comment on column measure_group.m0 is 'M0';
comment on column measure_group.x0 is 'X0';
comment on column measure_group.i0 is 'I0';
comment on column measure_group.a2 is 'A2';
comment on column measure_group.b2 is 'B2';
comment on column measure_group.p1 is 'P1';
comment on column measure_group.a3 is 'A3';
comment on column measure_group.deptId is '组织机构ID';
comment on column measure_group.usedCount is '使用次数统计';
comment on column measure_group.create_by is '创建者ID';
comment on column measure_group.create_at is '创建时间';
comment on column measure_group.update_by is '更新者ID';
comment on column measure_group.update_at is '更新时间';
comment on column measure_group.delete_at is '删除时间';

-- 手顺组合
drop table if exists opertaion_group;
CREATE TABLE opertaion_group (
  id serial PRIMARY KEY,
  code varchar(64),
  deptId integer,
  usedCount integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table opertaion_group is '手顺组合';
comment on column opertaion_group.code is '编码';
comment on column opertaion_group.deptId is '组织机构ID';
comment on column opertaion_group.usedCount is '使用次数统计';
comment on column opertaion_group.create_by is '创建者ID';
comment on column opertaion_group.create_at is '创建时间';
comment on column opertaion_group.update_by is '更新者ID';
comment on column opertaion_group.update_at is '更新时间';
comment on column opertaion_group.delete_at is '删除时间';

-- 手顺
drop table if exists operation_group_operation;
CREATE TABLE operation_group_operation (
  id serial PRIMARY KEY,
  operation_group_id integer,
  seq_number integer,
  operation varchar(256),
  measures varchar(17),
  frequency integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table operation_group_operation is '手顺';
comment on column operation_group_operation.operation_group_id is '手顺组合ID';
comment on column operation_group_operation.seq_number is '序号';
comment on column operation_group_operation.operation is '手顺';
comment on column operation_group_operation.measures is '指标';
comment on column operation_group_operation.frequency is '频度';
comment on column operation_group_operation.create_by is '创建者ID';
comment on column operation_group_operation.create_at is '创建时间';
comment on column operation_group_operation.update_by is '更新者ID';
comment on column operation_group_operation.update_at is '更新时间';
comment on column operation_group_operation.delete_at is '删除时间';

-- 分析表
drop table if exists work_book;
CREATE TABLE work_book (
  id serial PRIMARY KEY,
  dept_id integer,
  STLST varchar(8),
  model_id integer,
  destinations varchar(128),
  phase_id integer,
  workstation_id integer,
  work_name varchar(128),
  version_number varchar(32),
  maker_id integer,
  maked_at timestamp,
  continue_from_id integer,
  time_value decimal(18, 5),
  TMU decimal(18, 5),
  second_convert decimal(18, 5),
  remark text,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table work_book is '分析表';
comment on column work_book.dept_id is '组织机构ID';
comment on column work_book.STLST is 'ST/LST';
comment on column work_book.model_id is '机种ID';
comment on column work_book.destinations is '仕向';
comment on column work_book.phase_id is '生产阶段ID';
comment on column work_book.workstation_id is '工位ID';
comment on column work_book.work_name is '作业名';
comment on column work_book.version_number is '版本号';
comment on column work_book.maker_id is '制表人ID';
comment on column work_book.maked_at is '制表日期';
comment on column work_book.continue_from_id is '沿用来源ID';
comment on column work_book.time_value is '时间值';
comment on column work_book.TMU is 'TMU';
comment on column work_book.second_convert is '秒换算';
comment on column work_book.remark is '备注';
comment on column work_book.create_by is '创建者ID';
comment on column work_book.create_at is '创建时间';
comment on column work_book.update_by is '更新者ID';
comment on column work_book.update_at is '更新时间';
comment on column work_book.delete_at is '删除时间';

-- 分析表明细
drop table if exists work_operations;
CREATE TABLE work_operations (
  id serial PRIMARY KEY,
  seq_number integer,
  work_book_id integer,
  version varchar(64),
  operation varchar(256),
  measures varchar(17),
  frequency integer,
  time_value decimal(18, 5),
  TMU decimal(18, 5),
  second_convert decimal(18, 5),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table work_operations is '分析表明细';
comment on column work_operations.seq_number is '序号';
comment on column work_operations.work_book_id is '分析表ID';
comment on column work_operations.version is '版本信息';
comment on column work_operations.operation is '手顺';
comment on column work_operations.measures is '指标';
comment on column work_operations.frequency is '频度';
comment on column work_operations.time_value is '时间值';
comment on column work_operations.TMU is 'TMU';
comment on column work_operations.second_convert is '秒换算';
comment on column work_operations.remark is '备注';
comment on column work_operations.create_by is '创建者ID';
comment on column work_operations.create_at is '创建时间';
comment on column work_operations.update_by is '更新者ID';
comment on column work_operations.update_at is '更新时间';
comment on column work_operations.delete_at is '删除时间';