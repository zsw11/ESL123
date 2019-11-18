-- 菜单管理
drop table if exists sys_menu;
CREATE TABLE sys_menu (
  id serial PRIMARY KEY,
  parent_id bigint,
  name varchar(50),
  url varchar(200),
  perms varchar(500),
  type int,
  icon varchar(50),
  order_num int,
  create_by bigint,
  create_at timestamp default now(),
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_menu is '菜单管理';
comment on column sys_menu.parent_id is '父菜单ID，一级菜单为0';
comment on column sys_menu.name is '菜单名称';
comment on column sys_menu.url is '菜单URL';
comment on column sys_menu.perms is '授权(多个用逗号分隔，如：user:list,user:create)';
comment on column sys_menu.type is '类型   0：目录   1：菜单   2：按钮';
comment on column sys_menu.icon is '菜单图标';
comment on column sys_menu.order_num is '排序';
comment on column sys_menu.create_by is '创建者ID';
comment on column sys_menu.create_at is '创建时间';
comment on column sys_menu.update_by is '更新者ID';
comment on column sys_menu.update_at is '更新时间';
comment on column sys_menu.delete_at is '删除时间';

-- 系统用户
drop table if exists sys_user;
CREATE TABLE sys_user (
  id serial PRIMARY KEY,
  dept_id bigint,
  username varchar(50) not null,
  password varchar(100),
  salt varchar(20),
  email varchar(100),
  mobile varchar(100),
  status smallint,
  create_by bigint,
  create_at timestamp default now(),
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
Create Unique Index index_username_UNQ On sys_user(username);
comment on table sys_user is '系统用户';
comment on column sys_user.dept_id is '部门ID';
comment on column sys_user.username is '用户名';
comment on column sys_user.password is '密码';
comment on column sys_user.salt is '盐';
comment on column sys_user.email is '邮箱';
comment on column sys_user.mobile is '手机号';
comment on column sys_user.status is '状态  0：禁用   1：正常';
comment on column sys_user.create_by is '创建者ID';
comment on column sys_user.create_at is '创建时间';
comment on column sys_user.update_by is '更新者ID';
comment on column sys_user.update_at is '更新时间';
comment on column sys_user.delete_at is '删除时间';

-- 系统用户Token
drop table if exists sys_user_token;
CREATE TABLE sys_user_token (
  user_id serial PRIMARY KEY,
  token varchar(100) NOT NULL,
  expire_time timestamp,
  update_at timestamp
);
Create Unique Index index_token_UNQ On sys_user_token(token);
comment on table sys_user_token is '系统用户Token';
comment on column sys_user_token.token is 'token';
comment on column sys_user_token.expire_time is '过期时间';
comment on column sys_user_token.update_at is '更新时间';

-- 系统用户数据权限表
drop table if exists sys_user_data_filter;
CREATE TABLE sys_user_data_filter (
  user_id serial PRIMARY KEY,
  filter varchar(1024) NOT NULL,
  create_at timestamp default now(),
  update_at timestamp
);
comment on table sys_user_data_filter is '系统用户数据权限表';
comment on column sys_user_data_filter.filter is '数据过滤sql';
comment on column sys_user_data_filter.create_at is '创建时间';
comment on column sys_user_data_filter.update_at is '更新时间';

-- 系统验证码
drop table if exists sys_captcha;
CREATE TABLE sys_captcha (
  uuid varchar(36) PRIMARY KEY,
  code varchar(6) NOT NULL,
  expire_time timestamp DEFAULT NULL
);
comment on table sys_captcha is '系统验证码';
comment on column sys_captcha.uuid is 'uuid';
comment on column sys_captcha.code is '验证码';
comment on column sys_captcha.expire_time is '过期时间';

-- 角色
drop table if exists sys_role;
CREATE TABLE sys_role (
  id serial PRIMARY KEY,
  role_name varchar(100),
  dept_id bigint,
  remark varchar(100),
  create_by bigint,
  create_at timestamp default now(),
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_role is '角色';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.dept_id is '部门ID';
comment on column sys_role.remark is '备注';
comment on column sys_role.create_by is '创建者ID';
comment on column sys_role.create_at is '创建时间';
comment on column sys_role.update_by is '更新者ID';
comment on column sys_role.update_at is '更新时间';
comment on column sys_role.delete_at is '删除时间';

-- 用户与角色对应关系
drop table if exists sys_user_role;
CREATE TABLE sys_user_role (
  id serial NOT NULL PRIMARY KEY,
  user_id bigint,
  role_id bigint
);
comment on table sys_user_role is '用户与角色对应关系';
comment on column sys_user_role.user_id is '用户ID';
comment on column sys_user_role.role_id is '角色ID';

-- 角色与菜单对应关系
drop table if exists sys_role_menu;
CREATE TABLE sys_role_menu (
  id serial NOT NULL PRIMARY KEY,
  role_id bigint,
  menu_id bigint
);
comment on table sys_role_menu is '角色与菜单对应关系';
comment on column sys_role_menu.role_id is '角色ID';
comment on column sys_role_menu.menu_id is '菜单ID';

-- 系统配置信息
drop table if exists sys_config;
CREATE TABLE sys_config (
  id serial NOT NULL PRIMARY KEY,
  param_key varchar(50),
  param_value varchar(2048),
  status smallint DEFAULT 1,
  remark varchar(500),
  create_by bigint  NOT NULL,
  create_at timestamp default now()  NOT NULL,
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
Create Unique Index index_paramKey_UNQ On sys_config(param_key);
comment on table sys_config is '系统配置信息表';
comment on column sys_config.param_key is 'key';
comment on column sys_config.param_value is 'value';
comment on column sys_config.status is '状态   0：隐藏   1：显示';
comment on column sys_config.remark is '备注';
comment on column sys_config.create_by is '创建者ID';
comment on column sys_config.create_at is '创建时间';
comment on column sys_config.update_by is '更新者ID';
comment on column sys_config.update_at is '更新时间';
comment on column sys_config.delete_at is '删除时间';

-- 系统日志
drop table if exists sys_log;
CREATE TABLE sys_log (
  id serial NOT NULL PRIMARY KEY,
  username varchar(50),
  operation varchar(50),
  method varchar(200),
  params varchar(5000),
  time bigint NOT NULL,
  ip varchar(64),
  create_date timestamp
);
comment on table sys_log is '系统日志';
comment on column sys_log.username is '用户名';
comment on column sys_log.operation is '用户操作';
comment on column sys_log.method is '请求方法';
comment on column sys_log.params is '请求参数';
comment on column sys_log.time is '执行时长(毫秒)';
comment on column sys_log.ip is 'IP地址';
comment on column sys_log.create_date is '创建时间';

-- 文件上传
drop table if exists sys_oss;
CREATE TABLE sys_oss (
  id serial NOT NULL PRIMARY KEY,
  url varchar(200),
  create_date timestamp
);
comment on table sys_oss is '文件上传';
comment on column sys_oss.url is 'URL地址';
comment on column sys_oss.create_date is '创建时间';

-- 定时任务
drop table if exists schedule_job;
CREATE TABLE schedule_job (
  job_id serial NOT NULL PRIMARY KEY,
  bean_name varchar(200) DEFAULT NULL,
  method_name varchar(100) DEFAULT NULL,
  params varchar(2000) DEFAULT NULL,
  cron_expression varchar(100) DEFAULT NULL,
  status smallint DEFAULT NULL,
  remark varchar(255) DEFAULT NULL,
  create_at timestamp default now()
);
comment on table schedule_job is '定时任务';
comment on column schedule_job.job_id is '任务id';
comment on column schedule_job.bean_name is 'spring bean名称';
comment on column schedule_job.method_name is '方法名';
comment on column schedule_job.params is '参数';
comment on column schedule_job.cron_expression is 'cron表达式';
comment on column schedule_job.status is '任务状态  0：正常  1：暂停';
comment on column schedule_job.remark is '备注';
comment on column schedule_job.create_at is '创建时间';

-- 定时任务日志
drop table if exists schedule_job_log;
CREATE TABLE schedule_job_log (
  log_id serial NOT NULL PRIMARY KEY,
  job_id bigint NOT NULL,
  bean_name varchar(200) DEFAULT NULL,
  method_name varchar(100) DEFAULT NULL,
  params varchar(2000) DEFAULT NULL,
  status smallint NOT NULL,
  error varchar(2000) DEFAULT NULL,
  times int NOT NULL,
  create_at timestamp default now()
);
comment on table schedule_job_log is '定时任务日志';
CREATE INDEX index_jobId ON schedule_job_log (job_id);
comment on column schedule_job_log.log_id is '任务日志id';
comment on column schedule_job_log.job_id is '任务id';
comment on column schedule_job_log.bean_name is 'spring bean名称';
comment on column schedule_job_log.method_name is '方法名';
comment on column schedule_job_log.params is '参数';
comment on column schedule_job_log.status is '任务状态    0：成功    1：失败';
comment on column schedule_job_log.error is '失败信息';
comment on column schedule_job_log.times is '耗时(单位：毫秒)';
comment on column schedule_job_log.create_at is '任务日志id';
comment on column schedule_job_log.log_id is '创建时间';

-- 部门
drop table if exists sys_dept;
CREATE TABLE sys_dept (
  id serial NOT NULL PRIMARY KEY,
  parent_id bigint,
  name varchar(50),
  order_num int,
  del_flag smallint DEFAULT 0,
  dept_code varchar(64),
  dept_type varchar(64),
  dept_level varchar(64),
  STLST varchar(8),
  create_by bigint  NOT NULL,
  create_at timestamp default now() NOT NULL,
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_dept is '部门管理';
comment on column sys_dept.parent_id is '上级部门ID，一级部门为0';
comment on column sys_dept.name is '部门名称';
comment on column sys_dept.order_num is '排序';
comment on column sys_dept.del_flag is '是否删除  -1：已删除  0：正常';
comment on column sys_dept.dept_code is '部门机构编码';
comment on column sys_dept.dept_type is '部门机构类型';
comment on column sys_dept.dept_level is '部门机构等级';
comment on column sys_dept.STLST is 'ST/LST/Both';
comment on column sys_dept.create_by is '创建者ID';
comment on column sys_dept.create_at is '创建时间';
comment on column sys_dept.update_by is '更新者ID';
comment on column sys_dept.update_at is '更新时间';
comment on column sys_dept.delete_at is '删除时间';

-- 角色与部门对应关系
drop table if exists sys_role_dept;
CREATE TABLE sys_role_dept (
  id serial NOT NULL PRIMARY KEY,
  role_id bigint,
  dept_id bigint
);
comment on table sys_role_dept is '角色与部门对应关系';
comment on column sys_role_dept.role_id is '角色ID';
comment on column sys_role_dept.dept_id is '部门ID';

-- 字典类型
drop table if exists sys_dict;
CREATE TABLE sys_dict (
  id serial NOT NULL PRIMARY KEY,
  code varchar(64),
  name varchar(64),
  remark varchar(64),
  sort int,
  if_lock boolean default false,
  dict_type_id bigint NOT NULL,
  create_by bigint  NOT NULL,
  create_at timestamp default now(),
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_dict is '字典项';
comment on column sys_dict.code is '字典项编码';
comment on column sys_dict.name is '字典项名称';
comment on column sys_dict.remark is '描述';
comment on column sys_dict.sort is '排序';
comment on column sys_dict.if_lock is '是否锁定';
comment on column sys_dict.dict_type_id is '关联字典类型ID';
comment on column sys_dict.create_by is '创建者ID';
comment on column sys_dict.create_at is '创建时间';
comment on column sys_dict.update_by is '更新者ID';
comment on column sys_dict.update_at is '更新时间';
comment on column sys_dict.delete_at is '删除时间';

-- 字典项
drop table if exists sys_dict_type;
CREATE TABLE sys_dict_type (
  id serial NOT NULL PRIMARY KEY,
  type varchar(64),
  name varchar(128),
  remark varchar(128),
  if_lock boolean,
  create_by bigint NOT NULL,
  create_at timestamp default now() NOT NULL,
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_dict_type is '字典类型';
Create Unique Index index_type_UNQ On sys_dict_type(type);
comment on column sys_dict_type.type is '字典类型编码';
comment on column sys_dict_type.name is '字典类型名称';
comment on column sys_dict_type.remark is '描述';
comment on column sys_dict_type.if_lock is '是否锁定';
comment on column sys_dict_type.create_by is '创建者ID';
comment on column sys_dict_type.create_at is '创建时间';
comment on column sys_dict_type.update_by is '更新者ID';
comment on column sys_dict_type.update_at is '更新时间';
comment on column sys_dict_type.delete_at is '删除时间';

-- 人员信息
drop table if exists basic_member;
CREATE TABLE basic_member (
  id serial NOT NULL PRIMARY KEY,
  job_id BIGINT NOT NULL,
  dept_id BIGINT NOT NULL,
  user_id bigint,
  code varchar(64)  NOT NULL,
  name varchar(64)  NOT NULL,
  pinyin varchar(128) not null,
  gender varchar(20),
  mobilephone varchar(16),
  status varchar(20) not null,
  remark varchar(256),
  employment_date date not null,
  email varchar(64),
  job_number varchar(64),
  create_by bigint  NOT NULL,
  create_at timestamp default now() NOT NULL,
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table basic_member is '人员信息';
Create Unique Index index_codeAndUserId_UNQ On basic_member(code,user_id);
comment on column basic_member.job_id is '工作岗位';
comment on column basic_member.dept_id is '所属组织集团';
comment on column basic_member.user_id is '用户id';
comment on column basic_member.code is '人员编码';
comment on column basic_member.name is '人员姓名';
comment on column basic_member.pinyin is '姓名拼音';
comment on column basic_member.gender is '性别 0:男  1：女';
comment on column basic_member.mobilephone is '手机号码';
comment on column basic_member.status is '在职状态';
comment on column basic_member.remark is '备注';
comment on column basic_member.employment_date is '入职日期';
comment on column basic_member.email is '邮箱';
comment on column basic_member.job_number is '工号';
comment on column basic_member.create_by is '创建者ID';
comment on column basic_member.create_at is '创建时间';
comment on column basic_member.update_by is '更新者ID';
comment on column basic_member.update_at is '更新时间';
comment on column basic_member.delete_at is '删除时间';

-- 岗位信息
drop table if exists basic_job;
CREATE TABLE IF NOT EXISTS basic_job (
  id serial NOT NULL PRIMARY KEY,
  code varchar(64)  NOT NULL,
  name varchar(64)  NOT NULL,
  pinyin varchar(128) not null,
  grade varchar(32),
  create_by bigint  NOT NULL,
  create_at timestamp default now()  NOT NULL,
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table basic_job is '岗位信息';
Create Unique Index index_code_UNQ On basic_job(code);
comment on column basic_job.code is '岗位编码';
comment on column basic_job.name is '岗位名称';
comment on column basic_job.pinyin is '岗位名称拼音';
comment on column basic_job.grade is '工程师等级';
comment on column basic_job.create_by is '创建者ID';
comment on column basic_job.create_at is '创建时间';
comment on column basic_job.update_by is '更新者ID';
comment on column basic_job.update_at is '更新时间';
comment on column basic_job.delete_at is '删除时间';

-- 编码规则
drop table if exists sys_code_rule;
CREATE TABLE sys_code_rule (
  id serial NOT NULL PRIMARY KEY,
  code varchar(32) not null,
  name varchar(32) not null,
  current_serial_key varchar(64),
  current_serial integer DEFAULT 1,
  remark varchar(256),
  create_by bigint,
  create_at timestamp default now(),
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_code_rule is '编码规则';
Create Unique Index index_sysCodeRule_code_UNQ On sys_code_rule(code);
comment on column sys_code_rule.code is '编码';
comment on column sys_code_rule.name is '名称';
comment on column sys_code_rule.current_serial_key is '当前编号键';
comment on column sys_code_rule.current_serial is '当前编号';
comment on column sys_code_rule.remark is '备注';
comment on column sys_code_rule.create_by is '创建者ID';
comment on column sys_code_rule.create_at is '创建时间';
comment on column sys_code_rule.update_by is '更新者ID';
comment on column sys_code_rule.update_at is '更新时间';
comment on column sys_code_rule.delete_at is '删除时间';

-- 编码规则子项
drop table if exists sys_code_rule_item;
CREATE TABLE sys_code_rule_item (
  id serial NOT NULL PRIMARY KEY,
  code_rule_id bigint not null,
  order_number integer not null,
  type varchar(32) not null,
  fixed_value varchar(32),
  if_serial_key boolean DEFAULT false not null,
  number_boolean integer DEFAULT 4,
  begin_number integer DEFAULT 1,
  create_by bigint,
  create_at timestamp default now(),
  update_by bigint,
  update_at timestamp,
  delete_at timestamp
);
comment on table sys_code_rule_item is '编码规则子项';
comment on column sys_code_rule_item.code_rule_id is '编码规则ID';
comment on column sys_code_rule_item.order_number is '序号';
comment on column sys_code_rule_item.type is '类型';
comment on column sys_code_rule_item.fixed_value is '固定值';
comment on column sys_code_rule_item.if_serial_key is '是否序号键';
comment on column sys_code_rule_item.number_boolean is '位数';
comment on column sys_code_rule_item.begin_number is '起始编号';
comment on column sys_code_rule_item.create_by is '创建者ID';
comment on column sys_code_rule_item.create_at is '创建时间';
comment on column sys_code_rule_item.update_by is '更新时间';
comment on column sys_code_rule_item.update_at is '更新时间';
comment on column sys_code_rule_item.delete_at is '删除时间';

-- 引用表
drop table if exists sys_reference;
CREATE TABLE sys_reference (
 id serial NOT NULL PRIMARY KEY,
  main_entity varchar(64) NULL,
  main_id bigint NULL,
  by_entity varchar(64) NULL,
  by_id bigint NULL
);
comment on table sys_reference is '引用表';
comment on column sys_reference.main_entity is '主实体';
comment on column sys_reference.main_id is '主Id';
comment on column sys_reference.by_entity is '引用实体';
comment on column sys_reference.by_id is '引用Id';

-- 消息通知
drop table if exists sys_message;
CREATE TABLE sys_message (
  id serial NOT NULL PRIMARY KEY,
  dept_id bigint NOT NULL,
  process_id bigint,
  type varchar(16) NOT NULL,
  source_type varchar(16) NOT NULL,
  source_id bigint NOT NULL,
  to_member_id bigint,
  title varchar(64) NOT NULL,
  content varchar(128) NOT NULL,
  status varchar(16) NOT NULL,
  create_at timestamp default now() NOT NULL
);
comment on table sys_message is '消息通知';
comment on column sys_message.dept_id is '所属组织集团';
comment on column sys_message.process_id is '流程图实例ID';
comment on column sys_message.type is '消息类型(info,warn,error)';
comment on column sys_message.source_type is '来源类型';
comment on column sys_message.source_id is '来源ID';
comment on column sys_message.to_member_id is '指定用户ID';
comment on column sys_message.title is '标题';
comment on column sys_message.content is '内容';
comment on column sys_message.status is '状态';
comment on column sys_message.create_at is '创建时间';

-- 已读消息用户
drop table if exists sys_message_read;
CREATE TABLE sys_message_read (
  id serial NOT NULL PRIMARY KEY,
  member_id bigint NOT NULL,
  message_id bigint NOT NULL,
  create_at timestamp default now() NOT NULL
);
comment on table sys_message_read is '已读消息用户';
Create Unique Index index_sysMessageRead_memberIdAndMessageId_UNQ On sys_message_read(member_id,message_id);
comment on column sys_message_read.member_id is '用户ID';
comment on column sys_message_read.message_id is '消息通知ID';
comment on column sys_message_read.create_at is '创建时间';



-- 文件引用关系表
drop table if exists sys_file_reference;
CREATE TABLE sys_file_reference (
  id serial NOT NULL PRIMARY KEY,
  name varchar(128) NOT NULL,
  file_name varchar(64) NOT NULL,
  file_path varchar(128) NOT NULL,
  source_type varchar(64) NOT NULL,
  source_id bigint,
  if_reference boolean default false,
  create_at timestamp default now() NOT NULL
);
comment on table sys_file_reference is '文件引用关系表';
Create Unique Index index_sysFileReference_fileNameAndSourceTypeAndSourceId_UNQ On sys_file_reference(file_name,source_type,source_id);
comment on column sys_file_reference.name is '原文件名';
comment on column sys_file_reference.file_name is '文件名';
comment on column sys_file_reference.file_path is '文件路径';
comment on column sys_file_reference.source_type is '来源类型';
comment on column sys_file_reference.source_id is '来源ID';
comment on column sys_file_reference.if_reference is '是否引用';
comment on column sys_file_reference.create_at is '创建时间';

--  quartz自带表结构
drop table if exists QRTZ_JOB_DETAILS;
CREATE TABLE QRTZ_JOB_DETAILS(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA bytea NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP));

drop table if exists QRTZ_TRIGGERS;
CREATE TABLE QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT NULL,
PREV_FIRE_TIME BIGINT NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT NOT NULL,
END_TIME BIGINT NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT NULL,
JOB_DATA bytea NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP));

drop table if exists QRTZ_SIMPLE_TRIGGERS;
CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT NOT NULL,
REPEAT_INTERVAL BIGINT NOT NULL,
TIMES_TRIGGERED BIGINT NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP));

drop table if exists QRTZ_CRON_TRIGGERS;
CREATE TABLE QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP));

drop table if exists QRTZ_SIMPROP_TRIGGERS;
CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP));

drop table if exists QRTZ_BLOB_TRIGGERS;
CREATE TABLE QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA bytea NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP));
CREATE INDEX index_QRTZ_BLOB_TRIGGERS_SCHED_NAME ON QRTZ_BLOB_TRIGGERS (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP);

drop table if exists QRTZ_CALENDARS;
CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(200) NOT NULL,
CALENDAR bytea NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME));

drop table if exists QRTZ_PAUSED_TRIGGER_GRPS;
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP));

drop table if exists QRTZ_FIRED_TRIGGERS;
CREATE TABLE QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT NOT NULL,
SCHED_TIME BIGINT NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID));

drop table if exists QRTZ_SCHEDULER_STATE;
CREATE TABLE QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT NOT NULL,
CHECKIN_INTERVAL BIGINT NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME));

drop table if exists QRTZ_LOCKS;
CREATE TABLE QRTZ_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME));

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);

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
drop table if exists action;
CREATE TABLE action (
  id serial PRIMARY KEY,
  name varchar(64),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp,
  delete_at timestamp
);
comment on table action is '关键词';
comment on column action.name is '名称';
comment on column action.create_by is '创建者ID';
comment on column action.create_at is '创建时间';
comment on column action.update_by is '更新者ID';
comment on column action.update_at is '更新时间';
comment on column action.delete_at is '删除时间';

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
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, '分析表报表', NULL, NULL, 1, '2019-11-15 09:39:22', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, '人机联合表', NULL, NULL, 1, '2019-11-15 09:39:37', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'Collection-工位时间表', NULL, NULL, 1, '2019-11-15 09:39:59', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'Collection-Compare表', NULL, NULL, 1, '2019-11-15 09:40:19', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 'Collection-MOST Value表', NULL, NULL, 1, '2019-11-15 09:40:51', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 'Collection-Revision History表', NULL, NULL, 1, '2019-11-15 09:41:09', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 'Report-Total表', NULL, NULL, 1, '2019-11-15 09:41:25', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 'Report-拖机Total表', NULL, NULL, 1, '2019-11-15 09:41:51', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 'Report-时间联络表', NULL, NULL, 1, '2019-11-15 09:42:10', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 'Process List表', NULL, NULL, 1, '2019-11-15 09:42:32', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, '标准时间表', NULL, NULL, 1, '2019-11-15 09:42:43', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, '标准工数表', NULL, NULL, 1, '2019-11-15 09:42:53', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, '履历表', NULL, NULL, 1, '2019-11-15 09:43:00.935864', NULL, NULL, NULL);


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


-- ----------------------------
-- Records of basic_job
-- ----------------------------
INSERT INTO basic_job VALUES (1, 'W001', '临时岗位', 'linshigangwei', '01', 1, '2019-01-21 13:36:16', NULL, '2019-01-21 13:36:16', '2019-02-25 15:05:34');


-- ----------------------------
-- Records of basic_member
-- ----------------------------
INSERT INTO "public"."basic_member"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email","job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 1, 1, 1, 'admin', 'admin', 'admin', '0', '1', '在职', '无', '2019-10-15', '1', 1, '007', '2019-10-15 15:15:44', 1, '2019-10-15 15:15:47', NULL);


-- ----------------------------
-- Records of schedule_job
-- ----------------------------
-- INSERT INTO schedule_job VALUES (1, 'messageTask', 'batchUpdate', 'process', '0 0 10,22 * * ?', 0, '信息通知批量修改待处理类型为超期类型', '2019-02-03 14:55:56');


-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO sys_config VALUES (1, 'PaymentExcelPath', '/home/ubuntu/excel/', 1, '薪酬根据excel模板导出，配置生成excel缓存路径', 3, '2019-01-28 03:24:12', 3, '2019-02-20 17:22:27', '1970-01-01 00:00:00');
INSERT INTO sys_config VALUES (2, 'MessageExpire', '1440', 1, '待处理消息超期处理分钟配置（整数）', 1, '2018-12-27 09:44:19', 73, '2019-02-22 09:21:45', '1970-01-01 00:00:00');


-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") 
VALUES (1, 1, '总部', 0, 0, '0', 'bloc', '1', 'lst', 1, '2019-11-12 13:57:01', NULL, NULL, NULL);
-- INSERT INTO sys_dept VALUES (2, 1, '总部', 0, 0, NULL, 'headquarters', 'company', 1, 'LST', '2018-12-23 09:37:41', 1, '2019-02-27 11:08:13', '1970-01-01 00:00:00');
-- INSERT INTO sys_dept VALUES (3, 1, '广东（广州）', 0, 0, NULL, 'branch', 'company', 1, 'LST', '2018-12-23 09:37:41', 1, '2019-02-25 20:47:01', '1970-01-01 00:00:00');


-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO sys_dict VALUES (4, '01', '在职', '在职', 1, false, 2, 1, '2018-12-21 10:21:00', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO sys_dict VALUES (5, '02', '离职', '离职', 2, false, 2, 1, '2018-12-21 10:21:00', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO sys_dict VALUES (6, '03', '试用', '试用', 3, false, 2, 1, '2018-12-21 10:21:00', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO sys_dict VALUES (72, '01', '男', '男', 1, false, 22, 1, '2018-12-27 13:55:40', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO sys_dict VALUES (73, '02', '女', '女', 2, false, 22, 1, '2018-12-27 13:55:40', NULL, NULL, '1970-01-01 00:00:00');

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO sys_dict_type VALUES (2, 'WorkingStatus', '在职状态', '在职状态', true, 1, '2018-12-20 13:55:40', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO sys_dict_type VALUES (22, 'Gender', '性别', '性别', true, 1, '2018-12-27 13:55:40', NULL, NULL, '1970-01-01 00:00:00');


-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 4, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, 1, '用户管理', 'sys/user', NULL, 1, 'menu', 1, 1, '2018-12-23 09:37:41', 1, '2019-04-26 05:31:17', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 1, 'SQL监控', 'http://localhost:8080/renren-fast/druid/sql.html', NULL, 1, 'sql', 4, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-01-14 11:09:58');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 1, '定时任务', 'job/schedule', NULL, 1, 'job', 5, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-16 14:52:15');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 6, '查看', NULL, 'sys:schedule:list,sys:schedule:info', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-14 14:13:07');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-14 14:13:09');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-14 14:13:11');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-14 14:13:14');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-16 14:52:11');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-14 14:13:19');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-16 14:52:08');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-02-14 14:13:34');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (15, 2, '列表', NULL, 'sys:user:list,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 05:36:29', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (16, 2, '新增', NULL, 'sys:user:create,sys:user:list,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 05:28:23', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:user:list,sys:user:info,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 06:23:47', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (19, 3, '列表', NULL, 'sys:role:list,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 05:38:40', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (20, 3, '新增', NULL, 'sys:role:create,sys:role:list,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 05:39:19', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list,sys:role:info,basic:dept:list,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 06:12:26', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (22, 3, '删除', NULL, 'sys:role:delete,', 2, NULL, 0, 1, '2018-12-23 09:37:41', 1, '2019-04-26 05:41:13', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (24, 4, '新增', NULL, 'sys:menu:create,sys:menu:list', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:list', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (27, 1, '参数管理', 'sys/config', NULL, 1, 'config', 1, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (29, 1, '系统日志', 'sys/log', 'sys:log:list', 1, 'log', 7, 1, '2018-12-23 09:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (30, 1, '文件上传', 'oss/oss', 'sys:oss:all', 1, 'oss', 1, 1, '2018-12-23 09:37:41', NULL, NULL, '2019-01-14 15:09:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (31, 0, '基础数据', NULL, NULL, 0, 'basic', 5, 1, '2017-03-23 22:37:41', 2, '2019-02-16 14:55:48', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (42, 31, '岗位信息', 'basic/job', NULL, 1, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (43, 42, '查看', NULL, 'basic:job:list,basic:job:info', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (44, 42, '新增', NULL, 'basic:job:save', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (45, 42, '修改', NULL, 'basic:job:update', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (46, 42, '删除', NULL, 'basic:job:delete', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, 31, '人员信息', 'basic/member', NULL, 1, NULL, 4, 1, '2017-03-23 22:37:41', 2, '2019-01-04 13:36:19', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, 47, '查看', NULL, 'basic:member:list,basic:member:info', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, 47, '新增', NULL, 'basic:member:save', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, 47, '修改', NULL, 'basic:member:update', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, 47, '删除', NULL, 'basic:member:delete', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (72, 31, '部门管理', 'basic/dept', NULL, 1, NULL, 5, 1, '2017-03-23 22:37:41', 1, '2019-04-26 06:50:28', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (73, 72, '列表', NULL, 'basic:dept:list,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 06:47:22', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (74, 72, '新增', NULL, 'basic:dept:create,basic:dept:list,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 06:47:51', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (75, 72, '修改', NULL, 'basic:dept:update,basic:dept:list,basic:dept:info,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 06:48:28', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (76, 72, '删除', NULL, 'basic:dept:delete,basic:dept:list,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 06:48:48', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (77, 1, '字典管理', 'sys/dict', NULL, 1, 'menu', 8, 1, '2017-03-23 22:37:41', 1, '2019-04-26 07:04:43', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (78, 77, '列表', NULL, 'sys:dict:list,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 07:05:20', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (79, 77, '新增', NULL, 'sys:dict:create,sys:dict:list,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 07:05:51', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (80, 77, '修改', NULL, 'sys:dict:update,sys:dict:list,sys:dict:info,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 07:06:20', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (81, 77, '删除', NULL, 'sys:dict:delete,sys:dict:list,', 2, NULL, 1, 1, '2017-03-23 22:37:41', 1, '2019-04-26 07:06:39', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (82, 31, '字典项', 'sys/dict', NULL, 1, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '2019-01-03 09:17:09');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (83, 82, '查看', NULL, 'sys:dict:list,sys:dict:info', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '2019-01-03 09:17:04');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (84, 82, '新增', NULL, 'sys:dict:save', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '2019-01-03 09:17:03');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (85, 82, '修改', NULL, 'sys:dict:update', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '2019-01-03 09:17:01');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (86, 82, '删除', NULL, 'sys:dict:delete', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '2019-01-03 09:16:59');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (87, 1, '编码规则', 'sys/coderule', NULL, 1, NULL, 10, 1, '2017-03-23 22:37:41', 2, '2019-01-04 13:32:41', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (88, 87, '查看', NULL, 'sys:coderule:list,sys:coderule:info', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (89, 87, '新增', NULL, 'sys:coderule:save', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (90, 87, '修改', NULL, 'sys:coderule:update', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (91, 87, '删除', NULL, 'sys:coderule:delete', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (292, 2, '重置密码', NULL, 'sys:user:reset', 2, NULL, 0, 3, '2019-02-18 17:49:27', NULL, '2019-02-18 17:49:27', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (303, 27, '列表', NULL, 'sys:config:list,sys:config:info,', 2, NULL, NULL, 1, '2019-04-29 01:51:48', NULL, '2019-04-29 01:51:48', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (304, 27, '新增', NULL, 'sys:config:create,sys:config:info,', 2, NULL, NULL, 1, '2019-04-29 01:52:52', 1, '2019-04-29 01:55:30', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (305, 27, '修改', NULL, 'sys:config:update,', 2, NULL, NULL, 1, '2019-04-29 01:54:02', NULL, '2019-04-29 01:54:02', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (306, 27, '删除', NULL, 'sys:config:delete,', 2, NULL, NULL, 1, '2019-04-29 01:54:47', NULL, '2019-04-29 01:54:47', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (307, 0, 'Master Data', NULL, '', 0, 'menu', 3, NULL, '2019-11-08 14:27:35.130337', 1, '2019-11-08 14:35:24.468', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (401, 307, '常用审批意见', 'masterdata/approveopininon', NULL, 1, 'config', 1, NULL, '2019-11-08 15:28:51.256673', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (402, 401, '查看', NULL, 'masterData:approveopininon:list,masterData:approveopininon:info', 2, NULL, 1, NULL, '2019-11-08 15:31:05.426511', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (403, 401, '新增', NULL, 'masterData:approveopininon:save', 2, NULL, 1, NULL, '2019-11-08 15:31:05.448949', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (404, 401, '修改', NULL, 'masterData:approveopininon:update', 2, NULL, 1, NULL, '2019-11-08 15:31:05.463064', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (405, 401, '删除', NULL, 'masterData:approveopininon:delete', 2, NULL, 1, NULL, '2019-11-08 15:31:05.479702', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (416, 307, '部品', 'masterdata/part', NULL, 1, 'config', 1, NULL, '2019-11-08 15:34:28.409888', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (417, 416, '查看', NULL, 'masterData:part:list,masterData:part:info', 2, NULL, 1, NULL, '2019-11-08 15:35:07.086872', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (418, 416, '新增', NULL, 'masterData:part:save', 2, NULL, 1, NULL, '2019-11-08 15:35:07.109234', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (419, 416, '修改', NULL, 'masterData:part:update', 2, NULL, 1, NULL, '2019-11-08 15:35:07.120316', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (420, 416, '删除', NULL, 'masterData:part:delete', 2, NULL, 1, NULL, '2019-11-08 15:35:07.130856', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (421, 307, '生产阶段', 'masterdata/phase', NULL, 1, 'config', 1, NULL, '2019-11-08 15:35:16.331639', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (422, 421, '查看', NULL, 'masterData:phase:list,masterData:phase:info', 2, NULL, 1, NULL, '2019-11-08 15:35:30.164927', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (423, 421, '新增', NULL, 'masterData:phase:save', 2, NULL, 1, NULL, '2019-11-08 15:35:30.176979', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (424, 421, '修改', NULL, 'masterData:phase:update', 2, NULL, 1, NULL, '2019-11-08 15:35:30.189303', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (425, 421, '删除', NULL, 'masterData:phase:delete', 2, NULL, 1, NULL, '2019-11-08 15:35:30.200429', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (426, 307, '报表', 'masterdata/report', NULL, 1, 'config', 1, NULL, '2019-11-08 15:35:37.399362', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (427, 425, '查看', NULL, 'masterData:report:list,masterData:report:info', 2, NULL, 1, NULL, '2019-11-08 15:35:56.494035', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (428, 425, '新增', NULL, 'masterData:report:save', 2, NULL, 1, NULL, '2019-11-08 15:35:56.504269', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (429, 425, '修改', NULL, 'masterData:report:update', 2, NULL, 1, NULL, '2019-11-08 15:35:56.515729', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (430, 425, '删除', NULL, 'masterData:report:delete', 2, NULL, 1, NULL, '2019-11-08 15:35:56.525045', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (431, 307, '报表组', 'masterdata/reportgroup', NULL, 1, 'config', 1, NULL, '2019-11-08 15:35:56.534885', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (432, 431, '查看', NULL, 'masterData:reportgroup:list,masterData:reportgroup:info', 2, NULL, 1, NULL, '2019-11-08 15:36:11.392194', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (433, 431, '新增', NULL, 'masterData:reportgroup:save', 2, NULL, 1, NULL, '2019-11-08 15:36:11.403334', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (434, 431, '修改', NULL, 'masterData:reportgroup:update', 2, NULL, 1, NULL, '2019-11-08 15:36:11.413983', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (435, 431, '删除', NULL, 'masterData:reportgroup:delete', 2, NULL, 1, NULL, '2019-11-08 15:36:11.427339', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (436, 307, '治工具', 'masterdata/tool', NULL, 1, 'config', 1, NULL, '2019-11-08 15:36:11.435771', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (437, 436, '查看', NULL, 'masterData:tool:list,masterData:tool:info', 2, NULL, 1, NULL, '2019-11-08 15:36:25.710539', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (438, 436, '新增', NULL, 'masterData:tool:save', 2, NULL, 1, NULL, '2019-11-08 15:36:25.724866', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (439, 436, '修改', NULL, 'masterData:tool:update', 2, NULL, 1, NULL, '2019-11-08 15:36:25.738336', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (440, 436, '删除', NULL, 'masterData:tool:delete', 2, NULL, 1, NULL, '2019-11-08 15:36:25.751515', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (446, 307, '工位类型', 'masterdata/workstationtype', NULL, 1, 'config', 1, NULL, '2019-11-08 15:36:40.808149', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (447, 446, '查看', NULL, 'masterData:workstationtype:list,masterData:workstationtype:info', 2, NULL, 1, NULL, '2019-11-08 15:36:55.327124', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (448, 446, '新增', NULL, 'masterData:workstationtype:save', 2, NULL, 1, NULL, '2019-11-08 15:36:55.336529', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (449, 446, '修改', NULL, 'masterData:workstationtype:update', 2, NULL, 1, NULL, '2019-11-08 15:36:55.34815', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (450, 446, '删除', NULL, 'masterData:workstationtype:delete', 2, NULL, 1, NULL, '2019-11-08 15:36:55.361574', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (451, 307, '工位', 'masterdata/workstation', NULL, 1, 'config', 1, NULL, '2019-11-08 15:36:55.373083', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (452, 451, '查看', NULL, 'masterData:workstation:list,masterData:workstation:info', 2, NULL, 1, NULL, '2019-11-08 15:37:07.105426', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (453, 451, '新增', NULL, 'masterData:workstation:save', 2, NULL, 1, NULL, '2019-11-08 15:37:07.128195', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (454, 451, '修改', NULL, 'masterData:workstation:update', 2, NULL, 1, NULL, '2019-11-08 15:37:07.138884', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (455, 451, '删除', NULL, 'masterData:workstation:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:07.149442', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (456, 307, '工位类型节点', 'masterdata/workstationtypenode', NULL, 1, 'config', 1, NULL, '2019-11-08 15:37:07.160564', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (457, 456, '查看', NULL, 'masterData:workstationtypenode:list,masterData:workstationtypenode:info', 2, NULL, 1, NULL, '2019-11-08 15:37:18.7701', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (458, 456, '新增', NULL, 'masterData:workstationtypenode:save', 2, NULL, 1, NULL, '2019-11-08 15:37:18.791161', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (459, 456, '修改', NULL, 'masterData:workstationtypenode:update', 2, NULL, 1, NULL, '2019-11-08 15:37:18.801475', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (460, 456, '删除', NULL, 'masterData:workstationtypenode:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:18.819938', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (461, 307, '机种', 'masterdata/model', NULL, 1, 'config', 1, NULL, '2019-11-08 15:37:18.835141', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (462, 461, '查看', NULL, 'masterData:model:list,masterData:model:info', 2, NULL, 1, NULL, '2019-11-08 15:37:28.296369', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (463, 461, '新增', NULL, 'masterData:model:save', 2, NULL, 1, NULL, '2019-11-08 15:37:28.311712', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (464, 461, '修改', NULL, 'masterData:model:update', 2, NULL, 1, NULL, '2019-11-08 15:37:28.325631', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (465, 461, '删除', NULL, 'masterData:model:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:28.339652', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (471, 307, '机种系列', 'masterdata/modelseries', NULL, 1, 'config', 1, NULL, '2019-11-08 15:37:43.16794', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (472, 471, '查看', NULL, 'masterData:modelseries:list,masterData:modelseries:info', 2, NULL, 1, NULL, '2019-11-08 15:37:55.540469', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (473, 471, '新增', NULL, 'masterData:modelseries:save', 2, NULL, 1, NULL, '2019-11-08 15:37:55.551621', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (474, 471, '修改', NULL, 'masterData:modelseries:update', 2, NULL, 1, NULL, '2019-11-08 15:37:55.562454', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (475, 471, '删除', NULL, 'masterData:modelseries:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:55.570677', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (481, 307, '关键词', 'masterdata/action', NULL, 1, 'config', 1, NULL, '2019-11-08 15:38:04.642688', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (482, 481, '查看', NULL, 'masterData:action:list,masterData:opertaion:info', 2, NULL, 1, NULL, '2019-11-08 15:38:27.685012', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (483, 481, '新增', NULL, 'masterData:action:save', 2, NULL, 1, NULL, '2019-11-08 15:38:27.698335', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (484, 481, '修改', NULL, 'masterData:action:update', 2, NULL, 1, NULL, '2019-11-08 15:38:27.707977', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (485, 481, '删除', NULL, 'masterData:action:delete', 2, NULL, 1, NULL, '2019-11-08 15:38:27.716807', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (486, 0, '分析表', 'workbook/workbook', NULL, 1, 'config', 1, NULL, '2019-11-11 13:22:59.903762', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (487, 486, '查看', NULL, 'workBook:workbook:list,workBook:workbook:info', 2, NULL, 1, NULL, '2019-11-11 13:23:32.709518', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (488, 486, '新增', NULL, 'workBook:workbook:save', 2, NULL, 1, NULL, '2019-11-11 13:23:41.509737', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (489, 486, '修改', NULL, 'workBook:workbook:update', 2, NULL, 1, NULL, '2019-11-11 13:23:47.071175', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (490, 486, '删除', NULL, 'workBook:workbook:delete', 2, NULL, 1, NULL, '2019-11-11 13:23:54.024564', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (496, 307, '手顺', 'masterdata/opertaiongroup', NULL, 1, 'config', 1, NULL, '2019-11-11 13:27:18.335923', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (497, 496, '查看', NULL, 'masterData:opertaiongroup:list,masterData:opertaiongroup:info', 2, NULL, 1, NULL, '2019-11-11 13:27:41.834855', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (498, 496, '新增', NULL, 'masterData:opertaiongroup:save', 2, NULL, 1, NULL, '2019-11-11 13:27:52.906009', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (499, 496, '修改', NULL, 'masterData:opertaiongroup:update', 2, NULL, 1, NULL, '2019-11-11 13:28:03.767979', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (500, 496, '删除', NULL, 'masterData:opertaiongroup:delete', 2, NULL, 1, NULL, '2019-11-11 13:28:13.135872', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (501, 307, '常用指标组合', 'masterdata/measuregroup', NULL, 1, 'config', 1, NULL, '2019-11-11 13:28:30.528337', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (502, 501, '查看', NULL, 'masterData:measuregroup:list,masterData:measuregroup:info', 2, NULL, 1, NULL, '2019-11-11 13:28:45.473317', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (503, 501, '新增', NULL, 'masterData:measuregroup:save', 2, NULL, 1, NULL, '2019-11-11 13:28:54.230925', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (504, 501, '修改', NULL, 'masterData:measuregroup:update', 2, NULL, 1, NULL, '2019-11-11 13:29:03.722732', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (505, 501, '删除', NULL, 'masterData:measuregroup:delete', 2, NULL, 1, NULL, '2019-11-11 13:29:12.484948', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (506, 0, '报表', NULL, NULL, 0, 'basic', 2, 1, '2019-11-15 11:02:22', NULL, NULL, NULL);


-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO sys_role VALUES (1, 'admin', 1, NULL, 1, '2019-02-25 10:13:01', 1, '2019-03-02 14:30:03', '1970-01-01 00:00:00');

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO sys_user VALUES (1, 1, 'admin', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'root@yu.io', '13800138000', 1, 1, '2016-11-11 11:11:11', NULL, NULL, '1970-01-01 00:00:00');