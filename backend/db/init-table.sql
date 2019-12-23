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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (2, 3, 1);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (3, 4, 1);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (4, 5, 1);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (5, 6, 1);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (8, 13, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (9, 14, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (10, 15, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (11, 16, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (12, 17, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (13, 18, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (14, 19, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (1, 2, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (15, 20, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (6, 11, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (7, 12, 6);

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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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

INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, '02', 'ST', 'ST', 1, 'f', 3, 1, '2019-11-28 15:24:19', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (22, '01', 'LST', 'LST', 2, 'f', 3, 1, '2019-11-28 15:26:10.714288', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, '01', '审批中', '审批中', 1, 'f', 1, 1, '2019-11-25 09:19:55.110518', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, '02', '已审批', '已审批', 2, 'f', 1, 1, '2019-11-25 09:20:20.18776', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, '03', '未审批', '未审批', 3, 'f', 1, 1, '2019-12-18 13:35:44.38026', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, '01', '通过', NULL, 1, 'f', 4, 6, '2019-12-18 16:00:24.474', NULL, '2019-12-18 16:00:24.474', NULL);
INSERT INTO "public"."sys_dict"("id", "code", "name", "remark", "sort", "if_lock", "dict_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, '02', '拒绝', NULL, 2, 'f', 4, 6, '2019-12-18 16:00:39.968', NULL, '2019-12-18 16:00:39.968', NULL);
select setval('sys_dict_id_seq',23);


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
  update_at timestamp default now(),
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

INSERT INTO "public"."sys_dict_type"("id", "type", "name", "remark", "if_lock", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 'Status', '审批状态', '审批状态', 't', 1, '2019-11-25 09:21:00.011549', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_type"("id", "type", "name", "remark", "if_lock", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'ST', 'ST/LST', 'ST/LST', 't', 1, '2019-11-28 15:22:09', NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_type"("id", "type", "name", "remark", "if_lock", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'Result', 'approveResult', '审批结果', 't', 6, '2019-12-18 15:59:35.538', 6, '2019-12-18 16:00:01.027', NULL);
select setval('sys_dict_type_id_seq',5);

-- 人员信息
drop table if exists basic_staff;
CREATE TABLE basic_staff (
  id serial NOT NULL PRIMARY KEY,
  job_id BIGINT,
  dept_id BIGINT NOT NULL,
  user_id bigint,
  code varchar(64)  NOT NULL,
  name varchar(64)  NOT NULL,
  pinyin varchar(128),
  gender varchar(20),
  mobilephone varchar(16),
  status varchar(20),
  remark varchar(256),
  employment_date date,
  email varchar(64),
  job_number varchar(64),
  create_by bigint  NOT NULL,
  create_at timestamp default now() NOT NULL,
  update_by bigint,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table basic_staff is '人员信息';
Create Unique Index index_codeAndUserId_UNQ On basic_staff(code,user_id);
comment on column basic_staff.job_id is '工作岗位';
comment on column basic_staff.dept_id is '所属组织集团';
comment on column basic_staff.user_id is '用户id';
comment on column basic_staff.code is '人员编码';
comment on column basic_staff.name is '人员姓名';
comment on column basic_staff.pinyin is '姓名拼音';
comment on column basic_staff.gender is '性别 0:男  1：女';
comment on column basic_staff.mobilephone is '手机号码';
comment on column basic_staff.status is '在职状态';
comment on column basic_staff.remark is '备注';
comment on column basic_staff.employment_date is '入职日期';
comment on column basic_staff.email is '邮箱';
comment on column basic_staff.job_number is '工号';
comment on column basic_staff.create_by is '创建者ID';
comment on column basic_staff.create_at is '创建时间';
comment on column basic_staff.update_by is '更新者ID';
comment on column basic_staff.update_at is '更新时间';
comment on column basic_staff.delete_at is '删除时间';

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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  type varchar(16) NOT NULL,
  source_type varchar(16) NOT NULL,
  source_id bigint NOT NULL,
  to_staff_id bigint,
  title varchar(64) NOT NULL,
  content varchar(128) NOT NULL,
  status varchar(16) NOT NULL,
  create_at timestamp default now() NOT NULL
);
comment on table sys_message is '消息通知';
comment on column sys_message.dept_id is '所属组织集团';
comment on column sys_message.type is '消息类型(info,warn,error)';
comment on column sys_message.source_type is '来源类型';
comment on column sys_message.source_id is '来源ID';
comment on column sys_message.to_staff_id is '指定用户ID';
comment on column sys_message.title is '标题';
comment on column sys_message.content is '内容';
comment on column sys_message.status is '状态';
comment on column sys_message.create_at is '创建时间';

-- 已读消息用户
drop table if exists sys_message_read;
CREATE TABLE sys_message_read (
  id serial NOT NULL PRIMARY KEY,
  staff_id bigint NOT NULL,
  message_id bigint NOT NULL,
  create_at timestamp default now() NOT NULL
);
comment on table sys_message_read is '已读消息用户';
Create Unique Index index_sysMessageRead_staffIdAndMessageId_UNQ On sys_message_read(staff_id,message_id);
comment on column sys_message_read.staff_id is '用户ID';
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
  pinyin varchar(128),
  opininon varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table approve_opininon is '常用审批意见';
comment on column approve_opininon.approve_operation is '审批操作';
comment on column approve_opininon.pinyin is '拼音';
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
  pinyin varchar(128),
  dept_id integer,
  remark varchar(128),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table action is '关键词';
Create Unique Index index_action_name_UNQ On action(name);
CREATE UNIQUE INDEX action_uniq ON action (name,dept_id) WHERE delete_at IS NULL;
comment on column action.name is '名称';
comment on column action.pinyin is '拼音';
comment on column action.dept_id is '部门ID';
comment on column action.remark is '备注';
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
  pinyin varchar(128),
  common boolean,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table tool is '治工具';
comment on column tool.name is '名称';
CREATE UNIQUE INDEX tool_uniq ON tool (name) WHERE delete_at IS NULL;
comment on column tool.pinyin is '拼音';
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
  pinyin varchar(128),
  common boolean,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table part is '部品';
comment on column part.name is '名称';
CREATE UNIQUE INDEX part_uniq ON part (name) WHERE delete_at IS NULL;
comment on column part.pinyin is '拼音';
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
  pinyin varchar(128),
  continue_phase_id integer,
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table phase is '生产阶段';
Create Unique Index index_phase_name_UNQ On phase(name);
comment on column phase.name is '名称';
comment on column phase.pinyin is '拼音';
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
  pinyin varchar(128),
  remark varchar(512),
  workstation_type_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table workstation is '工位';
Create Unique Index index_workstation_name_UNQ On workstation(name);
comment on column workstation.name is '名称';
comment on column workstation.pinyin is '拼音';
comment on column workstation.remark is '备注';
comment on column workstation.workstation_type_id is '工位類型';
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
  pinyin varchar(128),
  workstation_id integer,
  workstation_type_id integer,
  remark varchar(512),
  parent_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table workstation_type_node is '工位类型节点';
Create Unique Index index_workstation_type_node_name_UNQ On workstation_type_node(name);
comment on column workstation_type_node.name is '名称';
comment on column workstation_type_node.pinyin is '拼音';
comment on column workstation_type_node.workstation_id is '工位ID';
comment on column workstation_type_node.workstation_type_id is '工位类型ID';
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
  pinyin varchar(128),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table workstation_type is '工位类型';
Create Unique Index index_workstation_type_name_UNQ On workstation_type(name);
comment on column workstation_type.name is '名称';
comment on column workstation_type.pinyin is '拼音';
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
  pinyin varchar(128),
  ename varchar(128),
  form_code varchar(64),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table report is '报表';
Create Unique Index index_report_name_UNQ On report(name);
comment on column report.name is '名称';
comment on column report.pinyin is '拼音';
comment on column report.ename is '英文名';
comment on column report.form_code is '空Form标准编号';
comment on column report.remark is '备注';
comment on column report.create_by is '创建者ID';
comment on column report.create_at is '创建时间';
comment on column report.update_by is '更新者ID';
comment on column report.update_at is '更新时间';
comment on column report.delete_at is '删除时间';
INSERT INTO "public"."report"("id", "name", "pinyin", "ename", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, '分析表报表', 'work_book','work_book',NULL, NULL, 1, '2019-11-15 09:39:22', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, '人机联合表','rj','rj', NULL, NULL, 1, '2019-11-15 09:39:37', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'Collection-工位时间表', 'collection_station_time','collection_station_time',NULL, NULL, 1, '2019-11-15 09:39:59', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename", "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'Collection-Compare表','collection_compare','collection_compare', NULL, NULL, 1, '2019-11-15 09:40:19', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 'Collection-MOST Value表','collection_most_value','collection_most_value', NULL, NULL, 1, '2019-11-15 09:40:51', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 'Collection-Revision History表','collection_revision_history','collection_revision_history', NULL, NULL, 1, '2019-11-15 09:41:09', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 'Report-Total表', 'report_total','report_total',NULL, NULL, 1, '2019-11-15 09:41:25', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 'Report-拖机Total表', 'tuoji','tuoji',NULL, NULL, 1, '2019-11-15 09:41:51', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 'Report-时间联络表','report_time_contact','report_time_contact', NULL, NULL, 1, '2019-11-15 09:42:10', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 'Process List表','list','list', NULL, NULL, 1, '2019-11-15 09:42:32', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, '标准时间表','report_standard_time','report_standard_time', NULL, NULL, 1, '2019-11-15 09:42:43', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, '标准工数表','report_standard_work','report_standard_work', NULL, NULL, 1, '2019-11-15 09:42:53', NULL, NULL, NULL);
INSERT INTO "public"."report"("id", "name", "pinyin","ename",  "form_code", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, '履历表','report_change_record','report_change_record', NULL, NULL, 1, '2019-11-15 09:43:00.935864', NULL, NULL, NULL);


-- 报表组
drop table if exists report_group;
CREATE TABLE report_group (
  id serial PRIMARY KEY,
  name varchar(64),
  pinyin varchar(128),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table report_group is '报表组';
Create Unique Index index_report_group_name_UNQ On report_group(name);
comment on column report_group.name is '名称';
comment on column report_group.pinyin is '拼音';
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
  pinyin varchar(128),
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
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table model is '机种';
comment on column model.model_series_id is '机种系列ID';
CREATE UNIQUE INDEX model_uniq ON model (name) WHERE delete_at IS NULL;
comment on column model.name is '名称';
comment on column model.pinyin is '拼音';
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
  pinyin varchar(128),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table model_series is '机种系列';
CREATE UNIQUE INDEX model_series_uniq ON model_series (name) WHERE delete_at IS NULL;
comment on column model_series.name is '名称';
comment on column model_series.pinyin is '拼音';
comment on column model_series.remark is '备注';
comment on column model_series.create_by is '创建者ID';
comment on column model_series.create_at is '创建时间';
comment on column model_series.update_by is '更新者ID';
comment on column model_series.update_at is '更新时间';
comment on column model_series.delete_at is '删除时间';

-- 组织机构关键词关系
drop table if exists dept_action_rela;
CREATE TABLE dept_action_rela (
  id serial PRIMARY KEY,
  dept_id integer,
  action_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table dept_action_rela is '组织机构关键词关系';
comment on column dept_action_rela.dept_id is '组织机构ID';
comment on column dept_action_rela.action_id is '关键词ID';
CREATE UNIQUE INDEX dept_action_rela_uniq ON dept_action_rela (dept_id,action_id) WHERE delete_at IS NULL;
comment on column dept_action_rela.create_by is '创建者ID';
comment on column dept_action_rela.create_at is '创建时间';
comment on column dept_action_rela.update_by is '更新者ID';
comment on column dept_action_rela.update_at is '更新时间';
comment on column dept_action_rela.delete_at is '删除时间';

-- 组织机构工位关系
drop table if exists dept_workstation_rela;
CREATE TABLE dept_workstation_rela (
  id serial PRIMARY KEY,
  dept_id integer,
  workstation_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
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
  model_id integer,
  tool_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table model_tool_rela is '机种治工具关系';
comment on column model_tool_rela.model_id is '机种ID';
comment on column model_tool_rela.tool_id is '治工具ID';
CREATE UNIQUE INDEX model_tool_rela_uniq ON model_tool_rela (model_id,tool_id) WHERE delete_at IS NULL;
comment on column model_tool_rela.create_by is '创建者ID';
comment on column model_tool_rela.create_at is '创建时间';
comment on column model_tool_rela.update_by is '更新者ID';
comment on column model_tool_rela.update_at is '更新时间';
comment on column model_tool_rela.delete_at is '删除时间';

-- 机种工位关系表
drop table if exists model_workstation_rela;
CREATE TABLE model_workstation_rela (
  id serial PRIMARY KEY,
  model_id integer,
  workstation_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table model_workstation_rela is '机种工位关系表';
comment on column model_workstation_rela.model_id is '机种ID';
comment on column model_workstation_rela.workstation_id is '工位ID';
CREATE UNIQUE INDEX model_workstation_rela_uniq ON model_workstation_rela (model_id,workstation_id) WHERE delete_at IS NULL;
comment on column model_workstation_rela.create_by is '创建者ID';
comment on column model_workstation_rela.create_at is '创建时间';
comment on column model_workstation_rela.update_by is '更新者ID';
comment on column model_workstation_rela.update_at is '更新时间';
comment on column model_workstation_rela.delete_at is '删除时间';

-- 机种部品关系
drop table if exists model_part_rela;
CREATE TABLE model_part_rela (
  id serial PRIMARY KEY,
  model_id integer,
  part_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table model_part_rela is '机种部品关系';
comment on column model_part_rela.model_id is '机种ID';
comment on column model_part_rela.part_id is '部品ID';
CREATE UNIQUE INDEX model_part_rela_uniq ON model_part_rela (model_id,part_id) WHERE delete_at IS NULL;
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
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table report_group_report_rela is '报表组报表关系';
comment on column report_group_report_rela.report_group_id is '报表组ID';
comment on column report_group_report_rela.report_id is '报表ID';
CREATE UNIQUE INDEX report_group_report_rela_uniq ON report_group_report_rela (report_group_id,report_id) WHERE delete_at IS NULL;
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
  a0 integer,
  b0 integer,
  g0 integer,
  a1 integer,
  b1 integer,
  p0 integer,
  m0 integer,
  x0 integer,
  i0 integer,
  tool varchar(64),
  a2 integer,
  b2 integer,
  p1 integer,
  a3 integer,
  a4 integer,
  b3 integer,
  p2 integer,
  a5 integer,
  dept_id integer,
  used_count integer,
  frequency integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
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
comment on column measure_group.dept_id is '组织机构ID';
CREATE UNIQUE INDEX measure_group_uniq ON measure_group (code,dept_id) WHERE delete_at IS NULL;
comment on column measure_group.used_count is '使用次数统计';
comment on column measure_group.create_by is '创建者ID';
comment on column measure_group.create_at is '创建时间';
comment on column measure_group.update_by is '更新者ID';
comment on column measure_group.update_at is '更新时间';
comment on column measure_group.delete_at is '删除时间';

-- 手顺及指标值组合
drop table if exists opertaion_group;
CREATE TABLE opertaion_group (
  id serial PRIMARY KEY,
  code varchar(64),
  dept_id integer,
  frequency integer,
  remark varchar(128),
  used_count integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table opertaion_group is '手顺及指标值组合';
comment on column opertaion_group.code is '编码';
comment on column opertaion_group.dept_id is '组织机构ID';
CREATE UNIQUE INDEX opertaion_group_uniq ON opertaion_group (code,dept_id) WHERE delete_at IS NULL;
comment on column opertaion_group.remark is '备注';
comment on column opertaion_group.used_count is '使用次数统计';
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
  key varchar(128),
  a0 integer,
  b0 integer,
  g0 integer,
  a1 integer,
  b1 integer,
  p0 integer,
  m0 integer,
  x0 integer,
  i0 integer,
  tool varchar(64),
  a2 integer,
  b2 integer,
  p1 integer,
  a3 integer,
  a4 integer,
  b3 integer,
  p2 integer,
  a5 integer,
  pinyin varchar(128),
  measures varchar(17),
  frequency integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table operation_group_operation is '手顺';
comment on column operation_group_operation.operation_group_id is '手顺组合ID';
comment on column operation_group_operation.seq_number is '序号';
comment on column operation_group_operation.operation is '手顺';
comment on column operation_group_operation.pinyin is '拼音';
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
  update_at timestamp default now(),
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
  type varchar(16),
  a0 integer,
  b0 integer,
  g0 integer,
  a1 integer,
  b1 integer,
  p0 integer,
  m0 integer,
  x0 integer,
  i0 integer,
  tool varchar(64),
  a2 integer,
  b2 integer,
  p1 integer,
  a3 integer,
  a4 integer,
  b3 integer,
  p2 integer,
  a5 integer,
  frequency integer,
  time_value decimal(18, 5),
  TMU decimal(18, 5),
  second_convert decimal(18, 5),
  remark varchar(512),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table work_operations is '分析表明细';
comment on column work_operations.seq_number is '序号';
comment on column work_operations.work_book_id is '分析表ID';
comment on column work_operations.version is '版本信息';
comment on column work_operations.operation is '手顺';
comment on column work_operations.type is '类型';
comment on column work_operations.a0 is 'A0';
comment on column work_operations.b0 is 'B0';
comment on column work_operations.g0 is 'G0';
comment on column work_operations.a1 is 'A1';
comment on column work_operations.b1 is 'B1';
comment on column work_operations.p0 is 'P0';
comment on column work_operations.m0 is 'M0';
comment on column work_operations.x0 is 'X0';
comment on column work_operations.i0 is 'I0';
comment on column work_operations.a2 is 'A2';
comment on column work_operations.b2 is 'B2';
comment on column work_operations.p1 is 'P1';
comment on column work_operations.a3 is 'A3';
comment on column work_operations.a4 is 'A4';
comment on column work_operations.b3 is 'B3';
comment on column work_operations.p2 is 'P2';
comment on column work_operations.a5 is 'A5';
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  unit_name varchar(64) NULL,
  total decimal(10, 2),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
  delete_at timestamp
);
comment on table collection_most_value_item is 'Collection - MOST Value 表';
comment on column collection_most_value_item.collection_most_value_id is 'Most Value表ID';
comment on column collection_most_value_item.type is '类型:Sub/Man/测试、检查/捆包';
comment on column collection_most_value_item.work_name is '作业名';
comment on column collection_most_value_item.unit_name is '组合名称';
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
  phase_id integer,
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
comment on column report_time_contact.stlst is 'ST/LST';
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  update_at timestamp default now(),
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
  model_id integer,
  phase_id integer,
  stlst varchar(128),
  report_group_id integer,
  next_approver_id integer,
  status varchar(32),
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
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
  model_id integer,
  phase_id integer,
  stlst varchar(128),
  result varchar(32),
  opinion varchar(128),
  report_group_id integer,
  next_approver_id integer,
  create_by integer,
  create_at timestamp default now(),
  update_by integer,
  update_at timestamp default now(),
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

-- ----------------------------
-- Records of basic_staff
-- ----------------------------
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 1, 1, 1, 'admin', 'admin', 'admin', '0', '1', '在职', '无', '2019-10-15', '1', '1', 7, '2019-10-15 15:15:44', 1, '2019-10-15 15:15:47', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, NULL, 1, 3, '1', '罗钰', 'luozuo', NULL, NULL, NULL, NULL, '2019-11-01', NULL, NULL, 1, '2019-11-24 11:26:49.375', NULL, '2019-11-24 11:26:49.375', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, NULL, 1, 4, '2', '何福睿', 'hefuzuo', NULL, NULL, NULL, NULL, '2019-11-01', NULL, NULL, 1, '2019-11-24 11:27:15.329', NULL, '2019-11-24 11:27:15.329', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, NULL, 1, 5, '3', '周述文', 'zhoushuwen', NULL, NULL, NULL, NULL, '2019-11-01', NULL, NULL, 1, '2019-11-24 11:27:45.24', NULL, '2019-11-24 11:27:45.24', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, NULL, 1, 6, '4', '熊驰', 'xiongchi', NULL, NULL, NULL, NULL, '2019-11-01', NULL, NULL, 1, '2019-11-24 11:28:08.506', NULL, '2019-11-24 11:28:08.506', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (22, NULL, 1, 40, 'CN001153', 'Susan', 'Susan', NULL, NULL, NULL, NULL, '2019-12-14', NULL, NULL, 1, '2019-12-14 10:10:19.323', NULL, '2019-12-14 10:10:19.323', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (23, NULL, 1, 41, 'CN007185', '徐其洪', 'xuqihong', NULL, NULL, NULL, NULL, '2019-12-05', NULL, NULL, 1, '2019-12-14 10:48:10.853', NULL, '2019-12-14 10:48:10.853', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (24, NULL, 5, 45, 'CN011051', '谭波', 'tanbo', NULL, '8832', NULL, NULL, '2019-12-15', 'tan.bo@esl.epson.com.hk', 'E000020727', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (25, NULL, 5, 46, 'CN004859', '施忠', 'shizhong', NULL, '8634', NULL, NULL, '2019-12-15', 'shi.zhong@esl.epson.com.hk', 'E000032539', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (26, NULL, 5, 47, 'CN003108', '刘秀兰', 'liuxiulan', NULL, '8634', NULL, NULL, '2019-12-15', 'liu.xiu.lan@esl.epson.com.hk', 'E000010836', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (27, NULL, 5, 48, 'CN004447', '聂丽梅', 'nielimei', NULL, '8634', NULL, NULL, '2019-12-15', 'nie.li.mei@esl.epson.com.hk', 'E000003018', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (28, NULL, 4, 49, 'CN001018', '庞素英', 'pangsuying', NULL, '8471', NULL, NULL, '2019-12-15', 'Pang.Su.Ying@esl.epson.com.hk', 'E000015822', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (29, NULL, 4, 50, 'CN005068', '黄秀莉', 'huangxiuli', NULL, '8477', NULL, NULL, '2019-12-15', 'Huang.Xiu.Li@esl.epson.com.hk', 'E000032041', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (30, NULL, 4, 51, 'CN000881', '刘玉春', 'liuyuchun', NULL, '8323', NULL, NULL, '2019-12-15', 'Liu.Yu.Chun@esl.epson.com.hk', 'E000009064', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (31, NULL, 4, 52, 'CN003029', '沈繁华', 'shenfanhua', NULL, '8453', NULL, NULL, '2019-12-15', 'Shen.Fan.Hua@esl.epson.com.hk', 'E000015577', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (32, NULL, 4, 53, 'CN006991', '李蝶', 'lidie', NULL, '8212', NULL, NULL, '2019-12-15', 'Li.Die@esl.epson.com.hk', 'E000029363', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (33, NULL, 4, 54, 'CN007248', '罗纳', 'luona', NULL, '8453', NULL, NULL, '2019-12-15', 'Luo.Na@esl.epson.com.hk', 'E000036270', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (34, NULL, 4, 55, 'CN006322', '雷佳丽', 'leijiali', NULL, '8453', NULL, NULL, '2019-12-15', 'Lei.Jia.Li@esl.epson.com.hk', 'E000036404', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (35, NULL, 4, 56, 'CN005946', '孟辞红', 'mengcihong', NULL, '8453', NULL, NULL, '2019-12-15', 'Meng.Ci.Hong@esl.epson.com.hk', 'E000037271', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (36, NULL, 4, 57, 'CN006904', '陆善丹', 'luxiangdan', NULL, '8212', NULL, NULL, '2019-12-15', 'Lu.Shan.Dan@esl.epson.com.hk', 'E000047078', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (37, NULL, 4, 58, 'CN007965', '向美奥', 'xiangmeiao', NULL, '8212', NULL, NULL, '2019-12-15', 'Xiang.Mei.Ao@esl.epson.com.hk', 'E000058287', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (38, NULL, 4, 59, 'CN006905', '潘丽丽', 'panlili', NULL, '8450', NULL, NULL, '2019-12-15', 'Pan.Li.Li@esl.epson.com.hk', 'E000058841', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (39, NULL, 4, 60, 'CN005371', '张小凤', 'zhangxiaofeng', NULL, '8429', NULL, NULL, '2019-12-15', 'Zhang.Xiao.Feng@esl.epson.com.hk', 'E000020497', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (40, NULL, 4, 61, 'E000060049', '刘竟', 'liujing', NULL, '8429', NULL, NULL, '2019-12-15', 'Liu.Jing@esl.epson.com.hk', 'E000060049', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (41, NULL, 6, 62, 'CN006386', '叶贵平', 'yeguiping', NULL, '8190', NULL, NULL, '2019-12-15', 'Ye.Gui.Ping@esl.epson.com.hk', 'E000037184', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (42, NULL, 6, 63, 'CN008052', '俸良华', 'fenglianghua', NULL, '8190', NULL, NULL, '2019-12-15', 'Feng.Liang.Hua@esl.epson.com.hk', 'E000069875', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (43, NULL, 6, 64, 'CN000614', '郑华军', 'zhenghuajun', NULL, '8279', NULL, NULL, '2019-12-15', 'Zheng.Hua.Jun@esl.epson.com.hk', 'E00007261', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (44, NULL, 6, 65, 'CN001552', '张代新', 'zhangdaixin', NULL, '8190', NULL, NULL, '2019-12-15', 'Zhang.Dai.Xin@esl.epson.com.hk', 'E000016395', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (45, NULL, 6, 66, 'CN000521', '黄春龙', 'huangchunlong', NULL, '8542', NULL, NULL, '2019-12-15', 'Huang.Chun.Long@esl.epson.com.hk', 'E000017558', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (46, NULL, 7, 67, 'CN006659', '林国军', 'linguojun', NULL, '8709', NULL, NULL, '2019-12-15', 'Lin.Guo.Jun@esl.epson.com.hk', 'E000055625', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, NULL, 7, 68, 'CN004574', '盘冬华', 'pandonghua', NULL, '8652', NULL, NULL, '2019-12-15', 'Pan.Dong.Hua@esl.epson.com.hk', 'E000026730', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, NULL, 7, 69, 'CN006439', '夏琼', 'xiaqiong', NULL, '8652', NULL, NULL, '2019-12-15', 'Xia.Qiong@esl.epson.com.hk', 'E000039630', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, NULL, 7, 70, 'CN000734', '李晓汉', 'lixiaohan', NULL, '8636', NULL, NULL, '2019-12-15', 'Li.Xiao.Han@esl.epson.com.hk', 'E000018476', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, NULL, 7, 71, 'CN007211', '金学哲', 'jinxuezhe', NULL, '8656', NULL, NULL, '2019-12-15', 'Kin.Gakutetsu@esl.epson.com.hk', 'J000000289', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, NULL, 7, 72, 'CN004971', '邓雲燕', 'dengyunyan', NULL, '8653', NULL, NULL, '2019-12-15', 'Deng.Yun.Yan@esl.epson.com.hk', 'E000027831', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (52, NULL, 7, 73, 'CN007250', '邓木清', 'dengmuqing', NULL, '8652', NULL, NULL, '2019-12-15', 'Deng.Mu.Qing@esl.epson.com.hk', 'E000036395', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (53, NULL, 8, 74, 'CN003387', '吕鹏云', 'lvpengyun', NULL, '8766', NULL, NULL, '2019-12-15', 'Lv.Peng.Yun@esl.epson.com.hk', 'E000027701', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
INSERT INTO "public"."basic_staff"("id", "job_id", "dept_id", "user_id", "code", "name", "pinyin", "gender", "mobilephone", "status", "remark", "employment_date", "email", "job_number", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (54, NULL, 5, 75, 'CN001520', '曾辉', 'zenghui', NULL, '8756', NULL, NULL, '2019-12-15', 'Zeng.Hui@esl.epson.com.hk', 'E000020701', 1, '2019-12-15 23:01:03.81', NULL, '2019-12-15 23:01:03.81', NULL);
select setval('basic_staff_id_seq',55);


-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO sys_config VALUES (1, 'PaymentExcelPath', '/home/ubuntu/excel/', 1, '薪酬根据excel模板导出，配置生成excel缓存路径', 3, '2019-01-28 03:24:12', 3, '2019-02-20 17:22:27', '1970-01-01 00:00:00');
INSERT INTO sys_config VALUES (2, 'MessageExpire', '1440', 1, '待处理消息超期处理分钟配置（整数）', 1, '2018-12-27 09:44:19', 73, '2019-02-22 09:21:45', '1970-01-01 00:00:00');


-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 0, 'ESL', 0, 0, '0', 'bloc', '1', 'lst', 1, '2019-11-12 13:57:01', NULL, NULL, NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, 1, 'P制造中心', 0, 0, 'PMC', NULL, NULL, NULL, 3, '2019-12-15 21:09:11.021', NULL, '2019-12-15 21:09:11.021', NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 2, '完成品制造部', 0, 0, 'P-01', NULL, NULL, NULL, 3, '2019-12-15 21:10:59', NULL, '2019-12-15 21:10:59', NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 1, 'VP制造中心', 1, 0, 'VPMC', NULL, NULL, NULL, 3, '2019-12-15 21:09:54.219', 3, '2019-12-15 21:14:08.007', NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 3, '组立技术', 1, 0, 'VP-01', NULL, NULL, NULL, 3, '2019-12-15 21:14:43.254', NULL, '2019-12-15 21:14:43.254', NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 2, '要素', 2, 0, 'P-02', NULL, NULL, NULL, 3, '2019-12-15 21:15:04.395', NULL, '2019-12-15 21:15:04.395', NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 2, '效率化投进部', 1, 0, 'VP-01', NULL, NULL, NULL, 3, '2019-12-15 21:14:43.254', NULL, '2019-12-15 21:14:43.254', NULL);
INSERT INTO "public"."sys_dept"("id", "parent_id", "name", "order_num", "del_flag", "dept_code", "dept_type", "dept_level", "stlst", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 2, '安全基盘管理部', 2, 0, 'P-02', NULL, NULL, NULL, 3, '2019-12-15 21:15:04.395', NULL, '2019-12-15 21:15:04.395', NULL);
select setval('sys_dept_id_seq',9);
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
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, 31, '人员信息', 'basic/staff', NULL, 1, NULL, 4, 1, '2017-03-23 22:37:41', 2, '2019-01-04 13:36:19', NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, 47, '查看', NULL, 'basic:staff:list,basic:staff:info', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, 47, '新增', NULL, 'basic:staff:save', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, 47, '修改', NULL, 'basic:staff:update', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, 47, '删除', NULL, 'basic:staff:delete', 2, NULL, 1, 1, '2017-03-23 22:37:41', NULL, NULL, NULL);
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
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (403, 401, '新增', NULL, 'masterData:approveopininon:create', 2, NULL, 1, NULL, '2019-11-08 15:31:05.448949', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (404, 401, '修改', NULL, 'masterData:approveopininon:update', 2, NULL, 1, NULL, '2019-11-08 15:31:05.463064', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (405, 401, '删除', NULL, 'masterData:approveopininon:delete', 2, NULL, 1, NULL, '2019-11-08 15:31:05.479702', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (416, 307, '部品', 'masterdata/part', NULL, 1, 'config', 1, NULL, '2019-11-08 15:34:28.409888', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (417, 416, '查看', NULL, 'masterData:part:list,masterData:part:info', 2, NULL, 1, NULL, '2019-11-08 15:35:07.086872', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (418, 416, '新增', NULL, 'masterData:part:create', 2, NULL, 1, NULL, '2019-11-08 15:35:07.109234', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (419, 416, '修改', NULL, 'masterData:part:update', 2, NULL, 1, NULL, '2019-11-08 15:35:07.120316', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (420, 416, '删除', NULL, 'masterData:part:delete', 2, NULL, 1, NULL, '2019-11-08 15:35:07.130856', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (421, 307, '生产阶段', 'masterdata/phase', NULL, 1, 'config', 1, NULL, '2019-11-08 15:35:16.331639', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (422, 421, '查看', NULL, 'masterData:phase:list,masterData:phase:info', 2, NULL, 1, NULL, '2019-11-08 15:35:30.164927', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (423, 421, '新增', NULL, 'masterData:phase:create', 2, NULL, 1, NULL, '2019-11-08 15:35:30.176979', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (424, 421, '修改', NULL, 'masterData:phase:update', 2, NULL, 1, NULL, '2019-11-08 15:35:30.189303', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (425, 421, '删除', NULL, 'masterData:phase:delete', 2, NULL, 1, NULL, '2019-11-08 15:35:30.200429', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (426, 307, '报表', 'masterdata/report', NULL, 1, 'config', 1, NULL, '2019-11-08 15:35:37.399362', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (427, 426, '查看', NULL, 'masterData:report:list,masterData:report:info', 2, NULL, 1, NULL, '2019-11-08 15:35:56.494035', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (428, 426, '新增', NULL, 'masterData:report:create', 2, NULL, 1, NULL, '2019-11-08 15:35:56.504269', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (429, 426, '修改', NULL, 'masterData:report:update', 2, NULL, 1, NULL, '2019-11-08 15:35:56.515729', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (430, 426, '删除', NULL, 'masterData:report:delete', 2, NULL, 1, NULL, '2019-11-08 15:35:56.525045', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (431, 307, '报表组', 'masterdata/reportgroup', NULL, 1, 'config', 1, NULL, '2019-11-08 15:35:56.534885', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (432, 431, '查看', NULL, 'masterData:reportgroup:list,masterData:reportgroup:info', 2, NULL, 1, NULL, '2019-11-08 15:36:11.392194', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (433, 431, '新增', NULL, 'masterData:reportgroup:create', 2, NULL, 1, NULL, '2019-11-08 15:36:11.403334', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (434, 431, '修改', NULL, 'masterData:reportgroup:update', 2, NULL, 1, NULL, '2019-11-08 15:36:11.413983', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (435, 431, '删除', NULL, 'masterData:reportgroup:delete', 2, NULL, 1, NULL, '2019-11-08 15:36:11.427339', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (436, 307, '治工具', 'masterdata/tool', NULL, 1, 'config', 1, NULL, '2019-11-08 15:36:11.435771', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (437, 436, '查看', NULL, 'masterData:tool:list,masterData:tool:info', 2, NULL, 1, NULL, '2019-11-08 15:36:25.710539', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (438, 436, '新增', NULL, 'masterData:tool:create', 2, NULL, 1, NULL, '2019-11-08 15:36:25.724866', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (439, 436, '修改', NULL, 'masterData:tool:update', 2, NULL, 1, NULL, '2019-11-08 15:36:25.738336', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (440, 436, '删除', NULL, 'masterData:tool:delete', 2, NULL, 1, NULL, '2019-11-08 15:36:25.751515', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (446, 307, '工位类型', 'masterdata/workstationtype', NULL, 1, 'config', 1, NULL, '2019-11-08 15:36:40.808149', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (447, 446, '查看', NULL, 'masterData:workstationtype:list,masterData:workstationtype:info', 2, NULL, 1, NULL, '2019-11-08 15:36:55.327124', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (448, 446, '新增', NULL, 'masterData:workstationtype:create', 2, NULL, 1, NULL, '2019-11-08 15:36:55.336529', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (449, 446, '修改', NULL, 'masterData:workstationtype:update', 2, NULL, 1, NULL, '2019-11-08 15:36:55.34815', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (450, 446, '删除', NULL, 'masterData:workstationtype:delete', 2, NULL, 1, NULL, '2019-11-08 15:36:55.361574', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (451, 307, '工位', 'masterdata/workstation', NULL, 1, 'config', 1, NULL, '2019-11-08 15:36:55.373083', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (452, 451, '查看', NULL, 'masterData:workstation:list,masterData:workstation:info', 2, NULL, 1, NULL, '2019-11-08 15:37:07.105426', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (453, 451, '新增', NULL, 'masterData:workstation:create', 2, NULL, 1, NULL, '2019-11-08 15:37:07.128195', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (454, 451, '修改', NULL, 'masterData:workstation:update', 2, NULL, 1, NULL, '2019-11-08 15:37:07.138884', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (455, 451, '删除', NULL, 'masterData:workstation:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:07.149442', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (461, 307, '机种', 'masterdata/model', NULL, 1, 'config', 1, NULL, '2019-11-08 15:37:18.835141', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (462, 461, '查看', NULL, 'masterData:model:list,masterData:model:info', 2, NULL, 1, NULL, '2019-11-08 15:37:28.296369', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (463, 461, '新增', NULL, 'masterData:model:create', 2, NULL, 1, NULL, '2019-11-08 15:37:28.311712', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (464, 461, '修改', NULL, 'masterData:model:update', 2, NULL, 1, NULL, '2019-11-08 15:37:28.325631', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (465, 461, '删除', NULL, 'masterData:model:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:28.339652', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (471, 307, '机种系列', 'masterdata/modelseries', NULL, 1, 'config', 1, NULL, '2019-11-08 15:37:43.16794', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (472, 471, '查看', NULL, 'masterData:modelseries:list,masterData:modelseries:info', 2, NULL, 1, NULL, '2019-11-08 15:37:55.540469', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (473, 471, '新增', NULL, 'masterData:modelseries:create', 2, NULL, 1, NULL, '2019-11-08 15:37:55.551621', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (474, 471, '修改', NULL, 'masterData:modelseries:update', 2, NULL, 1, NULL, '2019-11-08 15:37:55.562454', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (475, 471, '删除', NULL, 'masterData:modelseries:delete', 2, NULL, 1, NULL, '2019-11-08 15:37:55.570677', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (481, 307, '关键词', 'masterdata/action', NULL, 1, 'config', 1, NULL, '2019-11-08 15:38:04.642688', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (482, 481, '查看', NULL, 'masterData:action:list,masterData:opertaion:info', 2, NULL, 1, NULL, '2019-11-08 15:38:27.685012', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (483, 481, '新增', NULL, 'masterData:action:create', 2, NULL, 1, NULL, '2019-11-08 15:38:27.698335', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (484, 481, '修改', NULL, 'masterData:action:update', 2, NULL, 1, NULL, '2019-11-08 15:38:27.707977', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (485, 481, '删除', NULL, 'masterData:action:delete', 2, NULL, 1, NULL, '2019-11-08 15:38:27.716807', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (486, 0, '分析表', 'workbook/workbook', NULL, 1, 'config', 1, NULL, '2019-11-11 13:22:59.903762', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (487, 486, '查看', NULL, 'workBook:workbook:list,workBook:workbook:info', 2, NULL, 1, NULL, '2019-11-11 13:23:32.709518', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (488, 486, '新增', NULL, 'workBook:workbook:create', 2, NULL, 1, NULL, '2019-11-11 13:23:41.509737', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (489, 486, '修改', NULL, 'workBook:workbook:update', 2, NULL, 1, NULL, '2019-11-11 13:23:47.071175', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (490, 486, '删除', NULL, 'workBook:workbook:delete', 2, NULL, 1, NULL, '2019-11-11 13:23:54.024564', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (496, 307, '手顺及指标值组合', 'masterdata/opertaiongroup', NULL, 1, 'config', 1, NULL, '2019-11-11 13:27:18.335923', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (497, 496, '查看', NULL, 'masterData:opertaiongroup:list,masterData:opertaiongroup:info', 2, NULL, 1, NULL, '2019-11-11 13:27:41.834855', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (498, 496, '新增', NULL, 'masterData:opertaiongroup:create', 2, NULL, 1, NULL, '2019-11-11 13:27:52.906009', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (499, 496, '修改', NULL, 'masterData:opertaiongroup:update', 2, NULL, 1, NULL, '2019-11-11 13:28:03.767979', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (500, 496, '删除', NULL, 'masterData:opertaiongroup:delete', 2, NULL, 1, NULL, '2019-11-11 13:28:13.135872', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (501, 307, '常用指标值组合', 'masterdata/measuregroup', NULL, 1, 'config', 1, NULL, '2019-11-11 13:28:30.528337', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (502, 501, '查看', NULL, 'masterData:measuregroup:list,masterData:measuregroup:info', 2, NULL, 1, NULL, '2019-11-11 13:28:45.473317', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (503, 501, '新增', NULL, 'masterData:measuregroup:create', 2, NULL, 1, NULL, '2019-11-11 13:28:54.230925', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (504, 501, '修改', NULL, 'masterData:measuregroup:update', 2, NULL, 1, NULL, '2019-11-11 13:29:03.722732', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (505, 501, '删除', NULL, 'masterData:measuregroup:delete', 2, NULL, 1, NULL, '2019-11-11 13:29:12.484948', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (506, 0, '报表', NULL, NULL, 0, 'basic', 2, 1, '2019-11-15 11:02:22', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (508, 506, '时间联络表', 'report/timecontact', NULL, 1, 'config', 1, NULL, '2019-11-26 13:46:45.943343', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (509, 508, '查看', NULL, 'report:timecontact:list,report:timecontact:detail', 2, NULL, 6, NULL, '2019-11-26 13:47:06.019726', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (510, 508, '新增', NULL, 'report:timecontact:create', 2, NULL, 6, NULL, '2019-11-26 13:47:06.020467', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (511, 508, '修改', NULL, 'report:timecontact:update', 2, NULL, 6, NULL, '2019-11-26 13:47:06.021322', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (512, 508, '删除', NULL, 'report:timecontact:delete', 2, NULL, 6, NULL, '2019-11-26 13:47:06.022258', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (513, 506, '报表审批表', 'report/approve', NULL, 1, 'config', 2, NULL, '2019-11-26 13:52:46.504468', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (514, 513, '查看', NULL, 'report:approve:list,report:approve:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.505038', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (515, 513, '新增', NULL, 'report:approve:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.505587', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (516, 513, '修改', NULL, 'report:approve:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.506021', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (517, 513, '删除', NULL, 'report:approve:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.507049', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (518, 506, '标准时间表', 'report/standardtime', NULL, 1, 'config', 3, NULL, '2019-11-26 13:52:46.508018', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (519, 518, '查看', NULL, 'report:standardtime:list,report:standardtime:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.508592', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (520, 518, '新增', NULL, 'report:standardtime:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.509264', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (521, 518, '修改', NULL, 'report:standardtime:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.510909', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (522, 518, '删除', NULL, 'report:standardtime:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.511405', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (523, 506, '报表审批历史表', 'report/approvehistory', NULL, 1, 'config', 4, NULL, '2019-11-26 13:52:46.511998', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (524, 523, '查看', NULL, 'report:approvehistory:list,report:approvehistory:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.512555', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (525, 523, '新增', NULL, 'report:approvehistory:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.513277', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (526, 523, '修改', NULL, 'report:approvehistory:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.514423', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (527, 523, '删除', NULL, 'report:approvehistory:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.515008', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (528, 506, '履历表', 'report/changerecord', NULL, 1, 'config', 5, NULL, '2019-11-26 13:52:46.515635', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (529, 528, '查看', NULL, 'report:changerecord:list,report:changerecord:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.516874', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (530, 528, '新增', NULL, 'report:changerecord:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.517893', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (531, 528, '修改', NULL, 'report:changerecord:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.518385', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (532, 528, '删除', NULL, 'report:changerecord:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.520527', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (533, 506, 'Total表', 'report/total', NULL, 1, 'config', 6, NULL, '2019-11-26 13:52:46.53228', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (534, 533, '查看', NULL, 'report:total:list,report:total:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.532736', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (535, 533, '新增', NULL, 'report:total:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.533115', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (536, 533, '修改', NULL, 'report:total:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.533512', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (537, 533, '删除', NULL, 'report:total:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.533959', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (538, 506, '标准工数表', 'report/standardwork', NULL, 1, 'config', 7, NULL, '2019-11-26 13:52:46.534469', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (539, 538, '查看', NULL, 'report:standardwork:list,report:standardwork:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.535709', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (540, 538, '新增', NULL, 'report:standardwork:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.536299', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (541, 538, '修改', NULL, 'report:standardwork:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.536835', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (542, 538, '删除', NULL, 'report:standardwork:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.537224', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (543, 506, '工位时间表', 'report/stationtime', NULL, 1, 'config', 11, NULL, '2019-11-26 13:52:46.537688', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (544, 543, '查看', NULL, 'collection:stationtime:list,collection:stationtime:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.538121', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (545, 543, '新增', NULL, 'collection:stationtime:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.539143', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (546, 543, '修改', NULL, 'collection:stationtime:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.539676', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (547, 543, '删除', NULL, 'collection:stationtime:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.540352', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (548, 506, 'Revision History 表', 'report/revisionhistory', NULL, 1, 'config', 8, NULL, '2019-11-26 13:52:46.501702', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (549, 548, '查看', NULL, 'collection:revisionhistory:list,collection:revisionhistory:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.502563', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (550, 548, '新增', NULL, 'collection:revisionhistory:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.503193', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (551, 548, '修改', NULL, 'collection:revisionhistory:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.503625', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (552, 548, '删除', NULL, 'collection:revisionhistory:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.504038', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (553, 506, 'Compare表', 'report/compare', NULL, 1, 'config', 9, NULL, '2019-11-26 13:52:46.521058', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (554, 553, '查看', NULL, 'collection:compare:list,collection:compare:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.522905', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (555, 553, '新增', NULL, 'collection:compare:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.52449', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (556, 553, '修改', NULL, 'collection:compare:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.525031', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (557, 553, '删除', NULL, 'collection:compare:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.527074', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (558, 506, 'MOST Value 表', 'report/mostvalue', NULL, 1, 'config', 10, NULL, '2019-11-26 13:52:46.527628', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (559, 558, '查看', NULL, 'collection:mostvalue:list,collection:mostvalue:detail', 2, NULL, 6, NULL, '2019-11-26 13:52:46.529281', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (560, 558, '新增', NULL, 'collection:mostvalue:create', 2, NULL, 6, NULL, '2019-11-26 13:52:46.530622', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (561, 558, '修改', NULL, 'collection:mostvalue:update', 2, NULL, 6, NULL, '2019-11-26 13:52:46.531263', NULL, NULL, NULL);
INSERT INTO "public"."sys_menu"("id", "parent_id", "name", "url", "perms", "type", "icon", "order_num", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (562, 558, '删除', NULL, 'collection:mostvalue:delete', 2, NULL, 6, NULL, '2019-11-26 13:52:46.53181', NULL, NULL, NULL);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role"("id", "role_name", "dept_id", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 'admin', 1, NULL, 1, '2019-02-25 10:13:01', 1, '2019-11-24 11:28:35.273', '1970-01-01 00:00:00');
INSERT INTO "public"."sys_role"("id", "role_name", "dept_id", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, '测试', 1, NULL, 1, '2019-11-26 17:46:55.443', 1, '2019-11-26 17:52:16.252', NULL);
INSERT INTO "public"."sys_role"("id", "role_name", "dept_id", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (15, '中心管理员', 1, NULL, 1, '2019-12-16 15:06:03.104', NULL, '2019-12-16 15:06:03.104', NULL);
select setval('sys_role_id_seq',16);


-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 1, 'admin', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'root@yu.io', '13800138000', 1, 1, '2016-11-11 11:11:11', NULL, NULL, '1970-01-01 00:00:00');
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 1, 'roy', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', NULL, NULL, 1, 1, '2019-11-24 11:26:49.366', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 1, 'freedom', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', NULL, NULL, 1, 1, '2019-11-24 11:27:15.321', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 1, 'zsw', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', NULL, NULL, 1, 1, '2019-11-24 11:27:45.231', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 1, 'xc', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', NULL, NULL, 1, 1, '2019-11-24 11:28:08.498', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (40, 1, 'CN001153', 'f6a3316be6e3ced9fa071727d2f40b17a728f41a82fa09136c30bf18b1ad23b9', 'vzDuwtuXr7ihRXkozbbr', NULL, NULL, 1, 1, '2019-12-14 10:10:19.303', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (41, 1, 'CN007185', '641e504857bbe91687f1baa745eec38ab50c20d69e8eaab8882a2c619e4dc9ba', 'yiNmXqtc1z27PQv6ghmR', NULL, NULL, 1, 1, '2019-12-14 10:48:10.847', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (45, 5, 'CN011051', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'tanbo@es.com', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (46, 5, 'CN004859', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'shi.zhong@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, 5, 'CN003108', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'liu.xiu.lan@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, 5, 'CN004447', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'nie.li.mei@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, 4, 'CN001018', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Pang.Su.Ying@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, 4, 'CN005068', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Huang.Xiu.Li@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, 4, 'CN000881', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Liu.Yu.Chun@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (52, 4, 'CN003029', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Shen.Fan.Hua@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (53, 4, 'CN006991', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Li.Die@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (54, 4, 'CN007248', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Luo.Na@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (55, 4, 'CN006322', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Lei.Jia.Li@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (56, 4, 'CN005946', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Meng.Ci.Hong@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (57, 4, 'CN006904', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Lu.Shan.Dan@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (58, 4, 'CN007965', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Xiang.Mei.Ao@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (59, 4, 'CN006905', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Pan.Li.Li@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (60, 4, 'CN005371', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Zhang.Xiao.Feng@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (61, 4, 'E000060049', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Liu.Jing@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (62, 6, 'CN006386', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Ye.Gui.Ping@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (63, 6, 'CN008052', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Feng.Liang.Hua@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (64, 6, 'CN000614', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Zheng.Hua.Jun@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (65, 6, 'CN001552', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Zhang.Dai.Xin@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (66, 6, 'CN000521', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Huang.Chun.Long@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (67, 7, 'CN006659', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Lin.Guo.Jun@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (68, 7, 'CN004574', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Pan.Dong.Hua@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (69, 7, 'CN006439', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Xia.Qiong@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (70, 7, 'CN000734', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Li.Xiao.Han@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (71, 7, 'CN007211', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Kin.Gakutetsu@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (72, 7, 'CN004971', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Deng.Yun.Yan@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (73, 7, 'CN007250', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Deng.Mu.Qing@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (74, 8, 'CN003387', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Lv.Peng.Yun@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
INSERT INTO "public"."sys_user"("id", "dept_id", "username", "password", "salt", "email", "mobile", "status", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (75, 5, 'CN001520', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'Zeng.Hui@esl.epson.com.hk', '', 1, 1, '2019-12-15 23:01:03.781', NULL, NULL, NULL);
select setval('sys_user_id_seq',76);




INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (1, 1, 1);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (2, 1, 2);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (3, 1, 15);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (4, 1, 16);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (5, 1, 17);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (6, 1, 18);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (7, 1, 292);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (8, 1, 3);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (9, 1, 19);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (10, 1, 20);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (11, 1, 21);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (12, 1, 22);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (13, 1, 4);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (14, 1, 23);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (15, 1, 24);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (16, 1, 25);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (17, 1, 26);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (18, 1, 5);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (19, 1, 6);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (20, 1, 7);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (21, 1, 8);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (22, 1, 9);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (23, 1, 10);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (24, 1, 11);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (25, 1, 12);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (26, 1, 13);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (27, 1, 14);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (28, 1, 27);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (29, 1, 303);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (30, 1, 304);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (31, 1, 305);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (32, 1, 306);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (33, 1, 29);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (34, 1, 30);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (35, 1, 77);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (36, 1, 78);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (37, 1, 79);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (38, 1, 80);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (39, 1, 81);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (40, 1, 87);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (41, 1, 88);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (42, 1, 89);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (43, 1, 90);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (44, 1, 91);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (45, 1, 31);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (46, 1, 42);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (47, 1, 43);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (48, 1, 44);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (49, 1, 45);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (50, 1, 46);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (51, 1, 47);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (52, 1, 48);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (53, 1, 49);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (54, 1, 50);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (55, 1, 51);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (56, 1, 72);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (57, 1, 73);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (58, 1, 74);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (59, 1, 75);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (60, 1, 76);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (61, 1, 82);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (62, 1, 83);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (63, 1, 84);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (64, 1, 85);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (65, 1, 86);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (66, 1, 307);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (67, 1, 401);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (68, 1, 402);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (69, 1, 403);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (70, 1, 404);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (71, 1, 405);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (72, 1, 416);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (73, 1, 417);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (74, 1, 418);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (75, 1, 419);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (76, 1, 420);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (77, 1, 421);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (78, 1, 422);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (79, 1, 423);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (80, 1, 424);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (81, 1, 425);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (82, 1, 427);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (83, 1, 428);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (84, 1, 429);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (85, 1, 430);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (86, 1, 426);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (87, 1, 431);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (88, 1, 432);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (89, 1, 433);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (90, 1, 434);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (91, 1, 435);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (92, 1, 436);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (93, 1, 437);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (94, 1, 438);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (95, 1, 439);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (96, 1, 440);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (97, 1, 446);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (98, 1, 447);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (99, 1, 448);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (100, 1, 449);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (101, 1, 450);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (102, 1, 451);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (103, 1, 452);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (104, 1, 453);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (105, 1, 454);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (106, 1, 455);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (107, 1, 461);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (108, 1, 462);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (109, 1, 463);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (110, 1, 464);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (111, 1, 465);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (112, 1, 471);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (113, 1, 472);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (114, 1, 473);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (115, 1, 474);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (116, 1, 475);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (117, 1, 481);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (118, 1, 482);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (119, 1, 483);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (120, 1, 484);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (121, 1, 485);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (122, 1, 496);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (123, 1, 497);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (124, 1, 498);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (125, 1, 499);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (126, 1, 500);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (127, 1, 501);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (128, 1, 502);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (129, 1, 503);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (130, 1, 504);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (131, 1, 505);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (132, 1, 486);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (133, 1, 487);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (134, 1, 488);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (135, 1, 489);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (136, 1, 490);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (137, 1, 506);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (138, 1, 508);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (139, 1, 509);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (140, 1, 510);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (141, 1, 511);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (142, 1, 512);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (143, 1, 513);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (144, 1, 514);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (145, 1, 515);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (146, 1, 516);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (147, 1, 517);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (148, 1, 518);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (149, 1, 519);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (150, 1, 520);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (151, 1, 521);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (152, 1, 522);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (153, 1, 523);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (154, 1, 524);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (155, 1, 525);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (156, 1, 526);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (157, 1, 527);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (158, 1, 528);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (159, 1, 529);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (160, 1, 530);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (161, 1, 531);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (162, 1, 532);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (163, 1, 533);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (164, 1, 534);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (165, 1, 535);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (166, 1, 536);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (167, 1, 537);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (168, 1, 538);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (169, 1, 539);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (170, 1, 540);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (171, 1, 541);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (172, 1, 542);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (173, 1, 543);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (174, 1, 544);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (175, 1, 545);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (176, 1, 546);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (177, 1, 547);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (178, 1, 548);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (179, 1, 549);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (180, 1, 550);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (181, 1, 551);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (182, 1, 552);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (183, 1, 553);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (184, 1, 554);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (185, 1, 555);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (186, 1, 556);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (187, 1, 557);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (188, 1, 558);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (189, 1, 559);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (190, 1, 560);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (191, 1, 561);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (192, 1, 562);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (193, 1, -666666);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (430, 6, 562);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (289, 6, 47);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (290, 6, 48);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (291, 6, 49);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (292, 6, 50);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (293, 6, 51);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (294, 6, 72);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (295, 6, 73);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (296, 6, 74);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (297, 6, 75);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (298, 6, 76);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (299, 6, 82);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (300, 6, 83);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (301, 6, 84);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (302, 6, 85);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (303, 6, 86);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (304, 6, 307);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (305, 6, 401);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (306, 6, 402);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (307, 6, 403);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (308, 6, 404);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (309, 6, 405);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (310, 6, 416);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (311, 6, 417);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (312, 6, 418);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (313, 6, 419);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (314, 6, 420);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (315, 6, 421);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (316, 6, 422);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (317, 6, 423);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (318, 6, 424);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (319, 6, 425);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (320, 6, 427);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (321, 6, 428);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (322, 6, 429);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (323, 6, 430);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (324, 6, 426);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (325, 6, 431);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (326, 6, 432);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (327, 6, 433);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (328, 6, 434);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (329, 6, 435);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (330, 6, 436);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (331, 6, 437);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (332, 6, 438);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (333, 6, 439);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (334, 6, 440);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (335, 6, 446);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (336, 6, 447);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (337, 6, 448);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (338, 6, 449);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (339, 6, 450);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (340, 6, 451);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (341, 6, 452);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (342, 6, 453);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (343, 6, 454);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (344, 6, 455);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (345, 6, 461);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (346, 6, 462);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (347, 6, 463);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (348, 6, 464);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (349, 6, 465);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (350, 6, 471);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (351, 6, 472);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (352, 6, 473);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (353, 6, 474);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (354, 6, 475);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (355, 6, 481);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (356, 6, 483);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (357, 6, 484);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (358, 6, 485);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (359, 6, 482);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (360, 6, 496);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (361, 6, 497);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (362, 6, 498);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (363, 6, 499);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (364, 6, 500);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (365, 6, 501);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (366, 6, 502);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (367, 6, 503);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (368, 6, 504);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (369, 6, 505);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (370, 6, 486);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (371, 6, 487);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (372, 6, 488);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (373, 6, 489);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (374, 6, 490);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (375, 6, 506);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (376, 6, 508);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (377, 6, 509);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (378, 6, 510);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (379, 6, 511);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (380, 6, 512);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (381, 6, 513);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (382, 6, 514);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (383, 6, 515);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (384, 6, 516);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (385, 6, 517);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (386, 6, 518);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (387, 6, 519);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (388, 6, 520);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (389, 6, 521);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (390, 6, 522);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (391, 6, 523);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (392, 6, 524);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (393, 6, 525);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (394, 6, 526);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (395, 6, 527);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (396, 6, 528);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (397, 6, 529);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (398, 6, 530);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (399, 6, 531);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (400, 6, 532);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (401, 6, 533);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (402, 6, 534);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (403, 6, 535);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (404, 6, 536);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (405, 6, 537);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (406, 6, 538);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (407, 6, 539);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (408, 6, 540);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (409, 6, 541);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (410, 6, 542);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (411, 6, 543);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (412, 6, 544);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (413, 6, 545);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (414, 6, 546);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (415, 6, 547);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (416, 6, 548);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (417, 6, 549);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (418, 6, 550);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (419, 6, 551);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (420, 6, 552);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (421, 6, 553);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (422, 6, 554);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (423, 6, 555);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (424, 6, 556);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (425, 6, 557);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (426, 6, 558);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (427, 6, 559);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (428, 6, 560);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (429, 6, 561);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (431, 6, -666666);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (432, 6, 31);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (434, 15, 2);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (435, 15, 15);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (436, 15, 16);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (437, 15, 17);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (438, 15, 18);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (439, 15, 292);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (440, 15, 3);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (441, 15, 19);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (442, 15, 20);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (443, 15, 21);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (444, 15, 22);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (445, 15, 77);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (446, 15, 78);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (447, 15, 79);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (448, 15, 80);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (449, 15, 81);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (450, 15, 87);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (451, 15, 88);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (452, 15, 89);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (453, 15, 90);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (454, 15, 91);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (455, 15, 31);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (456, 15, 42);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (457, 15, 43);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (458, 15, 44);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (459, 15, 45);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (460, 15, 46);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (461, 15, 47);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (462, 15, 48);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (463, 15, 49);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (464, 15, 50);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (465, 15, 51);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (466, 15, 72);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (467, 15, 73);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (468, 15, 74);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (469, 15, 75);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (470, 15, 76);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (471, 15, 82);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (472, 15, 83);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (473, 15, 84);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (474, 15, 85);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (475, 15, 86);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (476, 15, 307);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (477, 15, 401);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (478, 15, 402);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (479, 15, 403);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (480, 15, 404);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (481, 15, 405);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (482, 15, 416);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (483, 15, 417);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (484, 15, 418);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (485, 15, 419);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (486, 15, 420);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (487, 15, 421);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (488, 15, 422);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (489, 15, 423);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (490, 15, 424);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (491, 15, 425);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (492, 15, 426);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (493, 15, 427);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (494, 15, 428);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (495, 15, 429);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (496, 15, 430);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (497, 15, 431);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (498, 15, 432);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (499, 15, 433);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (500, 15, 434);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (501, 15, 435);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (502, 15, 436);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (503, 15, 437);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (504, 15, 438);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (505, 15, 439);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (506, 15, 440);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (507, 15, 446);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (508, 15, 447);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (509, 15, 448);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (510, 15, 449);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (511, 15, 450);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (512, 15, 451);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (513, 15, 452);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (514, 15, 453);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (515, 15, 454);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (516, 15, 455);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (517, 15, 461);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (518, 15, 462);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (519, 15, 463);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (520, 15, 464);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (521, 15, 465);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (522, 15, 471);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (523, 15, 472);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (524, 15, 473);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (525, 15, 474);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (526, 15, 475);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (527, 15, 481);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (528, 15, 482);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (529, 15, 483);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (530, 15, 484);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (531, 15, 485);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (532, 15, 496);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (533, 15, 497);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (534, 15, 498);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (535, 15, 499);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (536, 15, 500);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (537, 15, 501);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (538, 15, 502);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (539, 15, 503);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (540, 15, 504);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (541, 15, 505);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (542, 15, 486);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (543, 15, 487);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (544, 15, 488);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (545, 15, 489);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (546, 15, 490);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (547, 15, 506);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (548, 15, 508);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (549, 15, 509);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (550, 15, 510);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (551, 15, 511);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (552, 15, 512);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (553, 15, 513);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (554, 15, 514);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (555, 15, 515);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (556, 15, 516);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (557, 15, 517);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (558, 15, 518);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (559, 15, 519);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (560, 15, 520);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (561, 15, 521);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (562, 15, 522);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (563, 15, 523);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (564, 15, 524);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (565, 15, 525);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (566, 15, 526);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (567, 15, 527);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (568, 15, 528);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (569, 15, 529);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (570, 15, 530);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (571, 15, 531);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (572, 15, 532);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (573, 15, 533);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (574, 15, 534);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (575, 15, 535);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (576, 15, 536);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (577, 15, 537);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (578, 15, 538);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (579, 15, 539);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (580, 15, 540);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (581, 15, 541);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (582, 15, 542);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (583, 15, 543);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (584, 15, 544);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (585, 15, 545);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (586, 15, 546);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (587, 15, 547);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (588, 15, 548);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (589, 15, 549);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (590, 15, 550);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (591, 15, 551);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (592, 15, 552);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (593, 15, 553);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (594, 15, 554);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (595, 15, 555);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (596, 15, 556);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (597, 15, 557);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (598, 15, 558);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (599, 15, 559);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (600, 15, 560);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (601, 15, 561);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (602, 15, 562);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (603, 15, -666666);
INSERT INTO "public"."sys_role_menu"("id", "role_id", "menu_id") VALUES (604, 15, 1);
select setval('sys_role_menu_id_seq',605);


INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (22, 40, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (23, 41, 6);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (26, 45, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (27, 46, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (28, 47, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (29, 48, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (30, 49, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (31, 50, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (32, 51, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (33, 52, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (34, 53, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (35, 54, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (36, 55, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (37, 56, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (38, 57, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (39, 58, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (40, 59, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (41, 60, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (42, 61, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (43, 62, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (44, 63, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (45, 64, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (46, 65, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (47, 66, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (48, 67, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (49, 68, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (50, 69, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (51, 70, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (52, 71, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (53, 72, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (54, 73, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (55, 74, 15);
INSERT INTO "public"."sys_user_role"("id", "user_id", "role_id") VALUES (56, 75, 15);
select setval('sys_user_role_id_seq',57);

INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (457, 1, 'Bossa-AD', NULL, 4, 'CE44', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:42:00.887046', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (458, 1, 'Samba-AD', NULL, 4, 'CE45', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (459, 1, 'Hilo-AD', NULL, 4, 'CE46', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (460, 2, 'Bossanova-EM', NULL, 4, 'CD00', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (461, 2, 'Hilo-ST', NULL, 4, 'CE30', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (462, 2, 'Cossack ', NULL, 4, 'CF06', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (463, 2, 'Guanyu', NULL, 4, 'CF79', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (464, 2, 'Markley', NULL, 4, 'CG44', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (465, 2, 'Markley-R-64-6', NULL, 4, 'CH99', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (466, 2, 'Markley-R-64-4', NULL, 4, 'CJ00', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:46:32.643453', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (467, 2, 'Samba2-L', NULL, 4, 'CH23', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (468, 2, 'Hilo2-L', NULL, 4, 'CH24', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (469, 3, 'Aquavit', NULL, 4, 'CA68', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (470, 3, 'Lafin 24', NULL, 4, 'CE39', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (471, 3, 'Lafin 44', NULL, 4, 'CE40', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (472, 3, 'Landell 24', NULL, 4, 'CE41', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (473, 3, 'Landell 44', NULL, 4, 'CE42', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (474, 3, 'Lafin2 24', NULL, 4, 'CH12', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (475, 3, 'Lafin2 44', NULL, 4, 'CH13', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (476, 4, 'Gaga-FP', NULL, 4, 'CE47', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (477, 4, 'Gaga-FQ', NULL, 4, 'CH01', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (478, 4, 'Gaga-Gram', NULL, 4, 'CJ03', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (479, 5, 'ANTA+ 4色', NULL, 4, 'CA00', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (480, 5, 'ANTA+ 8色', NULL, 4, 'CA00', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (481, 5, 'Clipper', NULL, 4, 'CD27', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (482, 5, 'Clipper-B', NULL, 4, 'CD62', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (483, 5, 'Clipper 2', NULL, 4, 'CH75', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (484, 5, 'Orleans', NULL, 4, 'CE22', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (485, 5, 'Molly 2', NULL, 4, 'CF66', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:47:39.406806', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (486, 5, 'Calypso', NULL, 4, 'CC62', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (487, 5, 'Largo', NULL, 4, 'CF82', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (488, 5, 'Kayak', NULL, 4, 'CB09', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (489, 5, 'Kayak-P', NULL, 4, 'CB81', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (490, 5, 'Kayak-B', NULL, 4, 'CC13', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (491, 6, 'Fusion 44', NULL, 4, 'CE17', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (492, 6, 'Fusion 64', NULL, 4, 'CE20', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (493, 6, 'Liubei 44', NULL, 4, 'CG67', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (494, 7, 'Raptor', NULL, 4, 'CF41', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (495, 8, 'Varius', NULL, 4, 'CF84', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (496, 9, 'Mosso', NULL, 4, 'CH74', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (497, 10, 'Opera-EM', NULL, 4, 'CD02', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (498, 10, 'Opera-MR 36', NULL, 4, 'CD40', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (499, 10, 'Opera-MR 44', NULL, 4, 'CD41', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (500, 10, 'Opera-AD 24', NULL, 4, 'CD66', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (501, 10, 'Opera-AD 36', NULL, 4, 'CD67', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (502, 10, 'Opera-AD 44', NULL, 4, 'CD68', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (503, 10, 'Pacato 44', NULL, 4, 'CF07', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (504, 10, 'Pacato-R', NULL, 4, 'CH66', NULL, NULL, NULL, NULL, '大P', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (505, 11, 'C4', NULL, 4, 'N02Y', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (506, 11, 'C4L', NULL, 4, 'N02Y', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (507, 11, 'LS3', NULL, 4, 'N001', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (508, 11, 'LS3-B', NULL, 4, 'N03L', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (509, 11, 'LS6', NULL, 4, 'N002', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (510, 11, 'LS6-B', NULL, 4, 'N03M', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (511, 11, 'LS10', NULL, 4, 'N03R', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (512, 11, 'LS20', NULL, 4, 'N006', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (513, 11, 'LS20-B', NULL, 4, 'N03Z', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:49:21.289791', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (628, 11, 'T3', NULL, 4, 'N03C', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (629, 11, 'T6', NULL, 4, 'N03P', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (630, 11, 'VT6', NULL, 4, 'N03T', NULL, NULL, NULL, NULL, 'RS', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (631, 12, 'LQ-520K', NULL, 4, 'CC25', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (632, 12, 'PLQ-20', NULL, 4, 'C560', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (633, 12, 'PLQ-20K', NULL, 4, 'C560', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (634, 12, 'LQ-90KP', NULL, 4, 'C560', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (635, 12, 'LQ-82KF', NULL, 4, 'CA39', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (636, 12, 'LQ-730K', NULL, 4, 'CA13', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (637, 12, 'LQ-690K', NULL, 4, 'CA13', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (638, 12, 'LQ-106KF', NULL, 4, 'CA13', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (639, 12, 'LQ-136KWII', NULL, 4, 'CF40', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (640, 12, 'LQ-2680K', NULL, 4, 'C684', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (641, 12, 'LQ-300+II', NULL, 4, 'C638', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (642, 12, 'LQ-300K+II', NULL, 4, 'C638', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (643, 12, 'LQ-790K', NULL, 4, 'CB95', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (644, 13, 'M-T173H', NULL, 4, 'D399', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (645, 13, 'M-T173V', NULL, 4, 'D400', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (646, 13, 'M-T183', NULL, 4, 'D386', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (647, 13, 'M-150', NULL, 4, 'D156', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (648, 13, 'M-160', NULL, 4, 'D157', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (649, 13, 'M-163', NULL, 4, 'D158', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (650, 13, 'M-164', NULL, 4, 'D159', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (651, 13, 'M-260 ', NULL, 4, 'D143', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (652, 13, 'M-262', NULL, 4, 'D144', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (653, 13, 'M-260A', NULL, 4, 'D152', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (654, 13, 'M-262A ', NULL, 4, 'D153', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (655, 13, 'M-265 ', NULL, 4, 'D146', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (656, 13, 'M-267', NULL, 4, 'D151', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (657, 13, 'M-290', NULL, 4, 'D128', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (658, 13, 'TU-260', NULL, 4, 'D322', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (659, 13, 'M-180', NULL, 4, 'D161', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (660, 13, 'M-183 ', NULL, 4, 'D164', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (661, 13, 'M-186', NULL, 4, 'D166', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (662, 13, 'M-190 ', NULL, 4, 'D015', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (663, 13, 'M-195 ', NULL, 4, 'D022', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (664, 13, 'M-191', NULL, 4, 'D024', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:50:53.175831', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (685, 13, 'M-192 ', NULL, 4, 'D025', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (686, 13, 'M-190G ', NULL, 4, 'D081', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (687, 13, 'M-192G', NULL, 4, 'D082', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (688, 13, 'M-T532AF', NULL, 4, 'D094', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (689, 13, 'M-T531AP ', NULL, 4, 'D095', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (690, 13, 'M-T532AP ', NULL, 4, 'D096', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (691, 13, 'M-T522AF ', NULL, 4, 'D100', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:51:46.650157', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (705, 13, 'M-T521 ', NULL, 4, 'D323', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:27.150734', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (706, 13, 'M-T523AP', NULL, 4, 'D351', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:27.150734', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (707, 13, 'M-T482AF', NULL, 4, 'D353', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:27.150734', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (708, 13, 'M-T532AF ', NULL, 4, 'D094', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:27.150734', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (709, 13, 'M-T542HF', NULL, 4, 'D111', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:45.714601', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (710, 13, 'M-T531IIAP', NULL, 4, 'D431', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:45.714601', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (711, 13, 'M-T532IIAF ', NULL, 4, 'D401', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:45.714601', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (712, 13, 'M-T532IIAP ', NULL, 4, 'D402', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:52:45.714601', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (718, 13, 'M-T533IIAP ', NULL, 4, 'D404', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:53:21.364599', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (719, 13, 'M-T533IIAF', NULL, 4, 'D403', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:53:21.364599', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (720, 13, 'M-T542IIHF ', NULL, 4, 'D413', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:53:21.364599', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (727, 13, 'M-T542IIAF', NULL, 4, 'D405', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:54:22.555293', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (728, 13, 'M-T542IIHF', NULL, 4, 'D413', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:04.636427', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (729, 13, 'M-T592IIHF', NULL, 4, 'D414', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (730, 13, 'M-T512IIAF', NULL, 4, 'D415', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (731, 13, 'M-T512IIAP', NULL, 4, 'D416', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (732, 13, 'M-T522IIAF', NULL, 4, 'D419', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (733, 13, 'M-T522IIAP', NULL, 4, 'D420', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (734, 14, 'TM-U295', NULL, 4, 'C163', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (735, 14, 'TM-U295P', NULL, 4, 'C178', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (736, 14, 'TM-T70-i', NULL, 4, 'C637', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (737, 14, 'TM-T70II', NULL, 4, 'CD38', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (738, 14, 'TM-T70II-DT', NULL, 4, 'CD51', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (739, 14, 'TM-T70II DT2', NULL, 4, 'CH61', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (740, 14, 'TM-T20', NULL, 4, 'CB10', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (741, 14, 'TM-T810F', NULL, 4, 'CB75', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (742, 14, 'TM-T900F', NULL, 4, 'CB76', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (743, 14, 'TM-T20II', NULL, 4, 'CD52', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (744, 14, 'TM-M10', NULL, 4, 'CE74', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (745, 14, 'TM-M30', NULL, 4, 'CE95', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (746, 14, 'TM-P20', NULL, 4, 'CE14', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (747, 14, 'TM-P60', NULL, 4, 'CC79', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (748, 14, 'TM-P80', NULL, 4, 'CD70', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (749, 14, 'TM-H5000', NULL, 4, 'C246', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (750, 14, 'TM-H5000IIP', NULL, 4, 'C249', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (751, 14, 'TM-U590', NULL, 4, 'C196', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (752, 14, 'TM-U590P', NULL, 4, 'C222', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (753, 14, 'TM-U950', NULL, 4, 'C151', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (754, 14, 'TM-U950P', NULL, 4, 'C176', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (755, 14, 'TM-T86L', NULL, 4, 'C532', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (756, 14, 'TM-T88IV', NULL, 4, 'C636', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (757, 14, 'TM-T88V', NULL, 4, 'CA85', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (758, 14, 'TM-T88V-DT', NULL, 4, 'CC74', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (759, 14, 'TM-T88VI-DT2', NULL, 4, 'CH64', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (760, 14, 'TM-T88VI', NULL, 4, 'CE94', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (761, 14, 'TM-U220A', NULL, 4, 'C513', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (762, 14, 'TM-U220B', NULL, 4, 'C514', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (763, 14, 'TM-U220D', NULL, 4, 'C515', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (764, 14, 'TM-U220PA', NULL, 4, 'C516', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (765, 14, 'TM-U220PB', NULL, 4, 'C517', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (766, 14, 'TM-U220PD', NULL, 4, 'C518', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (767, 14, 'TM-U288B', NULL, 4, 'CB65', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (768, 14, 'TM-U288D', NULL, 4, 'CB66', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (769, 14, 'TM-U330B', NULL, 4, 'CD85', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (770, 14, 'TM-H6000III', NULL, 4, 'C625', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (771, 14, 'TM-U675', NULL, 4, 'C283', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (772, 14, 'TM-U675P', NULL, 4, 'C289', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (773, 14, 'TM-H2000', NULL, 4, 'CB26', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (774, 14, 'TM-H6000IV-DT', NULL, 4, 'CD83', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (775, 14, 'TM-H6000IV', NULL, 4, 'CB25', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (776, 14, 'TM-H6000V', NULL, 4, 'CG62', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (777, 15, 'CTM-RX700', NULL, 4, 'C596', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (778, 15, 'GP-730', NULL, 4, 'CD75', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (779, 15, 'GP-C830', NULL, 4, 'CC68', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (780, 15, 'GP-M830', NULL, 4, 'CC69', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (781, 15, 'GP-1100', NULL, 4, 'CD26', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (782, 15, 'TM-C3400', NULL, 4, 'CA26', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (783, 15, 'TM-C3400-LT', NULL, 4, 'CC35', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (784, 15, 'TM-C3400BK', NULL, 4, 'CA26', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (785, 15, 'TM-C3500', NULL, 4, 'CD54', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (786, 15, 'TM-S1000', NULL, 4, 'A266', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (787, 15, 'TM-C7500', NULL, 4, 'CD84', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (788, 15, 'TM-L500', NULL, 4, 'CB49', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (789, 15, 'TM-S2000', NULL, 4, 'A268', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (790, 15, 'TMS2000II', NULL, 4, 'CG60', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (791, 15, 'TMS9000', NULL, 4, 'A267', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (792, 15, 'TMS9000II', NULL, 4, 'CG59', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (793, 15, 'TM-C700', NULL, 4, 'CA91', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (794, 15, 'TM-C800', NULL, 4, 'CF90', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (795, 15, 'PP-100N', NULL, 4, 'CA31', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (796, 15, 'PP-100AP', NULL, 4, 'CA93', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (797, 15, 'PP-50', NULL, 4, 'CB72', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (798, 15, 'PP-100II', NULL, 4, 'CD37', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (799, 15, 'PP-100III', NULL, 4, 'CH40', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (800, 15, 'PP-50II', NULL, 4, 'CH41', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (801, 15, 'TM-L90', NULL, 4, 'C412', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (802, 15, 'TM-L90P', NULL, 4, 'C414', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (803, 15, 'TM-T90', NULL, 4, 'C390', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (804, 15, 'TM-T90II', NULL, 4, 'CD39', NULL, NULL, NULL, NULL, '小P', 1, '2019-12-15 23:55:52.776443', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (813, 16, '机种1', NULL, 5, 'H919,H921', NULL, '2019-07-05', NULL, '2019-11-29', '', 1, '2019-12-16 00:01:26.067239', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (814, 16, '机种2', NULL, 5, 'H922,H956', NULL, '2019-07-05', NULL, '2019-11-29', '', 1, '2019-12-16 00:01:39.770459', NULL, NULL, NULL);
INSERT INTO "public"."model"("id", "model_series_id", "name", "pinyin", "dept_id", "code", "ws_time", "es_time", "amp_time", "mp_time", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (815, 17, 'DUCT PUMP,UNIT,4,CH74', NULL, 6, '1816975-**', NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-16 00:06:59.024882', NULL, NULL, NULL);
select setval('model_id_seq',816);

INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 'BSH-AD', 'BSH-AD', NULL, 1, '2019-12-16 00:37:50.423605', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, 'BSH', 'BSH', NULL, 1, '2019-12-16 00:37:54.945717', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'LL', 'LL', NULL, 1, '2019-12-16 00:37:59.577792', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'Gaga', 'Gaga', NULL, 1, '2019-12-16 00:38:08.11449', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 'LFP', 'LFP', NULL, 1, '2019-12-16 00:38:12.437534', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 'Fusion', 'Fusion', NULL, 1, '2019-12-16 00:38:16.297647', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 'Raptor', 'Raptor', NULL, 1, '2019-12-16 00:38:21.031962', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 'Varius', 'Varius', NULL, 1, '2019-12-16 00:38:26.028559', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 'Mosso', 'Mosso', NULL, 1, '2019-12-16 00:38:30.002403', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 'Opera', 'Opera', NULL, 1, '2019-12-16 00:38:34.094783', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, 'RS', 'RS', NULL, 1, '2019-12-16 00:38:38.099589', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, 'SIDM', 'SIDM', NULL, 1, '2019-12-16 00:38:42.235437', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, 'MPM', 'MPM', NULL, 1, '2019-12-16 00:38:47.079773', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (14, 'TM-R', 'TM-R', NULL, 1, '2019-12-16 00:38:51.240997', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (15, 'TM-NR', 'TM-NR', NULL, 1, '2019-12-16 00:38:54.903655', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (16, 'Latvia', 'Latvia', NULL, 1, '2019-12-16 00:38:59.051497', NULL, NULL, NULL);
INSERT INTO "public"."model_series"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (17, 'MOSSO', 'MOSSO', NULL, 1, '2019-12-16 00:39:03.077822', NULL, NULL, NULL);
select setval('model_series_id_seq',18);

INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 'S1', NULL, '以Varius76-4 ES1阶段数据为参考', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, 'S2', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'S3', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'S4', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 'S5', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 'Roll&Reel', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 'M0', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 'M1', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 'M2', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 'M3', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, 'M4', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, '内观', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, '外装1', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (14, '漏电', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (15, '漏气', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (16, '印字', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (17, '洗净', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (18, '外装2', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (19, '安检&出荷', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (20, '外观', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (21, '本体捆包', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (22, 'Option 捆包', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (23, '反射镜A,B组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (24, 'N偏光板R+G+B', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (25, 'LDP 组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (26, '上光通道组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (27, '光通道组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (28, '下壳组合（分方向）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (29, 'LDDR 组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (30, 'LD消音器组合 ', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (31, '排気通道组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (32, '吸気通道组合 ', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (33, 'LV 通道组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (34, 'LLC 电源组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (35, '電源组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (36, 'LD通道组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (37, 'EX1 & DR 基板组合 （H922/H956专用)', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (38, '扬声器支架组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (39, 'IRR 支架组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (40, '前壳组合（分方向)', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (41, '上壳组合 （H919/H921）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (42, '上壳组合 （H922/H956）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (43, '空气过滤网组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (44, '前盖组合 ', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (45, '笔盒组合（H919专用）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (46, '光引擎组合', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, '光轴，对比度调整（分方向)', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, '光轴调整（H922/H956)', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, '本体组立1（分方向）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, '本体组立2（分方向)', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, '送料（B19拉物流人员，B18拉直接人员）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (52, '安全测试&电力测试&耐久工程', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (53, '画质调整（3台）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (54, 'ITR调整：2台（H919,H921专用）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (55, 'ITR检查 & WFD检查（H919,H921专用）', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (56, '光学测试', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (57, '最终外观，标签貼付', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (58, '附属品十套管理清点', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (59, '制品捆包', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (60, '制品堆码+出机单+扫描', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (61, 'Label印刷', NULL, '', 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
INSERT INTO "public"."workstation"("id", "name", "pinyin", "remark", "workstation_type_id", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (62, 'Mosso/DUCT PUMP UNIT 4 CH74', NULL, NULL, 0, 1, '2019-12-16 00:55:23.447992', NULL, NULL, NULL);
select setval('workstation_id_seq',62);

INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 'E-ring,4,f/uc-3c', '', 't', '以Varius76-4 ES1阶段S1工位数据为参考', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.047765', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, 'E-ring,2.3,f/uc-3c', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.050327', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'C.b.s-tite(p2)screw,3x8,f/zb-3c', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.052023', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'Edge saddle,wes-1210', '', 't', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.054713', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 'Shaft,holder ink eject tunk', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.056394', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 'Guide,tube,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.058079', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 'Frame,ink eject,drive', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.06675', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 'Shaft,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.07253', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 'Plate spring,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.074082', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 'Frame,base,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.07586', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, 'Frame,sensor,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.079223', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, 'Mounting plate,shaft,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.080988', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, 'Frame,ink eject,driven,sub', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.082788', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (14, 'Mounting plate,ink eject,left', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.084458', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (15, 'Frame,ink eject,upper', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.086394', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (16, 'C.b.s-tite(sp2) screw,4x10 f/zb-3c', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.08942', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (17, 'Leaf sensor,p599', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.091009', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (18, 'Harness,ink eject', '', 'f', '', 1, '2019-12-16 01:02:07.24992', NULL, '2019-12-19 17:48:35.093232', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (19, 'Ink eject assy  esl', '', 'f', '', 1, '2019-12-16 01:02:57.307167', NULL, '2019-12-19 17:48:35.09454', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (20, 'C.c.s-tite screw,4x8,f/zn-3c', '', 't', '', 1, '2019-12-16 01:03:39.904484', NULL, '2019-12-19 17:48:35.095731', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (21, 'One touch bushing,nb-15', '', 'f', '', 1, '2019-12-16 01:03:39.904484', NULL, '2019-12-19 17:48:35.096846', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (22, 'Frame,cover,side,right', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.09784', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (23, 'Locking wire saddle lws-2218z', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.098922', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (24, 'Ffc clamp,fc-45', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.100179', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (25, 'Frame side cover right assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.102138', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (26, 'Mounting plate,breaker', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.104966', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (27, 'Mounting plate,breaker;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.107487', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (28, 'C.p.screw,4x40,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.108837', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (29, 'Leak breaker', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.110128', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (30, 'Frame breaker assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.111372', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (31, 'Square bush,sb-2718', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.112573', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (32, 'Square bush', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.113656', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (33, 'Frame,cover,side,left', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.114965', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (34, 'Frame side cover left assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.116616', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (35, 'H.s.c.bolt.(s)screw,6x12,f/zb-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.117833', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (36, 'Shaft,fasten,afterheater', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.119797', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (37, 'Frame,fasten,heater,sub', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.121819', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (38, 'Frame fasten heater sub assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.122941', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (39, 'Plate assy.,after,side,right', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.123961', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (40, 'H.n.-1,10,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.125171', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (41, 'Shaft,fasten,flame,side', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.126247', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (42, 'Plate afterheater side assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.127502', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (43, 'Square nut 5.5,3x2.3', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.128586', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (44, 'C.p.(s-p2)screw,3x8,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.129883', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (45, 'Cut washer,4.1x0.5x6.5', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.131067', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (46, 'Holder,bearing,centor support', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.132155', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, 'Mounting plate,centor support', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.133163', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, 'Mounting plate,centor support;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.134249', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, 'Mounting plate,centor support;c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.135757', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, 'Mounting plate,centor support;d', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.137825', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, 'Shaft,adjust,centor support', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.13929', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (52, 'Bushing,cam,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.140555', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (53, 'Mounting plate,holder,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.141703', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (54, 'Holder,bearing,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.143007', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (55, 'Shaft,bearing,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.144169', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (56, 'Spacer,holder,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.145448', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (57, 'Ball bearing,shaft,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.146591', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (58, 'Polyslider,ll-5208-10', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.147642', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (59, 'Frame adjust pf assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.148814', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (60, 'Struct,right', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.149997', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (61, 'Struct right assy', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.151343', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (62, 'H.s.c.bolt,4x16,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.153018', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (63, 'Struct,left', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.154602', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (64, 'Shaft,struct', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.15569', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (65, 'Struct left assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.156715', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (66, 'Plate,motor,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.158159', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (67, 'Fan,cooling,06025ss-24q-al-de', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.16051', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (68, 'Plate pf motor assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.161759', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (69, 'C.c.screw,3x30,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.162917', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (70, 'C.c.screw,3x8,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.164142', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (71, 'C.c.screw,4x8,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.165575', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (72, 'Cover,motor,cutter', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.166822', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (73, 'Edge saddle,wes-1117', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.167954', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (74, 'Edge saddle,wes-0507', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.169161', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (75, 'Spacer,motor,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.171672', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (76, 'Shaft assy.,drive,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.172996', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (77, 'Holder,motor,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.174163', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (78, 'Holder,drive', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.175305', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (79, 'Holder,drive;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.1763', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (80, 'Frame assy.,drive', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.177359', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (81, 'Mounting plate,frame,drive', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.178639', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (82, 'Adjusting plate,motor,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.179757', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (83, 'Compression spring,0.58', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.181028', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (84, 'Combination gear,36,27,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.182026', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (85, 'Belt,motor,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.183049', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (86, 'C.b.screw,4x10,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.184077', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (87, 'Motor assy.,rewind', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.185756', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (88, 'Harness,motor,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.187919', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (89, 'Motor assy,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.188923', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (90, 'Motor cr driven assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.189877', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (91, 'C.b.s-tite screw,4x10,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.191001', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (92, 'Shaft assy.,driven,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.193029', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (93, 'Holder assy,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.19426', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (94, 'Fixing plate,bearing,driven', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.195188', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (95, 'Frame,driven;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.196135', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (96, 'Frame,driven;c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.197085', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (97, 'Mounting plate,frame,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.198154', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (98, 'Frame assy,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.199483', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (99, 'C.c.screw,4x25,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.20072', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (100, 'H.s.c.bolt(p4),6x40,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.201945', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (101, 'Pulley driven assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.204439', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (102, 'E-ring,3,f/uc-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.205637', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (103, 'C.b.p-tite screw,3x10,f/zb-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.20686', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (104, 'C.b.p-tite screw,2x8,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.208139', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (105, 'C.b.screw,4x5,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.209372', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (106, 'C.f.screw,3x6,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.210508', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (107, 'H.s.c.bolt,3x6,f/ni', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.211625', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (108, 'Board assy.,encoder,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.213032', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (109, 'H.s.c.bolt,8x12,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.214296', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (110, 'Mounting plate,bearing,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.215382', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (111, 'Flag,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.216544', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (112, 'Cam,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.217793', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (113, 'Stopper,bearing,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.2193', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (114, 'Mounting plate,sensor,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.22183', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (115, 'Flag,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.222896', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (116, 'Holder,encoder,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.224009', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (117, 'Guide,cam,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.225359', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (118, 'Mounting plate,shaft,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.226404', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (119, 'Mounting plate,p.c.b.,cr;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.227684', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (120, 'Stopper,cr,main', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.22901', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (121, 'Mounting plate,filter,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.230342', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (122, 'Carriage assy.,main', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.231463', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (123, 'Spur gear,12,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.23254', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (124, 'Shaft,cam,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.233732', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (125, 'Shaft,bearing,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.234933', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (126, 'Cam,cr', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.236866', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (127, 'Spacer assy.,guide,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.238303', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (128, 'Lm guide,2mmg7wmuus+118ls', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.239613', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (129, 'Brass spacer,bsb-310e', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.24106', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (130, 'C-ring(s),17,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.24258', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (131, 'Parallel pins,1615101-20100', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.243881', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (132, 'Round nut', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.244988', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (133, 'S,e,l,h,m,screw,w,cross', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.246024', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (134, 'P.w.,3.8,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.247486', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (135, 'Ball bearing', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.24887', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (136, 'Ball bearing f684zz ns7l', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.250077', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (137, 'Ball bearing 6903jrxzz 5k', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.251563', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (138, 'C.b.screw(p2),4x8,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.25351', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (139, 'Photo interrupter', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.255736', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (140, 'Ffc,encoder,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.257181', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (141, 'Harness,detect,apg', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.258688', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (142, 'Main cr assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.259886', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (143, 'C.b.p-tite screw,2x4,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.261019', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (144, 'Harness clamp:lws-0511z/s', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.262202', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (145, 'One touch bush,nb-11', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.263465', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (146, 'Mounting plate,holder head', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.264627', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (147, 'Extension spring,47.59', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.265909', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (148, 'Extension spring,31.44', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.267149', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (149, 'Holder,pw', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.269354', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (150, 'Mounting plate,shutter,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.271982', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (151, 'Strengthen plate,cr,sub', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.273271', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (152, 'Groundingplate,head', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.274387', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (153, 'Carriage assy.,sub', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.275449', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (154, 'Holder head assy.,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.276484', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (155, 'Dummy,head,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.277503', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (156, 'Plate,cover,cr,home', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.278678', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (157, 'Plate,cover,cr,full', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.280183', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (158, 'Shutter,camera,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.281481', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (159, 'Clump,pw,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.282698', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (160, 'Compression spring,9.58', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.28378', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (161, 'Polyslider,ll-6209-05', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.285131', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (162, 'One touch bushing,nb-29', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.287538', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (163, 'Label,shutter,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.288933', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (164, 'Double-sided adhesive tape,cpadsensor,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.290259', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (165, 'C.b.(s-p2)screw,4x10,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.291419', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (166, 'Board assy.,detector,pw', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.292529', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (167, 'Ffc,pw,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.293761', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (168, 'Ffc,cpad,sensor,home', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.294842', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (169, 'Ffc,cpad,sensor,full', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.296148', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (170, 'Harness,grounding,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.297213', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (171, 'P.c.b.,relay,cpadsensor', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.298219', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (172, 'Piezo film sensor,fmlussc008', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.299385', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (173, 'Sub cr assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.300921', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (174, 'C.c.screw,3x6,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.302956', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (175, 'Shaft,roller,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.304797', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (176, 'Shaft,adjust plate,spring', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.305965', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (177, 'Adjust plate,spring', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.307186', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (178, 'Fasten plate,roller,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.308277', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (179, 'Cut washer,2.5x0.25x5.0', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.309393', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (180, 'Holder,paper guide,upper', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.310406', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (181, 'Roller,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.311613', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (182, 'Holder,pf,driven', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.313888', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (183, 'Plate,adjust,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.315115', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (184, 'Pf driven assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.316313', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (185, 'Bushing,shaft,paper guide,upper,b', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.317368', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (186, 'Shaft,driven,release,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.318694', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (187, 'Cam,release,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.320771', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (188, 'Cam release assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.32209', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (189, 'Cam,adjust,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.32313', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (190, 'Shaft,driven,adjust,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.324213', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (191, 'Cam adjust assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.325308', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (192, 'C.b.screw,3x6,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.326572', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (193, 'C.b.screw,3x8,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.327882', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (194, 'C.c.s-tite screw,3x8,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.329194', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (195, 'Wire saddle,lws-1316z', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.330527', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (196, 'Holder,boardassy.,ie', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.331704', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (197, 'Holder,filter,cr', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.332866', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (198, 'Flame assy.,leak', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.334256', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (199, 'Holder,filter', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.337133', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (200, 'Clamp,tube', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.339305', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (201, 'Clamp,tube;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.340708', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (202, 'Clamp,tube,c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.341842', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (203, 'Guide plate,tube', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.343923', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (204, 'Holder,guide plate,tube', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.345105', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (205, 'Holder,tube', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.346157', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (206, 'Holder,fix plate,tube', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.347128', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (207, 'Grounding plate,holder,tube;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.348225', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (208, 'Sheet,guide plate,tube', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.349575', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (209, 'Supply tube assy.,esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.350628', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (210, 'Porous pad,filter', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.351931', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (211, 'Board assy.,sns', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.353946', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (212, 'Harness,leak sensor,cr;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.35495', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (213, 'Harness, sub-c, ffc40', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.355867', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (214, 'Harness, power, sub-h', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.357267', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (215, 'Mm duplex sc-sc armored patch cord', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.358489', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (216, 'Tube assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.359572', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (217, 'C.b.s-tite screw,3x6,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.360561', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (218, 'Mounting plate,p.c.b.,led', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.361555', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (219, 'Frame,support,upper,left', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.362658', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (220, 'Board assy.,sub,ce46 sub-r', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.36377', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (221, 'Frame support left top assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.364684', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (222, 'C.c.s-tite r.screw,3x6,f/zn-3c', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.365798', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (223, 'Cover,p.c.b.,led', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.367137', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (224, 'Frame,cover,upper', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.368702', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (225, 'Board assy.,sub,ce46 sub-c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.371273', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (226, 'Harness, relay, sub-r 2', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.372436', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (227, 'Harness, relay, sub-r', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.373396', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (228, 'Frame cover top assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.374321', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (229, 'Base,paper guide,lower', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.37534', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (230, 'H.s.c.bolt,5x40,f/zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.376467', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (231, 'Cover,base,paper guide,rear', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.377474', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (232, 'Frame,duct,fan', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.378425', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (233, 'Plate,paper guide,adjust', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.379333', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (234, 'Spacer,paper guide,adjust', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.380405', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (235, 'Seal,paper guide', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.381539', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (236, 'Seal,paper guide;b', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.382861', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (237, 'Plate,paper guide,positioning', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.383935', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (238, 'Coil spring,swm12x30', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.385242', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (239, 'Wing.bolt.-2,4x40,f,zn-3c', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.387558', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (240, 'Fan assy.,absorption', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.388805', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (241, 'Frame base paperguide front assy esl', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.389813', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (242, 'Coupling,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.390921', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (243, 'Key,pf', '', 'f', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.392004', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (244, '防拆标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.393045', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (245, '号码牌', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.394296', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (246, '镜头保护盖', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.395563', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (247, '服务标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.396675', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (248, '镜头标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.397682', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (249, '激光注意标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.398631', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (250, '激光开口标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.39976', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (251, '电线盖', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.400951', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (252, '3LCD', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.402537', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (253, '电源线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.404553', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (254, '电脑线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.405462', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (255, '电池', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.406373', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (256, '遥控器', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.407488', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (257, '说明书', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.4087', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (258, '包机袋子', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.409853', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (259, '附属品胶袋子', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.41106', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (260, '保护环', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.412255', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (261, '天吊落下注意卡', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.413417', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (262, '油烟警告标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.414345', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (263, '商品号标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.415321', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (264, 'MRP标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.416222', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (265, '系列号标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.417277', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (266, 'S/N标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.418715', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (267, '节能标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.421344', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (268, '落下警告标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.422519', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (269, '3LCD标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.423537', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (270, '警告标签A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.424431', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (271, '警告标签B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.425364', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (272, '警告标签C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.426663', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (273, '警告标签D', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.427864', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (274, '警告标签E', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.429033', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (275, '大定格标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.430074', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (276, '小定格标签', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.431166', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (277, '后撑脚', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.432323', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (278, '下壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.43352', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (279, '前撑脚', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.434529', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (280, '撑脚垫', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.435931', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (281, '大垫圈', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.438269', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (282, '小垫圈', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.439618', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (283, '吸气海绵A（下）', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.440771', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (284, '吸气海绵B（上）', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.441717', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (285, '吸气海绵C（右）', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.442663', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (286, '吸气海绵D（中）', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.443737', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (287, '吸气海绵E（左）', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.444902', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (288, '螺母', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.446097', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (289, '下壳前屏蔽板;A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.447188', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (290, '下壳前屏蔽板;B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.448224', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (291, '下壳后屏蔽板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.449155', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (292, '安全杆', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.450118', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (293, '电源插口连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.451145', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (294, '过滤基板支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.452629', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (295, '3*12螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.454695', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (296, '下过滤基板垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.45578', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (297, 'PS胶纸', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.456699', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (298, '4*6花边螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.45768', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (299, '过滤绝缘盖板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.458905', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (300, '过滤器基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.460116', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (301, '3*6螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.461036', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (302, 'AC连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.462115', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (303, '排气风扇垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.463105', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (304, '排气风扇通道', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.464397', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (305, '3*35螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.465669', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (306, '中继连接线 M4P120', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.466555', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (307, 'LDDR垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.46738', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (308, 'LDDR基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.468528', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (309, '3*8螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.470561', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (310, 'LDDR-LLC连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.47201', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (311, 'LDDR-MA连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.472978', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (312, 'LD风扇', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.474157', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (313, 'LD风扇海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.475118', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (314, 'LD风扇通道；上', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.476338', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (315, 'LD风扇通道海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.477399', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (316, 'HS通道海绵A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.478327', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (317, '海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.47932', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (318, 'LV热敏电阻', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.480262', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (319, 'HS通道海绵B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.481532', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (320, 'WRF支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.482697', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (321, 'P3模块', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.483597', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (322, '2*6螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.48463', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (323, 'WFD-WRF FFC连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.486428', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (324, 'WRF垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.487931', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (325, '吸气通道', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.489228', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (326, '吸气通道海绵A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.490349', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (327, '吸气通道海绵B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.491266', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (328, 'TH基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.492297', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (329, '2.5*8螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.493342', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (330, '灯胆风扇', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.494601', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (331, '中继连接线 MOLEX-JST', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.49569', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (332, '3*14螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.496645', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (333, '吸气垫片A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.497525', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (334, '吸气风扇A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.498337', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (335, '吸气风扇B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.499133', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (336, '固定胶纸', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.500078', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (337, '吸气通道海绵F', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.501098', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (338, '吸气通道海绵G', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.502439', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (339, '投写镜盖,B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.505021', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (340, 'PS上壳支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.506031', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (341, 'PS上壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.506914', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (342, 'PS基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.507785', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (343, 'PS-MA连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.508528', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (344, 'PS-LLC连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.509239', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (345, 'PS下壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.510092', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (346, 'PS下壳支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.511024', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (347, '扬声器支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.51196', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (348, '扬声器', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.51275', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (349, '消音海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.513561', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (350, '吸气密封海绵B1', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.514302', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (351, '3*10螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.514991', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (352, '吸气风扇垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.515872', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (353, '消音器支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.51676', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (354, '消音胶纸', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.517558', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (355, 'LD消音海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.518529', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (356, 'HS通道海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.520322', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (357, '排气风扇', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.521315', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (358, '排气海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.522182', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (359, '空气过滤网盖', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.523087', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (360, '空气过滤网盖螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.523926', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (361, '空气过滤网连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.524644', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (362, '上壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.525478', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (363, '上壳海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.526288', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (364, 'LED灯', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.527148', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (365, 'LED支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.527937', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (366, '上壳固定垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.528729', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (367, '吸气窗', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.529614', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (368, '排气窗', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.530484', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (369, 'LV上通道', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.531333', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (370, 'LV下通道', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.53207', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (371, 'LV通道海绵A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.532748', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (372, 'LV通道海绵B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.533606', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (373, 'LV通道海绵C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.534462', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (374, 'LV通道海绵E', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.535845', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (375, 'LV通道海绵F', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.537928', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (376, 'LV通道海绵G', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.538796', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (377, 'LV通道海绵H', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.539668', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (378, '投射镜盖A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.540562', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (379, 'IRF基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.541356', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (380, 'RC连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.542109', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (381, 'IR基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.542802', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (382, '投射镜盖海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.543555', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (383, 'PJ镜头盖海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.54442', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (384, 'LLC上壳支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.545495', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (385, 'LLC上壳海绵A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.546344', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (386, 'LLC上壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.547076', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (387, 'LLC基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.547766', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (388, 'LLC下壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.548639', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (389, 'LLC下壳支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.549481', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (390, 'THE风扇', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.550303', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (391, '吸气风扇海绵B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.55106', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (392, 'LED基板支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.552381', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (393, 'LED基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.554611', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (394, '3*10黑色螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.555574', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (395, 'EMI抑制片F', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.556543', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (396, 'WFD支架A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.557411', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (397, 'WFD支架B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.558146', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (398, 'EMI抑制片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.559003', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (399, '2.5*10螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.559887', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (400, 'IF遮光屏蔽板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.560723', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (401, 'WFD支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.561491', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (402, 'LDP固定垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.562203', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (403, '双面胶纸', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.562903', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (404, 'NP 调整支架R.B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.563605', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (405, 'N偏光板支架G', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.564478', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (406, 'IF标签 A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.565326', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (407, 'IF标签 B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.566161', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (408, 'SW操作面板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.567046', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (409, 'ENTER键', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.568786', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (410, '后壳', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.570607', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (411, '操作面板基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.571531', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (412, 'FFC连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.572296', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (413, '反射镜調整支架;A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.573057', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (414, '反射镜调整支架B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.57402', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (415, 'MA基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.574824', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (416, '锂电池', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.575622', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (417, 'MA绝缘胶纸B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.576317', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (418, 'EMI抑制片垫片F', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.576962', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (419, 'WFD垫片；A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.577597', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (420, 'MA垫片，左', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.578237', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (421, 'MA垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.578863', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (422, '六角螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.579489', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (423, 'IF屏蔽板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.580126', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (424, 'IF基板', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.580772', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (425, '垫片海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.581551', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (426, '连接线固定胶纸', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.582326', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (427, 'WFD-WRF FFC垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.583098', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (428, 'EMI抑制片垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.583883', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (429, 'MA支架垫片A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.58469', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (430, 'MA支架垫片B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.585777', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (431, 'MA框架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.587834', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (432, '光驱动底座', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.588762', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (433, '线夹', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.589697', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (434, '连接线固定垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.590699', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (435, '上光通道', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.591718', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (436, 'CLG固定弹片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.592561', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (437, '2.6*6螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.59343', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (438, 'LV通道海绵D', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.594269', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (439, '1.7*5螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.595119', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (440, '聚焦环B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.595943', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (441, '变焦环B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.596814', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (442, '变焦环A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.597897', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (443, '聚焦环A', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.598709', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (444, '下光通道', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.599521', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (445, '下光通道海绵', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.600343', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (446, '光通道遮光垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.601345', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (447, 'SEN感应器基板　　　　　　　　　　　　', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.60291', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (448, 'RGB感应器连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.604788', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (449, '光通道遮光垫片;B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.605551', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (450, '复合镜固定弹片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.606357', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (451, '固定弹片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.60733', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (452, '光通道垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.608186', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (453, '光通道盖', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.60892', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (454, 'LG 连接弹片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.609637', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (455, 'PS支架', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.61033', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (456, '2.5*6螺丝', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.611007', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (457, 'TH连接线', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.6119', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (458, '投射镜支架B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.612779', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (459, '导热垫片', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.613604', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (460, '空气过滤网', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.614321', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (461, 'M3螺母', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.61505', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (462, 'C.B.P-TITE SCREW,3X8,F/ZN-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.615975', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (463, 'C.C.P-TITE,3X8,F/ZB-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.616933', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (464, 'C.B.SCREW,3X6,F/ZN-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.617902', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (465, 'CLAMP,SB-1909', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.619224', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (466, 'Locking Edge Saddle', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.620656', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (467, 'CAM,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.621599', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (468, 'JOINT,CAM,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.622478', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (469, 'MOUNTING PLATE,HOLDER,NEEDLE,IV', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.623339', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (470, 'TUBE,DP,41', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.624081', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (471, 'TUBE,DP,82', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.624831', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (472, 'COVER,TUBE,DP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.625699', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (473, 'JJOINT,AIR PUMP,IR;B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.626555', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (474, 'TUBE,PDP,135', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.627331', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (475, 'NEEDLE,CHOKE,4.,ASSY', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.628046', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (476, 'C.B.P-TITE SCREW,3X14,F/ZN-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.628725', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (477, 'ROLLER,SLIDER,CA', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.629547', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (478, 'SHAFT,ROLLER,GUIDE,HOLDER,ICH', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.630423', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (479, 'NEEDLE,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.631473', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (480, 'GUIDE,INK', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.632333', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (481, 'POROUS PAD,HOLDER,NEEDLE,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.633598', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (482, 'HOLDER,NEEDLE,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.634381', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (483, 'LEVER,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.635538', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (484, 'INPUT VALVE,IR', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.637364', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (485, 'COMPRESSION SPRING,1.01', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.638243', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (486, 'COMPRESSION SPRING,0.92', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.639013', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (487, 'WELDING DUCT,CHOKE,4.,ASSY', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.639795', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (488, 'COVER,DUCT,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.640678', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (489, 'DUCT,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.641596', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (490, 'TUBE,CHOKE PUMP.,ASSY', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.642396', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (491, 'JOINT,TUBE,L', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.643099', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (492, 'TORSION SPRING,TUBE,FASTEN,D', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.643789', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (493, 'TUBE,CHOKE PUMP,74', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.644538', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (494, 'TUBE,SUPPLY,REEL', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.64545', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (495, 'TUBE,CHOKE PUMP,22', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.646365', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (496, 'CHOKE,MOTOR.,ASSY', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.647178', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (497, 'C.B.P-TITE SCREW,2X4,F/ZN-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.647925', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (498, 'C.B.SCREW,2.6X4,F/ZN-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.648692', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (499, 'HOLDER,MOTOR,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.65195', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (500, 'GROUNDING PLATE,MOTOR,INPUT VALVE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.654822', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (501, 'SPUR GEAR,7.6', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.655716', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (502, 'COMBINATION GEAR,12.4,21.2', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.656601', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (503, 'PHOTO INTERRUPTER', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.657482', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (504, 'MOTOR,CR,UD,SUB ASS''Y', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.658431', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (505, 'JOINT,DUCT PUMP,4.,ASSY', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.659388', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (506, 'SEAL,SUPPLY HOLE,LSM', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.661017', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (507, 'JOINT,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.661937', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (508, 'SEAL,JOINT,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.662826', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (509, 'FIXING PLATE,DUCT,PUMP,IR', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.663814', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (510, 'SHEET,FASTEN,HARNESS', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.6648', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (511, 'SHEET,PROTECTION,SENSER', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.665699', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (512, 'DUCT,PUMP,4.,ASSY;B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.666543', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (513, 'C.B.P-TITE SCREW,3X20,F/ZN-3C', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.667305', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (514, 'DIAPHRAGM,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.668231', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (515, 'COMPRESSION SPRING,0.39', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.67068', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (516, 'FRAME,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.671768', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (517, 'JOINT,SEAL,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.672579', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (518, 'PISTON,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.67335', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (519, 'PISTON,DUCT,PUMP;B', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.67418', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (520, 'STRENGTHEN,DUCT,PUMP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.675191', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (521, 'COMPRESSION SPRING,12.76G', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.676212', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (522, 'O RING,DP', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.677024', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (523, 'WELDING DUCT,PUMP.,ASSY', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.677836', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (524, 'PHOTO SENSOR,KI1305-AALF', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.678617', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (525, 'HARNESS,IES', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.679589', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (526, 'CONNECTOR,RELAY,3P', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.680504', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (527, 'HARNESS,CHOKE', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.68138', NULL);
INSERT INTO "public"."part"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (528, 'HARNESS,IES,4,CH74', '', 't', '', 1, '2019-12-16 01:09:32.224371', NULL, '2019-12-19 17:48:35.682141', NULL);
select setval('part_id_seq',529);

INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (343, 'IS板金Cam Bolt位置決定治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (344, 'Cover Maintenance Home Assy取付保持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (345, 'IS板金Z調整治具 Master', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (346, 'IS板金Z調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (347, 'IS板金X/Y調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (348, 'CR保持治具(Head Assemble)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (349, '供給口保護治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (350, 'Tube ASSY Spacer治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (351, '従動荷重調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (352, 'PF Coupling 圧入治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (353, 'PF Coupling 圧入作業支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (354, 'PF Coupling 打螺絲止轉治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (355, 'PF駆動軸調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (356, 'PF軸Y調整軸', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (357, 'PF軸調整Driver', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (358, 'PF Scale 取付補助治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (359, 'PF Scale保護治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (360, 'PF軸保護Sheet', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (361, 'Platen 調整治具 Master', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (362, 'Platen 調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (363, 'Platen 調整治具位置決定治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (364, 'A寸法確認治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (365, 'Platen Y調板金位置決定治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (366, 'After Heater Guide Bolt調整治具（Z/Y）', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (367, 'Assy搬送Lift', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (368, 'P Cover Dumper取付支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (369, 'Main Frame Assy搬送Lift', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (370, 'Main Frame 反転作業台車', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (371, '筒治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (372, 'Frame Support Upper Right ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (373, 'Frame Fan Left/Right ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (374, 'Frame Cover Top ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (375, 'Frame Left/Ringht Top ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (376, 'SSD ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (377, 'Board Main Bov ASSY Fan組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (378, '廃液Bottle Holder ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (379, 'Cover Maintenance Home SUB ASSY取付保持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (380, 'Fan 組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (381, 'Cover Lock Lever組立支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (382, 'Frame Support Front Left/Right ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (383, 'Ojigi_Katamuki調整治具 Master', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (384, 'Ojigi_Katamuki調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (385, 'RGB Camera ASSY検査治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (386, 'CPAD Sensor 貼付治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (387, 'CPAD Sensor 計測治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (388, '桁方向確認治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (389, 'CR Belt Pull治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (390, 'CR Belt組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (391, 'Main CR LM Holder取付支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (392, 'Main CR 取付支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (393, 'APG Shaft保護治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (394, 'CR Scale調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (395, 'LM下軸調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (396, 'Main CR組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (397, 'CR組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (398, 'PG Plate CR組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (399, 'Scale Encoder ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (400, 'CR Motor 組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (401, 'CR Motor Holder組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (402, 'FRAME ASSEMBLY Drive B組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (403, 'HEAD駆動基板組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (404, 'Tube ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (405, 'CR移動規制治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (406, 'SUB組定盤LM Guide保護Cover', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (407, 'SUB組定盤Dummy PF保護Cover', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (408, 'SUB組定盤治具(含む従動調整治具)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (409, 'Platen Block Z粗調治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (410, 'Frame Base Paperguide Front ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (411, 'EJ Motor Assy組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (412, '除電布組立補助台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (413, '従動軸Assy組立治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (414, 'PF Bearing 圧入治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (415, 'PF Pully Pin 圧入治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (416, 'PF Pully 組立支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (417, 'PF Pully 圧入治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (418, 'PF Z Cam Shaft Pin 圧入治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (419, 'PF Motor Assy組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (420, 'Frame Adjust PF ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (421, 'RELEASE GEAR ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (422, 'Coupling Key圧入治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (423, '硬化Heater組立反転搬送治具台車', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (424, '硬化Fan回転測定治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (425, 'HEATER SIDE PLATE組立支持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (426, 'FRAME FASTEN HEATER SUB ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (427, 'PREHETER UPPER ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (428, '硬化Heater Side Plate組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (429, '硬化Heater 金網誤組防止治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (430, 'HEATER貼付補助治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (431, 'Head FFC 确认 JIG(TESTER JIG)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (432, '乾燥Fan回転測定治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (433, 'DRYING FAN ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (434, 'Roll/Reel Motor Assy組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (435, 'Roll/Reel STAY 組立支持定盤', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (436, 'Reel STAY 検査间隙Gauge', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (437, 'STAND ROLL ASSY組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (438, 'JACK ASSY ROLLER押込治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (439, 'MEDIA GUIDE BAR組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (440, 'ROLL/REEL SW組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (441, 'JACK ROLLER 注油台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (442, 'Stand Assy 回転治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (443, 'Stand Caster治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (444, 'BIB搬送治具台車', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (445, 'LEVER 組立支持台', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (446, 'BASE ASSY 移動作業補助棒', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (447, 'SUB CR駆動治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (448, 'PF従動調整治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (449, 'Cable 保持治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (450, '従動荷重切替Gear固定PIN治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (451, 'PF Gear取付補助PIN治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (452, '偏心Driver A', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (453, '偏心Driver B', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (454, 'HEATER取付確認治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (455, 'HEATER Harness確認治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (456, '治具BIB Unit', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (457, 'INTER LOCK解除治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (458, 'Printer Cover Sensor Push 治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (459, 'Release Lever 治具 (前)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (460, 'Release Lever 治具 (后)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (461, '負圧測定器', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (462, 'Platen Mask Sheet治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (463, '治具ROLL STAY', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (464, '治具REEL STAY', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (465, '治具STAND', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (466, '治具廃液Tank(検査用)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (467, 'Micro trace治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (468, '充填治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (469, '洗浄治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (470, '洗浄液承受治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (471, 'Leak 検査治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (472, '治具ROLL', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (473, '治具REEL', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (474, '治具布Wiper', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (475, '治具布Wiper Cassette', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (476, '治具吸収材', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (477, '治具廃液Tank(廃液用)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (478, 'PG確認间隙Gauge', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (479, '絶縁耐圧Relay Open治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (480, '信号灯治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (481, 'Lifter(本体梱包)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (482, 'Lifter(卡板)', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (483, '3D Measurement Master Pin治具', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (484, 'Pre Heater Mounting Plate Positioning JIG', NULL, 'f', '治具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (485, '非接触三次元測定System(Comet)', NULL, 'f', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (486, '高扭力批', NULL, 'f', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (487, '水準調整治具（水準器）', NULL, 'f', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (488, 'Earth線', NULL, 'f', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (489, '精密石定盤治具(小)', NULL, 'f', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (490, 'QR Reader扫描器', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (491, '張力計', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (492, '印字用AC Cable', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (493, '接地安全测试仪', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (494, '绝缘耐压测试仪', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (495, '安全試験機用AC Cable', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (496, '安全漏电检查仪', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (497, '安全漏电检查仪option', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (498, 'Leak電流試験用AC Cable', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (499, '非接触温度計', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (500, 'PC电脑(Desk Top/検プロ用）ケンヨウ', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (501, '推拉力计', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (502, '高速电批', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (503, '批嘴（高速电批）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (504, '低速电批', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (505, '批嘴（低速电批）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (506, '批嘴（手批）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (507, '碟型内六角套筒', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (508, '螺丝拧紧机', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (509, '六角固定杆', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (510, '六角批嘴', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (511, '六角扳手套筒', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (512, '插板', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (513, '六角批嘴（高速电批）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (514, '六角批嘴（低速电批）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (515, '六角批嘴（高扭力电批）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (516, '扳手', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (517, '扳手套筒', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (518, '六角头扭矩扳手', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (519, '镊子（塑胶）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (520, '镊子（金属）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (521, '注射器', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (522, '毛笔刷', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (523, '厚度规', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (524, 'E-RING座', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (525, '打点笔', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (526, '酒精瓶', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (527, '弹簧销钉（推）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (528, '弹簧销钉（拉）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (529, '螺旋扳手', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (530, '卡环钳', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (531, 'C-Ring钳', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (532, '尖嘴钳', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (533, '内六角批嘴', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (534, '静电手环（有线）', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (535, '安全防护眼镜', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (536, '手套', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (537, '安全帽', NULL, 't', '工具', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (538, '夹子', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (539, '案内板', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (540, '螺丝刀', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (541, '组立治具', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (542, '镊子', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (543, '笔', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (544, '笔盖', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (545, '物料箱', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (546, '帘子', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (547, '皱纹胶纸', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (548, '治具', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (549, '治具手柄', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (550, '扫描仪', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (551, '启动键', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (552, '确认键', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (553, 'AUTO', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (554, 'START', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (555, '计测器', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (556, '接着剂', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (557, '照度计', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (558, '硬化剂', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (559, '结果确认OK', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (560, '风枪', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (561, '流动票', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (562, '电源线', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (563, 'Enter', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (564, 'Power', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (565, 'OK', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (566, '关联笔', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (567, '制造号', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (568, '看板卡', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (569, '出机单', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (570, '捆包膜', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (571, '脚刹', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (572, '打印机', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (573, '调整杆', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (574, '遮光盖', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (575, '挡板', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (576, '棉签', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (577, '全棉纸', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (578, '白光布', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (579, '酒精', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (580, '尺子', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (581, '剪刀', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (582, '胶纸机', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (583, '鼠标', NULL, 't', '', 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (584, '手批', NULL, 't', NULL, 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (585, '电批', NULL, 't', NULL, 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (586, '激光熔着设备', NULL, 't', NULL, 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (587, '熔着设备', NULL, 't', NULL, 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
INSERT INTO "public"."tool"("id", "name", "pinyin", "common", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (588, '测试设备', NULL, 't', NULL, 1, '2019-12-16 01:18:27.163927', NULL, NULL, NULL);
select setval('tool_id_seq',686);

INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (1, 'Take and keep', 'Take and keep', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (2, 'Take and put', 'Take and put', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (3, 'Take and install', 'Take and install', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (4, 'Take and insert', 'Take and insert', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (5, 'Take and through', 'Take and through', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (6, 'Take and check', 'Take and check', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (7, 'Move mouse to', 'Move mouse to', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (8, 'Select it', 'Select it', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (9, 'Pull out materiel box', 'Pull out materiel box', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (10, 'Push materiel box', 'Push materiel box', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (11, 'Press it smoothly', 'Press it smoothly', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (12, 'Throw plastic bag', 'Throw plastic bag', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (13, 'Move stool to right place', 'Move stool to right place', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (14, 'Move materiel car to right place', 'Move materiel car to right place', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (15, 'Take up', 'Take up', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (16, 'Put down', 'Put down', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (17, '取.....保持', 'qubaochi', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (18, '取.....放', 'qufang', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (19, '取.....安装', 'quanzhuang', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (20, '取.....插入', 'qucharu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (21, '取.....夹住', 'qujiazhu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (22, '将.....放', 'jiangfang', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (23, '将.....贴付', 'jiangtiefu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (24, '将.....放入', 'jiangfangru', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (25, '将.....对准', 'jiangduizhun', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (26, '将.....安装', 'jianganzhuang', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (27, '撕掉.....保护纸', 'sidiaobaohuzhi', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (28, '拿.....压', 'naya', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (29, '紧.....到', 'jindao', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (30, '扔掉.....袋子', 'rengdiaodaizi', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (31, '多余.....放回', 'duoyufanghui', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (32, '打开.....袋子', 'daikaidaizi', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (33, '取.....检查放', 'qujianchafang', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (34, '取.....排入', 'qupairu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (35, '核对....一致', 'heduiyizhi', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (36, '拉出', 'lachu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (37, '推回', 'tuihui', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (38, '拧紧', 'ningjin', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (39, '按压', 'anya', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (40, '推', 'tui', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (41, '拿…', 'na', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (42, '放…到工作台', 'fangdaogongzuotai', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (43, '安装…到…', 'anzhuangdao', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (44, '检查…', 'jiancha', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (45, '确认…', 'queren', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (46, '拿保持…', 'nabaochi', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (47, '测量…', 'celiang', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (48, '按压…', 'anya', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (49, '按…', 'zhuan', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (50, '转动...', 'zhuandong', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (51, '翻转…', 'fanzhuan', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (52, '撕…', 'si', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (53, '推…', 'tui', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (54, '插入…到…', 'charudao', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (55, '返回…', 'fanhui', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (56, '拉…', 'la', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (57, '打开…', 'dakai', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (58, '紧…到…', 'jindao', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (59, '拿…安装到…', 'naanzhuangdao', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (60, '返回剩于…', 'fanhuishengyu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (61, '抓住…', 'zhuazhu', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (62, '放…到物料盒', 'fangdaowuliaohe', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
INSERT INTO "public"."action"("id", "name", "pinyin", "remark", "create_by", "create_at", "update_by", "update_at", "delete_at") VALUES (63, '拧松…', 'ningsong', NULL, 11, '2019-12-04 18:57:13.082144', NULL, NULL, NULL);
select setval('action_id_seq',64);