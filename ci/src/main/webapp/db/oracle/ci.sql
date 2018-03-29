CREATE USER ci IDENTIFIED BY ci;
GRANT CONNECT,RESOURCE TO ci;
CONNECT ci/ci;

create table pipelining_info  (
   id                   VARCHAR2(32)                    not null,
   pip_name             VARCHAR2(100),
   execution_mode       VARCHAR2(2),
   timed_cron           VARCHAR2(100),
   mail_address         VARCHAR2(300),
   create_time          VARCHAR2(20),
   create_user          VARCHAR2(32),
   project_id           VARCHAR2(32),
   constraint PK_PIPELINING_INFO primary key (id)
);

comment on table pipelining_info is
'流水线表';

comment on column pipelining_info.id is
'主键';

comment on column pipelining_info.pip_name is
'流水线名称';

comment on column pipelining_info.execution_mode is
'执行方式(1,手动；2,自动)';

comment on column pipelining_info.timed_cron is
'定时任务表达式';

comment on column pipelining_info.mail_address is
'发送邮件地址';

comment on column pipelining_info.create_time is
'创建时间';

comment on column pipelining_info.create_user is
'创建人';

comment on column pipelining_info.project_id is
'项目id';
/*==============================================================*/
/* Table: project_info                                          */
/*==============================================================*/
create table project_info  (
   project_id           varchar2(32)                    not null,
   project_name         varchar2(300),
   project_describe     varchar2(1000),
   create_user          varchar2(32),
   create_time          varchar2(20),
   project_status       varchar2(2),
   constraint pk_project_info primary key (project_id)
);

comment on table project_info is
'项目表';

comment on column project_info.project_id is
'主键';

comment on column project_info.project_name is
'项目名称';

comment on column project_info.project_describe is
'描述';

comment on column project_info.create_user is
'创建人';

comment on column project_info.create_time is
'创建时间';

comment on column project_info.project_status is
'状态';
/*==============================================================*/
/* Table: code_repositories                                     */
/*==============================================================*/
create table code_repositories  (
   id                   varchar2(32)                    not null,
   rep_name             varchar2(300),
   rep_describe         varchar2(1000),
   rep_url              varchar2(200),
   rep_version          varchar2(2),
   rep_account_number   varchar2(100),
   rep_password         varchar2(100),
   constraint pk_code_repositories primary key (id)
);

comment on table code_repositories is
'代码仓库';

comment on column code_repositories.id is
'主键';

comment on column code_repositories.rep_name is
'仓库名称';

comment on column code_repositories.rep_describe is
'描述';

comment on column code_repositories.rep_url is
'url';

comment on column code_repositories.rep_version is
'版本库类型';

comment on column code_repositories.rep_account_number is
'账号';

comment on column code_repositories.rep_password is
'密码';
/*==============================================================*/
/* Table: s_dic                                                 */
/*==============================================================*/
create table s_dic  (
   dic_id               varchar2(32)                    not null,
   dic_code             varchar2(100),
   dic_name             varchar2(100),
   dic_icon             varchar2(300),
   dic_sort             varchar2(4),
   parent_dic_id        varchar2(32),
   constraint pk_s_dic primary key (dic_id)
);

comment on table s_dic is
'字典表';

comment on column s_dic.dic_id is
'主键';

comment on column s_dic.dic_code is
'类型编码';

comment on column s_dic.dic_name is
'类型名称';

comment on column s_dic.dic_icon is
'类型图标';

comment on column s_dic.dic_sort is
'序号';

comment on column s_dic.parent_dic_id is
'父id';
/*==============================================================*/
/* Table: pipelining_steps                                      */
/*==============================================================*/
create table pipelining_steps  (
   id                   varchar2(32)                    not null,
   pip_steps_name       varchar2(100),
   pip_steps_sort       varchar2(10),
   create_user          varchar2(32),
   create_time          varchar2(20),
   pip_id               varchar2(32),
   constraint pk_pipelining_steps primary key (id)
);

comment on table pipelining_steps is
'流水线步骤';

/*==============================================================*/
/* Table: build_history                                         */
/*==============================================================*/
create table build_history  (
   id                   VARCHAR2(32)                    not null,
   pip_id               VARCHAR2(32),
   pip_task_id          VARCHAR2(32),
   build_start_time     VARCHAR2(20),
   build_end_time       VARCHAR2(20),
   build_status         VARCHAR2(2),
   build_user           VARCHAR2(32),
   build_total_time     VARCHAR2(20),
   pip_version          VARCHAR2(32),
   task_version         VARCHAR2(32),
   apply_url            VARCHAR2(500),
   parent_pip_id        VARCHAR2(32),
   constraint PK_BUILD_HISTORY primary key (id)
);

comment on table build_history is
'构建历史';

comment on column build_history.id is
'主键';

comment on column build_history.pip_id is
'流水线id';

comment on column build_history.pip_task_id is
'流水线任务关联Id';

comment on column build_history.build_start_time is
'构建开始时间';

comment on column build_history.build_end_time is
'构建结束时间';

comment on column build_history.build_status is
'构建状态';

comment on column build_history.build_user is
'构建人';

comment on column build_history.build_total_time is
'构建时长(毫秒数)';

comment on column build_history.pip_version is
'流水线版本号';

comment on column build_history.task_version is
'任务版本号';

comment on column build_history.parent_pip_id is
'父流水线主键';

/*==============================================================*/
/* Table: task_deploy                                           */
/*==============================================================*/
create table task_deploy  (
   id                   varchar2(32)                    not null,
   deploy_name          varchar2(100),
   create_user          varchar2(32),
   create_time          varchar2(20),
   mould_id             varchar2(32),
   node_id              varchar2(32),
   real_deploy_name		varchar2(100),
   build_id				varchar2(32),
   build_mode           varchar2(2),
   constraint pk_task_deploy primary key (id)
);

comment on table task_deploy is
'部署任务';
/*==============================================================*/
/* Table: node_info                                             */
/*==============================================================*/
create table node_info  (
   id                   varchar2(32)                    not null,
   node_ip              varchar2(50),
   node_port            varchar2(50),
   node_account_number  varchar2(100),
   node_password        varchar2(100),
   history_id          VARCHAR2(32),
   app_id              VARCHAR2(32),
   constraint pk_node_info primary key (id)
);

comment on table node_info is
'节点表';
/*==============================================================*/
/* Table: task_auto_test                                        */
/*==============================================================*/
create table task_auto_test  (
   id                   VARCHAR2(32)                    not null,
   autotest_id          VARCHAR2(50),
   test_name            VARCHAR2(100),
   create_user          VARCHAR2(32),
   create_time          VARCHAR2(20),
   request_url          VARCHAR2(300),
   respose_url          VARCHAR2(300),
   deploy_id            VARCHAR2(32),
   TEST_TYPE            VARCHAR2(100),
   constraint PK_TASK_AUTO_TEST primary key (id)
);

comment on table task_auto_test is
'自动化测试';

comment on column task_auto_test.id is
'主键';

comment on column task_auto_test.autotest_id is
'主键-接口提供';

comment on column task_auto_test.test_name is
'任务名称';

comment on column task_auto_test.create_user is
'创建人';

comment on column task_auto_test.create_time is
'创建时间';

comment on column task_auto_test.request_url is
'执行url';

comment on column task_auto_test.respose_url is
'回调接口';

comment on column task_auto_test.deploy_id is
'部署id';
/*==============================================================*/
/* Table: task_build                                            */
/*==============================================================*/
create table task_build  (
   id                   varchar2(32)                    not null,
   build_name           varchar2(100),
   build_environment    varchar2(2),
   build_type           varchar2(2),
   code_rep_id          varchar2(32),
   create_user          varchar2(32),
   create_time          varchar2(20),
   real_build_name      varchar2(100),
   constraint pk_task_build primary key (id)
);

comment on table task_build is
'构建任务';
/*==============================================================*/
/* Table: pipelining_steps_task                                 */
/*==============================================================*/
create table pipelining_steps_task  (
   id                   varchar2(32)                    not null,
   pip_steps_id         varchar2(32),
   task_id              varchar2(32),
   task_type            varchar2(2),
   sort                 varchar2(4),
   constraint pk_pipelining_steps_task primary key (id)
);

comment on table pipelining_steps_task is
'步骤任务关联表';

comment on column pipelining_steps_task.id is
'主键';

comment on column pipelining_steps_task.pip_steps_id is
'步骤id';

comment on column pipelining_steps_task.task_id is
'任务id';

comment on column pipelining_steps_task.task_type is
'任务类型';

comment on column pipelining_steps_task.sort is
'序号';

/*==============================================================*/
/* Table: pipelining_task_parameter                             */
/*==============================================================*/
create table pipelining_task_parameter  (
   id                   VARCHAR2(32)                    not null,
   pip_task_id          VARCHAR2(32),
   param_name           VARCHAR2(100),
   param_type           VARCHAR2(20),
   param_describe       VARCHAR2(300),
   param_sort           VARCHAR2(4),
   param_source         VARCHAR2(100),
   param_source_key     VARCHAR2(100),
   constraint PK_PIPELINING_TASK_PARAMETER primary key (id)
);

comment on table pipelining_task_parameter is
'任务参数配置';

comment on column pipelining_task_parameter.id is
'主键';

comment on column pipelining_task_parameter.pip_task_id is
'步骤id';

comment on column pipelining_task_parameter.param_name is
'参数名称';

comment on column pipelining_task_parameter.param_type is
'参数类型';

comment on column pipelining_task_parameter.param_describe is
'参数描述';

comment on column pipelining_task_parameter.param_sort is
'序号';

comment on column pipelining_task_parameter.param_source is
'参数来源（步骤任务关联Id）';

/*==============================================================*/
/* Table: pipelining_task                                       */
/*==============================================================*/
create table pipelining_task  (
   id                   VARCHAR2(32)                    not null,
   pip_id               VARCHAR2(32),
   task_id              VARCHAR2(32),
   task_type            VARCHAR2(2),
   sort                 VARCHAR2(4),
   constraint PK_PIPELINING_TASK primary key (id)
);

comment on table pipelining_task is
'流水线任务关联表';

comment on column pipelining_task.id is
'主键';

comment on column pipelining_task.pip_id is
'流水线id';

comment on column pipelining_task.task_id is
'任务id';

comment on column pipelining_task.task_type is
'任务类型（0子流水线，1构建任务，2部署任务，3自动化测试任务）';

comment on column pipelining_task.sort is
'序号';

/*==============================================================*/
/* Table: task_parameter                                        */
/*==============================================================*/
create table task_parameter  (
   id                   VARCHAR2(32)                    not null,
   task_id              VARCHAR2(32),
   task_type            VARCHAR2(2),
   param_name           VARCHAR2(100),
   param_type           VARCHAR2(20),
   param_default        VARCHAR2(100),
   param_describe       VARCHAR2(300),
   param_sort           VARCHAR2(4),
   constraint PK_TASK_PARAMETER primary key (id)
);

comment on table task_parameter is
'任务参数配置';

comment on column task_parameter.id is
'主键';

comment on column task_parameter.task_id is
'任务id';

comment on column task_parameter.task_type is
'任务类型（0子流水线，1构建任务，2部署任务，3自动化测试任务）';

comment on column task_parameter.param_name is
'参数名称';

comment on column task_parameter.param_type is
'参数类型';

comment on column task_parameter.param_default is
'参数默认值';

comment on column task_parameter.param_describe is
'参数描述';

comment on column task_parameter.param_sort is
'序号';

/*==============================================================*/
/* Table: build_history_product                                 */
/*==============================================================*/
create table build_history_product  (
   ID                   VARCHAR2(32)                    not null,
   build_history_id     VARCHAR2(32),
   product_key          VARCHAR2(100),
   product_value        VARCHAR2(300),
   pipe_task_parameter_id VARCHAR2(32),
   constraint PK_BUILD_HISTORY_PRODUCT primary key (ID)
);

comment on table build_history_product is
'构建历史产出物';

comment on column build_history_product.ID is
'主键';

comment on column build_history_product.build_history_id is
'构建历史主键';

comment on column build_history_product.product_key is
'产出物key';

comment on column build_history_product.product_value is
'产出物值';

comment on column build_history_product.pipe_task_parameter_id is
'流水线任务参数配置Id';

/*==============================================================*/
/* Table: jenkins_credentials                                   */
/*==============================================================*/
create table jenkins_credentials  (
   ID                   VARCHAR2(32)                    not null,
   CredentialID         VARCHAR2(250),
   username             VARCHAR2(100),
   Password             VARCHAR2(100),
   is_use               VARCHAR2(1),
   constraint PK_JENKINS_CREDENTIALS primary key (ID)
);

comment on table jenkins_credentials is
'jenkins中认证账号表';

comment on column jenkins_credentials.ID is
'主键';

comment on column jenkins_credentials.CredentialID is
'jenkins认证唯一标识';

comment on column jenkins_credentials.username is
'账号名称';

comment on column jenkins_credentials.Password is
'账号密码';

comment on column jenkins_credentials.is_use is
'是否可用(1表示可用，0表示已作废)';

