CREATE TABLE sys_user (
  id                int(11)         NOT NULL AUTO_INCREMENT,
  created_time      timestamp       NULL        DEFAULT CURRENT_TIMESTAMP,
  creator           int(11)         NULL        DEFAULT NULL,
  updated_time      timestamp       NULL        DEFAULT CURRENT_TIMESTAMP,
  updater           int(11)         NULL        DEFAULT NULL,
  is_delete         tinyint(1)      NOT NULL    DEFAULT '0' COMMENT '逻辑删除 0: 未删除 1: 已删除',
  username          varchar(255)    NULL        DEFAULT NULL COMMENT '用户名',
  password          varchar(255)    NULL        DEFAULT NULL COMMENT '密码',
  email             varchar(255)    NULL        DEFAULT NULL COMMENT '邮箱',
  phone             varchar(255)    NULL        DEFAULT NULL COMMENT '手机号',
  last_login_time   int(11)         NULL        DEFAULT NULL COMMENT '最后登陆时间',
  last_login_ip     varchar(255)    NULL        DEFAULT NULL COMMENT '最后登陆id',
  is_enable         tinyint(1)      NOT NULL    DEFAULT 0    COMMENT '是否启用 0:未启用/1:启用',
  salt              varchar(20)     NOT NULL    COMMENT '盐',
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;