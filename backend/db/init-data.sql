
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
INSERT INTO sys_menu VALUES ('1', '0', '系统管理', null, null, '0', 'system', '0', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('2', '1', '用户管理', 'sys/user', null, '1', 'menu', '1', '1', '2018-12-23 09:37:41', '1', '2019-04-26 05:31:17', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('3', '1', '角色管理', 'sys/role', null, '1', 'role', '2', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('4', '1', '菜单管理', 'sys/menu', null, '1', 'menu', '3', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('5', '1', 'SQL监控', 'http://localhost:8080/renren-fast/druid/sql.html', null, '1', 'sql', '4', '1', '2018-12-23 09:37:41', null, null, '2019-01-14 11:09:58');
INSERT INTO sys_menu VALUES ('6', '1', '定时任务', 'job/schedule', null, '1', 'job', '5', '1', '2018-12-23 09:37:41', null, null, '2019-02-16 14:52:15');
INSERT INTO sys_menu VALUES ('7', '6', '查看', null, 'sys:schedule:list,sys:schedule:info', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-14 14:13:07');
INSERT INTO sys_menu VALUES ('8', '6', '新增', null, 'sys:schedule:save', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-14 14:13:09');
INSERT INTO sys_menu VALUES ('9', '6', '修改', null, 'sys:schedule:update', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-14 14:13:11');
INSERT INTO sys_menu VALUES ('10', '6', '删除', null, 'sys:schedule:delete', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-14 14:13:14');
INSERT INTO sys_menu VALUES ('11', '6', '暂停', null, 'sys:schedule:pause', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-16 14:52:11');
INSERT INTO sys_menu VALUES ('12', '6', '恢复', null, 'sys:schedule:resume', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-14 14:13:19');
INSERT INTO sys_menu VALUES ('13', '6', '立即执行', null, 'sys:schedule:run', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-16 14:52:08');
INSERT INTO sys_menu VALUES ('14', '6', '日志列表', null, 'sys:schedule:log', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '2019-02-14 14:13:34');
INSERT INTO sys_menu VALUES ('15', '2', '列表', null, 'sys:user:list,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 05:36:29', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('16', '2', '新增', null, 'sys:user:create,sys:user:list,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 05:28:23', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('17', '2', '修改', null, 'sys:user:update,sys:user:list,sys:user:info,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 06:23:47', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('18', '2', '删除', null, 'sys:user:delete', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('19', '3', '列表', null, 'sys:role:list,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 05:38:40', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('20', '3', '新增', null, 'sys:role:create,sys:role:list,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 05:39:19', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('21', '3', '修改', null, 'sys:role:update,sys:menu:list,sys:role:info,basic:dept:list,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 06:12:26', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('22', '3', '删除', null, 'sys:role:delete,', '2', null, '0', '1', '2018-12-23 09:37:41', '1', '2019-04-26 05:41:13', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('23', '4', '查看', null, 'sys:menu:list,sys:menu:info', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('24', '4', '新增', null, 'sys:menu:create,sys:menu:list', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('25', '4', '修改', null, 'sys:menu:update,sys:menu:list', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('26', '4', '删除', null, 'sys:menu:delete', '2', null, '0', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('27', '1', '参数管理', 'sys/config', null, '1', 'config', '6', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('29', '1', '系统日志', 'sys/log', 'sys:log:list', '1', 'log', '7', '1', '2018-12-23 09:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('30', '1', '文件上传', 'oss/oss', 'sys:oss:all', '1', 'oss', '6', '1', '2018-12-23 09:37:41', null, null, '2019-01-14 15:09:00');
INSERT INTO sys_menu VALUES ('31', '0', '基础数据', null, null, '0', 'basic', '0', '1', '2017-03-23 22:37:41', '2', '2019-02-16 14:55:48', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('42', '31', '岗位信息', 'basic/job', null, '1', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('43', '42', '查看', null, 'basic:job:list,basic:job:info', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('44', '42', '新增', null, 'basic:job:save', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('45', '42', '修改', null, 'basic:job:update', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('46', '42', '删除', null, 'basic:job:delete', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('47', '31', '人员信息', 'basic/member', null, '1', null, '4', '1', '2017-03-23 22:37:41', '2', '2019-01-04 13:36:19', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('48', '47', '查看', null, 'basic:member:list,basic:member:info', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('49', '47', '新增', null, 'basic:member:save', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('50', '47', '修改', null, 'basic:member:update', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('51', '47', '删除', null, 'basic:member:delete', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('72', '31', '部门管理', 'basic/dept', null, '1', null, '5', '1', '2017-03-23 22:37:41', '1', '2019-04-26 06:50:28', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('73', '72', '列表', null, 'basic:dept:list,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 06:47:22', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('74', '72', '新增', null, 'basic:dept:create,basic:dept:list,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 06:47:51', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('75', '72', '修改', null, 'basic:dept:update,basic:dept:list,basic:dept:info,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 06:48:28', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('76', '72', '删除', null, 'basic:dept:delete,basic:dept:list,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 06:48:48', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('77', '1', '字典管理', 'sys/dict', null, '1', 'menu', '8', '1', '2017-03-23 22:37:41', '1', '2019-04-26 07:04:43', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('78', '77', '列表', null, 'sys:dict:list,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 07:05:20', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('79', '77', '新增', null, 'sys:dict:create,sys:dict:list,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 07:05:51', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('80', '77', '修改', null, 'sys:dict:update,sys:dict:list,sys:dict:info,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 07:06:20', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('81', '77', '删除', null, 'sys:dict:delete,sys:dict:list,', '2', null, '6', '1', '2017-03-23 22:37:41', '1', '2019-04-26 07:06:39', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('82', '31', '字典项', 'sys/dict', null, '1', null, '6', '1', '2017-03-23 22:37:41', null, null, '2019-01-03 09:17:09');
INSERT INTO sys_menu VALUES ('83', '82', '查看', null, 'sys:dict:list,sys:dict:info', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '2019-01-03 09:17:04');
INSERT INTO sys_menu VALUES ('84', '82', '新增', null, 'sys:dict:save', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '2019-01-03 09:17:03');
INSERT INTO sys_menu VALUES ('85', '82', '修改', null, 'sys:dict:update', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '2019-01-03 09:17:01');
INSERT INTO sys_menu VALUES ('86', '82', '删除', null, 'sys:dict:delete', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '2019-01-03 09:16:59');
INSERT INTO sys_menu VALUES ('87', '1', '编码规则', 'sys/coderule', null, '1', null, '10', '1', '2017-03-23 22:37:41', '2', '2019-01-04 13:32:41', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('88', '87', '查看', null, 'sys:coderule:list,sys:coderule:info', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('89', '87', '新增', null, 'sys:coderule:save', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('90', '87', '修改', null, 'sys:coderule:update', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('91', '87', '删除', null, 'sys:coderule:delete', '2', null, '6', '1', '2017-03-23 22:37:41', null, null, '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('292', '2', '重置密码', null, 'sys:user:reset', '2', null, '0', '3', '2019-02-18 17:49:27', null, '2019-02-18 17:49:27', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('293', '0', '审批管理', 'approve', null, '0', null, '0', '1', '2019-03-01 09:49:45', null, '2019-03-01 09:49:45', '2019-04-26 06:56:19');
INSERT INTO sys_menu VALUES ('294', '293', '调拨单-总仓调拨', 'approve/transfer01', null, '1', null, '0', '1', '2019-03-01 09:51:21', null, '2019-03-01 09:51:21', '2019-04-26 06:56:15');
INSERT INTO sys_menu VALUES ('295', '294', '审批节点01', null, 'approve:transfer01:node01', '2', null, '0', '1', '2019-03-01 09:53:59', '1', '2019-03-02 14:24:41', '2019-04-26 06:56:12');
INSERT INTO sys_menu VALUES ('296', '294', '审批节点02', null, 'approve:transfer01:node02', '2', null, '0', '1', '2019-03-02 11:15:12', null, '2019-03-02 11:15:12', '2019-04-26 06:56:08');
INSERT INTO sys_menu VALUES ('297', '293', '调拨单-申领调拨', 'approve/transfer02', null, '1', null, '0', '1', '2019-03-01 09:51:21', null, '2019-03-01 09:51:21', '2019-04-26 06:56:00');
INSERT INTO sys_menu VALUES ('298', '297', '审批节点01', null, 'approve:transfer02:node01', '2', null, '0', '1', '2019-03-01 09:53:59', '1', '2019-03-02 14:24:41', '2019-04-26 06:55:57');
INSERT INTO sys_menu VALUES ('299', '293', '调拨单-区域调拨', 'approve/transfer03', null, '1', null, '0', '1', '2019-03-01 09:51:21', null, '2019-03-01 09:51:21', '2019-04-26 06:55:52');
INSERT INTO sys_menu VALUES ('300', '299', '审批节点01', null, 'approve:transfer03:node01', '2', null, '0', '1', '2019-03-01 09:53:59', '1', '2019-03-02 14:24:41', '2019-04-26 06:55:49');
INSERT INTO sys_menu VALUES ('301', '293', '物料退回单-审批', 'approve/sparepartreturn', null, '1', null, '0', '1', '2019-03-01 09:53:59', '1', '2019-03-01 09:53:59', '2019-04-26 06:55:42');
INSERT INTO sys_menu VALUES ('302', '301', '审批节点01', null, 'approve:sparepartreturn:node01', '2', null, '0', '1', '2019-03-02 14:29:20', '1', '2019-03-02 14:29:24', '2019-04-26 06:55:38');
INSERT INTO sys_menu VALUES ('303', '27', '列表', null, 'sys:config:list,sys:config:info,', '2', null, null, '1', '2019-04-29 01:51:48', null, '2019-04-29 01:51:48', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('304', '27', '新增', null, 'sys:config:create,sys:config:info,', '2', null, null, '1', '2019-04-29 01:52:52', '1', '2019-04-29 01:55:30', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('305', '27', '修改', null, 'sys:config:update,', '2', null, null, '1', '2019-04-29 01:54:02', null, '2019-04-29 01:54:02', '1970-01-01 00:00:00');
INSERT INTO sys_menu VALUES ('306', '27', '删除', null, 'sys:config:delete,', '2', null, null, '1', '2019-04-29 01:54:47', null, '2019-04-29 01:54:47', '1970-01-01 00:00:00');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO sys_role VALUES (1, 'admin', 1, NULL, 1, '2019-02-25 10:13:01', 1, '2019-03-02 14:30:03', '1970-01-01 00:00:00');

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO sys_user VALUES (1, 1, 'admin', '7d0ec1bcc916e97f9e6392efeee74e6862f566613e7457c710d9862b5a3751e2', 'YzcmCZNvbXocrsz9dm8e', 'root@yu.io', '13800138000', 1, 1, '2016-11-11 11:11:11', NULL, NULL, '1970-01-01 00:00:00');

