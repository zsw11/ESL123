-- Collection - 工位时间表
drop table if exists collection_station_time;
CREATE TABLE collection_station_time (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  remark text,
  model_id integer,
  phase_id integer,
  destinations varchar(128),
  comfirm_by integer,
  in_charge_by integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_station_time is 'Collection - 工位时间表';
comment on column collection_station_time.dept_id is '组织机构ID';
comment on column collection_station_time.title is '标题';
comment on column collection_station_time.sheet_name is 'Sheet名称';
comment on column collection_station_time.model_id is '机种ID';
comment on column collection_station_time.phase_id is '生产阶段ID';
comment on column collection_station_time.destinations is '仕向';
comment on column collection_station_time.comfirm_by is '确认ID';
comment on column collection_station_time.in_charge_by is '承认ID';
comment on column collection_station_time.remark is '备注';
comment on column collection_station_time.create_by is '创建者ID';
comment on column collection_station_time.create_at is '创建时间';
comment on column collection_station_time.update_by is '更新者ID';
comment on column collection_station_time.update_at is '更新时间';
comment on column collection_station_time.delete_at is '删除时间';

-- Collection - 工位时间表子表
drop table if exists collection_station_time_item;
CREATE TABLE collection_station_time_item (
  id serial PRIMARY KEY,
  collection_station_time_id integer,
  sub boolean;
  station_name varchar(64),
  work_name varchar(64),
  LST_value decimal(18, 5),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_station_time_item is 'Collection - 工位时间表子表';
comment on column collection_station_time_item.collection_station_time_id is '工作时间表ID';
comment on column collection_station_time_item.sub is '是否是sub';
comment on column collection_station_time_item.station_name is '工位名称';
comment on column collection_station_time_item.work_name is '作业名';
comment on column collection_station_time_item.LST_value is 'LST时间（秒）';
comment on column collection_station_time_item.create_by is '创建者ID';
comment on column collection_station_time_item.create_at is '创建时间';
comment on column collection_station_time_item.update_by is '更新者ID';
comment on column collection_station_time_item.update_at is '更新时间';
comment on column collection_station_time_item.delete_at is '删除时间';


-- Collection - MOST Value 表
drop table if exists collection_most_value;
CREATE TABLE collection_most_value (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  first_column_name varchar(128),
  sheet_name varchar(128),
  remark text,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_most_value is 'Collection - MOST Value 表';
comment on column collection_most_value.dept_id is '组织机构ID';
comment on column collection_most_value.title is '标题';
comment on column collection_most_value.first_column_name is '组立职场名称';
comment on column collection_most_value.sheet_name is 'Sheet名称';
comment on column collection_most_value.remark is '备注';
comment on column collection_most_value.create_by is '创建者ID';
comment on column collection_most_value.create_at is '创建时间';
comment on column collection_most_value.update_by is '更新者ID';
comment on column collection_most_value.update_at is '更新时间';
comment on column collection_most_value.delete_at is '删除时间';

-- Collection - MOST Value 表子表
drop table if exists collection_most_value_item;
CREATE TABLE collection_most_value_item (
  id serial PRIMARY KEY,
  collection_most_value_id integer,
  type varchar(32),
  work_name varchar(64),
  total decimal(10, 2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_most_value_item is 'Collection - MOST Value 表';
comment on column collection_most_value_item.collection_most_value_id is 'Most Value表ID';
comment on column collection_most_value_item.type is '类型:Sub/Man/测试、检查/捆包';
comment on column collection_most_value_item.work_name is '作业名';
comment on column collection_most_value_item.total is '耗时（Min）';
comment on column collection_most_value_item.create_by is '创建者ID';
comment on column collection_most_value_item.create_at is '创建时间';
comment on column collection_most_value_item.update_by is '更新者ID';
comment on column collection_most_value_item.update_at is '更新时间';
comment on column collection_most_value_item.delete_at is '删除时间';

-- Collection - Revision History 表
drop table if exists collection_revision_history;
CREATE TABLE collection_revision_history (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  model_id integer,
  destinations varchar(128),
  comfirm_by integer,
  in_charge_by integer,
  factory varchar(64),
  month_result timestamp,
  rev_No varchar(64),
  last_ST_name varchar(64),
  current_ST_name varchar(64),
  last_LST_name varchar(64),
  current_LST_name varchar(64),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_revision_history is 'Collection - Revision History 表';
comment on column collection_revision_history.dept_id is '组织机构ID';
comment on column collection_revision_history.title is '标题';
comment on column collection_revision_history.sheet_name is 'Sheet名称';
comment on column collection_revision_history.model_id is '机种ID';
comment on column collection_revision_history.destinations is '仕向';
comment on column collection_revision_history.comfirm_by is '确认ID';
comment on column collection_revision_history.in_charge_by is '承认ID';
comment on column collection_revision_history.factory is '制造工厂';
comment on column collection_revision_history.month_result is '发行日';
comment on column collection_revision_history.rev_No is '版本号';
comment on column collection_revision_history.last_ST_name is '上一版本ST名称';
comment on column collection_revision_history.current_ST_name is '当前版本ST名称';
comment on column collection_revision_history.last_LST_name is '上一版本LST名称';
comment on column collection_revision_history.current_LST_name is '当前版本LST名称';
comment on column collection_revision_history.create_by is '创建者ID';
comment on column collection_revision_history.create_at is '创建时间';
comment on column collection_revision_history.update_by is '更新者ID';
comment on column collection_revision_history.update_at is '更新时间';
comment on column collection_revision_history.delete_at is '删除时间';

-- Collection - Revision History 表子表
drop table if exists collection_revision_history_item;
CREATE TABLE collection_revision_history_item (
  id serial PRIMARY KEY,
  collection_revision_history_id integer,
  name varchar(64),
  remark varchar(128),
  last_ST_time decimal(10, 2),
  current_ST_time decimal(10, 2),
  ST_interval decimal(10, 2),
  last_LST_time decimal(10, 2),
  current_LST_time decimal(10, 2),
  LST_interval decimal(10, 2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_revision_history_item is 'Collection - Revision History 表子表';
comment on column collection_revision_history_item.collection_revision_history_id is 'Revision History 表ID';
comment on column collection_revision_history_item.name is '工程名';
comment on column collection_revision_history_item.remark is '改訂履歴';
comment on column collection_revision_history_item.last_ST_time is '上一ST版本耗时';
comment on column collection_revision_history_item.current_ST_time is '当前版本ST耗时';
comment on column collection_revision_history_item.ST_interval is 'ST版本耗时差异值';
comment on column collection_revision_history_item.last_LST_time is '上一LST版本耗时';
comment on column collection_revision_history_item.current_LST_time is '当前版本LST耗时';
comment on column collection_revision_history_item.LST_interval is 'LST版本耗时差异值';
comment on column collection_revision_history_item.create_by is '创建者ID';
comment on column collection_revision_history_item.create_at is '创建时间';
comment on column collection_revision_history_item.update_by is '更新者ID';
comment on column collection_revision_history_item.update_at is '更新时间';
comment on column collection_revision_history_item.delete_at is '删除时间';

-- Collection - Compare表
drop table if exists collection_compare;
CREATE TABLE collection_compare (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  model_id integer,
  phase_id integer,
  destinations varchar(128),
  comfirm_by integer,
  in_charge_by integer,
  first_column_name varchar(64),
  last_version_name varchar(64),
  current_version_name varchar(64),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_compare is 'Collection - Compare表';
comment on column collection_compare.dept_id is '组织机构ID';
comment on column collection_compare.title is '标题';
comment on column collection_compare.sheet_name is 'Sheet名称';
comment on column collection_compare.model_id is '机种ID';
comment on column collection_compare.phase_id is '生产阶段ID';
comment on column collection_compare.destinations is '仕向';
comment on column collection_compare.comfirm_by is '确认ID';
comment on column collection_compare.in_charge_by is '承认ID';
comment on column collection_compare.first_column_name is '组立职场名称';
comment on column collection_compare.last_version_name is '上一版本名称';
comment on column collection_compare.current_version_name is '当前版本名称';
comment on column collection_compare.create_by is '创建者ID';
comment on column collection_compare.create_at is '创建时间';
comment on column collection_compare.update_by is '更新者ID';
comment on column collection_compare.update_at is '更新时间';
comment on column collection_compare.delete_at is '删除时间';

-- Collection - Compare表子表
drop table if exists collection_compare_item;
CREATE TABLE collection_compare_item (
  id serial PRIMARY KEY,
  collection_compare_id integer,
  sub boolean,
  work_name varchar(128),
  unit_name varchar(64),
  last_value decimal(10, 2),
  current_value decimal(10, 2),
  remark varchar(64),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table collection_compare_item is 'Collection - Compare表子表';
comment on column collection_compare_item.collection_compare_id is 'compare表ID';
comment on column collection_compare_item.sub is '是否是Sub';
comment on column collection_compare_item.work_name is '工位名';
comment on column collection_compare_item.unit_name is '组合名称';
comment on column collection_compare_item.last_value is '上一版本耗时';
comment on column collection_compare_item.current_value is '当前版本耗时';
comment on column collection_compare_item.remark is '备注';
comment on column collection_compare_item.create_by is '创建者ID';
comment on column collection_compare_item.create_at is '创建时间';
comment on column collection_compare_item.update_by is '更新者ID';
comment on column collection_compare_item.update_at is '更新时间';
comment on column collection_compare_item.delete_at is '删除时间';

