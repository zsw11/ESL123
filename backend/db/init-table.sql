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