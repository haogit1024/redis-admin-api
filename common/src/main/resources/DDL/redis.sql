CREATE TABLE redis (
id                    int(11)         NOT NULL AUTO_INCREMENT,
created_time          timestamp       NULL        DEFAULT CURRENT_TIMESTAMP,
creator               int(11)         NULL        DEFAULT NULL,
updated_time          timestamp       NULL        DEFAULT NULL,
updater               int(11)         NULL        DEFAULT NULL,
is_delete             tinyint(1)      NOT NULL    DEFAULT '0' COMMENT '逻辑删除 0: 未删除 1: 已删除',
host                  text            NULL        DEFAULT NULL COMMENT 'host',
port                  text            NULL        DEFAULT NULL COMMENT '端口',
password              text            NULL        DEFAULT NULL COMMENT '密码',
last_connection_time  int(11)         NULL        DEFAULT NULL COMMENT '最后连接时间',
last_connection_ip    varchar(255)    NULL        DEFAULT NULL COMMENT '最后登陆id',
name                  varchar(255)    NULL        DEFAULT NULL COMMENT '名称',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;