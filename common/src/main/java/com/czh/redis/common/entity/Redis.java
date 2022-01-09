package com.czh.redis.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "redis")
public class Redis extends BaseEntity {
    /**
     * host
     */
    @TableField(value = "`host`")
    private String host;

    /**
     * 端口
     */
    @TableField(value = "port")
    private String port;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 最后连接时间
     */
    @TableField(value = "last_connection_time")
    private Integer lastConnectionTime;

    /**
     * 最后登陆id
     */
    @TableField(value = "last_connection_ip")
    private String lastConnectionIp;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    public static final String COL_HOST = "host";

    public static final String COL_PORT = "port";

    public static final String COL_PASSWORD = "password";

    public static final String COL_LAST_CONNECTION_TIME = "last_connection_time";

    public static final String COL_LAST_CONNECTION_IP = "last_connection_ip";

    public static final String COL_NAME = "name";
}