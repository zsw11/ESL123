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
  stlst varchar(128),
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
  sub boolean,
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
  model_id integer,
  phase_id integer,
  stlst varchar(128),
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
  phase_id varchar(128),
  stlst varchar(128),
  comfirm_by integer,
  in_charge_by integer,
  factory varchar(64),
  month_result timestamp,
  Rev_No varchar(64),
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
comment on column collection_revision_history.Rev_No is '版本号';
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
  stlst  varchar(128),
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


-- Report - Total表
drop table if exists report_total;
CREATE TABLE report_total (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  model_id integer,
  phase_id integer,
  month_result timestamp,
  destinations varchar(128),
  stlst varchar(128),
  cotegory varchar(64),
  mecha varchar(64),
  R_code varchar(64),
  ST_Rev varchar(64),
  LST_Rev varchar(64),
  allowance_rate decimal(10, 2),
  comfirm_by integer,
  in_charge_by integer,
  type varchar(32),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_total is 'Report - Total表';
comment on column report_total.dept_id is '组织机构ID';
comment on column report_total.title is '标题';
comment on column report_total.sheet_name is 'Sheet名称';
comment on column report_total.model_id is '机种ID';
comment on column report_total.month_result is '发行日';
comment on column report_total.destinations is '仕向';
comment on column report_total.cotegory is '类别';
comment on column report_total.mecha is 'mecha';
comment on column report_total.R_code is 'R_code';
comment on column report_total.ST_Rev is 'ST版本号';
comment on column report_total.LST_Rev is 'LST版本号';
comment on column report_total.allowance_rate is '津贴';
comment on column report_total.comfirm_by is '确认ID';
comment on column report_total.in_charge_by is '承认ID';
comment on column report_total.type is '拖机或不拖机';
comment on column report_total.create_by is '创建者ID';
comment on column report_total.create_at is '创建时间';
comment on column report_total.update_by is '更新者ID';
comment on column report_total.update_at is '更新时间';
comment on column report_total.delete_at is '删除时间';

-- Report - Total表子表
drop table if exists report_total_item;
CREATE TABLE report_total_item (
  id serial PRIMARY KEY,
  report_total_id integer,
  work_name varchar(64),
  process varchar(64),
  assembly_layout_Rev varchar(64),
  assembly_work_execution_firm varchar(64),
  most_ST_HT decimal(10, 2),
  most_ST_MT decimal(10, 2),
  most_LST_HT decimal(10, 2),
  most_LST_MT decimal(10, 2),
  ST_complement decimal(10,2),
  ST_sampling decimal(10,2),
  ST_sampling_size decimal(10,2),
  ST_sec decimal(10,2),
  LST_complement decimal(10,2),
  LST_sampling decimal(10,2),
  LST_sampling_size decimal(10,2),
  LST_most_value_use decimal(10,2),
  LST_sec decimal(10,2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_total_item is 'Report - Total表子表';
comment on column report_total_item.report_total_id is 'Total表ID';
comment on column report_total_item.work_name is '工位名称';
comment on column report_total_item.process is '进程';
comment on column report_total_item.assembly_layout_Rev is '部件版本号';
comment on column report_total_item.assembly_work_execution_firm is '组装工厂';
comment on column report_total_item.most_ST_HT is 'ST人工耗时';
comment on column report_total_item.most_ST_MT is 'ST机器耗时';
comment on column report_total_item.most_LST_HT is 'LST人工耗时';
comment on column report_total_item.most_LST_MT is 'LST机器耗时';
comment on column report_total_item.create_by is '创建者ID';
comment on column report_total_item.create_at is '创建时间';
comment on column report_total_item.update_by is '更新者ID';
comment on column report_total_item.update_at is '更新时间';
comment on column report_total_item.delete_at is '删除时间';

-- Report - 时间联络表
drop table if exists report_time_contact;
CREATE TABLE report_time_contact (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  comfirm_by integer,
  in_charge_by integer,
  model_id integer,
  stage varchar(16),
  publish_type varchar,
  revise_reason varchar(16),
  stlst varchar(128),
  phase_id integer,
  Rev_no varchar(64),
  all_count_sub decimal(10, 2),
  all_count_main decimal(10, 2),
  all_count_printing decimal(10, 2),
  all_count_external_inspection decimal(10, 2),
  all_count_packing decimal(10, 2),
  towing_last_version_sub decimal(10, 2),
  towing_last_version_main decimal(10, 2),
  towing_last_version_printing decimal(10, 2),
  towing_last_version_external_inspection decimal(10, 2),
  towing_last_version_packing decimal(10, 2),
  operation_standard_No varchar(64),
  operation_instruction varchar(64),
  exception_operation varchar(64),
  remark_version_copmare varchar(128),
  remark_sub varchar(128),
  remark_main varchar(128),
  remark_printing varchar(128),
  remark_external_inspection varchar(128),
  remark_packing varchar(128),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_time_contact is 'Report - 时间联络表';
comment on column report_time_contact.dept_id is '组织机构ID';
comment on column report_time_contact.title is '标题';
comment on column report_time_contact.sheet_name is 'Sheet名称';
comment on column report_time_contact.comfirm_by is '确认ID';
comment on column report_time_contact.in_charge_by is '承认ID';
comment on column report_time_contact.model_id is '机种ID';
comment on column report_time_contact.stage is 'ES/AMP/MP';
comment on column report_time_contact.publish_type is '发行类别：新规制定/修订';
comment on column report_time_contact.revise_reason is '修订理由';
comment on column report_time_contact.ST_type is 'ST/LST';
comment on column report_time_contact.Rev_no is '版本号';
comment on column report_time_contact.all_count_sub is '全数sub工序用时';
comment on column report_time_contact.all_count_main is '全数main工序用时';
comment on column report_time_contact.all_count_printing is '全数印字/检查/调整工序用时';
comment on column report_time_contact.all_count_external_inspection is '全数外装工序用时';
comment on column report_time_contact.all_count_packing is '全数捆包工序用时';
comment on column report_time_contact.towing_last_version_sub is '拖机上一版本sub工序用时';
comment on column report_time_contact.towing_last_version_main is '拖机上一版本main工序用时';
comment on column report_time_contact.towing_last_version_printing is '拖机上一版本印字/检查/调整工序用时';
comment on column report_time_contact.towing_last_version_external_inspection is '拖机上一版本外装工序用时';
comment on column report_time_contact.towing_last_version_packing is '拖机上一版本捆包工序用时';
comment on column report_time_contact.remark_version_copmare is '版本差异备注';
comment on column report_time_contact.remark_sub is 'sub差异备注';
comment on column report_time_contact.remark_main is 'main差异备注';
comment on column report_time_contact.remark_printing is '印字/检查/调整差异备注';
comment on column report_time_contact.remark_external_inspection is '外装差异备注';
comment on column report_time_contact.remark_packing is '捆包差异备注';
comment on column report_time_contact.create_by is '创建者ID';
comment on column report_time_contact.create_at is '创建时间';
comment on column report_time_contact.update_by is '更新者ID';
comment on column report_time_contact.update_at is '更新时间';
comment on column report_time_contact.delete_at is '删除时间';


-- 标准时间表
drop table if exists report_standard_time;
CREATE TABLE report_standard_time (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  stlst varchar(128),
  phase_id integer,
  sheet_name varchar(128),
  model_id integer,
  model_type varchar(64),
  unit varchar(32),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_standard_time is '标准时间表';
comment on column report_standard_time.dept_id is '组织机构ID';
comment on column report_standard_time.title is '标题';
comment on column report_standard_time.sheet_name is 'Sheet名称';
comment on column report_standard_time.model_id is '机种ID';
comment on column report_standard_time.model_type is '型号';
comment on column report_standard_time.unit is '单位';
comment on column report_standard_time.create_by is '创建者ID';
comment on column report_standard_time.create_at is '创建时间';
comment on column report_standard_time.update_by is '更新者ID';
comment on column report_standard_time.update_at is '更新时间';
comment on column report_standard_time.delete_at is '删除时间';


-- 标准时间表子表
drop table if exists report_standard_time_item;
CREATE TABLE report_standard_time_item (
  id serial PRIMARY KEY,
  report_standard_time_id integer,
  process_No integer,
  process_name varchar(64),
  most_HT decimal(10, 2),
  most_MT decimal(10, 2),
  most_MH decimal(10, 2),
  time_total decimal(10, 2),
  time_sample1 decimal(10, 2),
  time_sample_size decimal(10, 2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_standard_time_item is '标准时间表子表';
comment on column report_standard_time_item.report_standard_time_id is '标准时间表ID';
comment on column report_standard_time_item.process_No is '工程NO';
comment on column report_standard_time_item.process_name is '工程名';
comment on column report_standard_time_item.most_HT is 'most-HT值';
comment on column report_standard_time_item.most_MT is 'most-MT值';
comment on column report_standard_time_item.most_MH is 'most-MH值';
comment on column report_standard_time_item.time_total is '时间total值';
comment on column report_standard_time_item.time_sample1 is '时间sample1值';
comment on column report_standard_time_item.time_sample_size is '时间sampleSize值';
comment on column report_standard_time_item.create_by is '创建者ID';
comment on column report_standard_time_item.create_at is '创建时间';
comment on column report_standard_time_item.update_by is '更新者ID';
comment on column report_standard_time_item.update_at is '更新时间';
comment on column report_standard_time_item.delete_at is '删除时间';

-- 标准工数表
drop table if exists report_standard_work;
CREATE TABLE report_standard_work (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  stlst varchar(128),
  model_id integer,
  model_type varchar(64),
  coefficient decimal(10, 2),
  phase_id integer,
  Rev_No varchar(64),
  month_result timestamp,
  first_standard_work_title varchar(32),
  second_standard_work_title varchar(32),
  third_standard_work_title varchar(32),
  comfirm_by integer,
  in_charge_by integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_standard_work is '标准工数表';
comment on column report_standard_work.dept_id is '组织机构ID';
comment on column report_standard_work.title is '标题';
comment on column report_standard_work.sheet_name is 'Sheet名称';
comment on column report_standard_work.model_id is '机种ID';
comment on column report_standard_work.model_type is '型号';
comment on column report_standard_work.coefficient is '系数';
comment on column report_standard_work.phase_id is '生产阶段ID';
comment on column report_standard_work.Rev_No is '技通No';
comment on column report_standard_work.month_result is '发行日';
comment on column report_standard_work.first_standard_work_title is '首个标准工数title';
comment on column report_standard_work.second_standard_work_title is '第二个标准工数title';
comment on column report_standard_work.third_standard_work_title is '第三个标准工数title';
comment on column report_standard_work.comfirm_by is '确认ID';
comment on column report_standard_work.in_charge_by is '承认ID';
comment on column report_standard_work.create_by is '创建者ID';
comment on column report_standard_work.create_at is '创建时间';
comment on column report_standard_work.update_by is '更新者ID';
comment on column report_standard_work.update_at is '更新时间';
comment on column report_standard_work.delete_at is '删除时间';

-- 标准时间表子表
drop table if exists report_standard_work_item;
CREATE TABLE report_standard_work_item (
  id serial PRIMARY KEY,
  report_standard_work_id integer,
  process_No integer,
  process_name varchar(64),
  first_time decimal(10, 2),
  second_time decimal(10, 2),
  third_time decimal(10, 2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_standard_work_item is '标准时间表子表';
comment on column report_standard_work_item.report_standard_work_id is '标准工数表ID';
comment on column report_standard_work_item.process_No is '工程号';
comment on column report_standard_work_item.process_name is '工程名';
comment on column report_standard_work_item.first_time is '第一个时间';
comment on column report_standard_work_item.second_time is '第二个时间';
comment on column report_standard_work_item.third_time is '第三个时间';
comment on column report_standard_work_item.create_by is '创建者ID';
comment on column report_standard_work_item.create_at is '创建时间';
comment on column report_standard_work_item.update_by is '更新者ID';
comment on column report_standard_work_item.update_at is '更新时间';
comment on column report_standard_work_item.delete_at is '删除时间';

-- 履历表
drop table if exists report_change_record;
CREATE TABLE report_change_record (
  id serial PRIMARY KEY,
  dept_id integer,
  title varchar(128),
  sheet_name varchar(128),
  factory varchar(64),
  model_id integer,
  stlst varchar(128),
  phase_id integer,
  model_type varchar(64),
  destinations varchar(64),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_change_record is '履历表';
comment on column report_change_record.dept_id is '组织机构ID';
comment on column report_change_record.title is '标题';
comment on column report_change_record.sheet_name is 'Sheet名称';
comment on column report_change_record.factory is '工场';
comment on column report_change_record.model_id is '机种ID';
comment on column report_change_record.model_type is '型号';
comment on column report_change_record.destinations is '仕向';
comment on column report_change_record.create_by is '创建者ID';
comment on column report_change_record.create_at is '创建时间';
comment on column report_change_record.update_by is '更新者ID';
comment on column report_change_record.update_at is '更新时间';
comment on column report_change_record.delete_at is '删除时间';

-- 履历表子表
drop table if exists report_change_record_item;
CREATE TABLE report_change_record_item (
  id serial PRIMARY KEY,
  report_change_record_id integer,
  maked_at timestamp,
  Rev_No varchar(16),
  process_name varchar(64),
  content varchar(64),
  current_value decimal(10, 2),
  last_value decimal(10, 2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_change_record_item is '履历表子表';
comment on column report_change_record_item.report_change_record_id is '履历表ID';
comment on column report_change_record_item.maked_at is '制定日';
comment on column report_change_record_item.Rev_No is '版本号';
comment on column report_change_record_item.process_name is '工程名';
comment on column report_change_record_item.content is '修改内容';
comment on column report_change_record_item.current_value is '当前耗时值';
comment on column report_change_record_item.last_value is '变更前耗时值';
comment on column report_change_record_item.create_by is '创建者ID';
comment on column report_change_record_item.create_at is '创建时间';
comment on column report_change_record_item.update_by is '更新者ID';
comment on column report_change_record_item.update_at is '更新时间';
comment on column report_change_record_item.delete_at is '删除时间';

-- 报表审批表
drop table if exists report_approve;
CREATE TABLE report_approve (
  id serial PRIMARY KEY,
  dept_id integer,
  report_group_id integer,
  next_approver_id integer,
  status varchar(32),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_approve is '报表审批表';
comment on column report_approve.dept_id is '所属部门';
comment on column report_approve.report_group_id is '报表组ID';
comment on column report_approve.next_approver_id is '下一审批者ID';
comment on column report_approve.status is '状态';
comment on column report_approve.create_by is '创建者ID';
comment on column report_approve.create_at is '创建时间';
comment on column report_approve.update_by is '更新者ID';
comment on column report_approve.update_at is '更新时间';
comment on column report_approve.delete_at is '删除时间';

-- 报表审批历史表
drop table if exists report_approve_history;
CREATE TABLE report_approve_history (
  id serial PRIMARY KEY,
  dept_id integer,
  report_approve_id integer,
  result varchar(32),
  report_group_id integer,
  next_approver_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table report_approve_history is '报表审批表';
comment on column report_approve_history.report_approve_id is '报表审批表ID';
comment on column report_approve_history.report_group_id is '报表组ID';
comment on column report_approve_history.next_approver_id is '下一审批者ID';
comment on column report_approve_history.result is '审批结果';
comment on column report_approve_history.create_by is '创建者ID';
comment on column report_approve_history.create_at is '创建时间';
comment on column report_approve_history.update_by is '更新者ID';
comment on column report_approve_history.update_at is '更新时间';
comment on column report_approve_history.delete_at is '删除时间';