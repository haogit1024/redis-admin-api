CREATE TABLE lsh_platform.YOUR_TABLE_NAME
(
    id              int(11)         NOT NULL    AUTO_INCREMENT,
    creator         int(11)         NOT NULL    COMMENT '创建人',
    created_time    datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater         int(11)         NOT NULL    COMMENT '更新人',
    updated_time    datetime        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    is_delete       tinyint(1)      NOT NULL    DEFAULT '0' COMMENT '逻辑删除 0: 未删除 1: 已删除',
    PRIMARY KEY (id)
)