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
  is_common boolean,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table tool is '治工具';
comment on column tool.name is '名称';
comment on column tool.is_common is '是否通用';
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
  is_common boolean,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table part is '部品';
comment on column part.name is '名称';
comment on column part.is_common is '是否通用';
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
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table workstation_type_node is '工位类型节点';
comment on column workstation_type_node.name is '名称';
comment on column workstation_type_node.workstation_id is '工位ID';
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