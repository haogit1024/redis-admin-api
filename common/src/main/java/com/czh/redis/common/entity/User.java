package com.czh.redis.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author czh
 * @date 2020/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_user")
public class User extends BaseEntity {
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 邮箱/登录账号
     */
    @TableField(value = "email")
    private String email;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 角色
     */
    @TableField(value = "roles")
    private String roles;

    /**
     * 最后登陆时间
     */
    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登陆id
     */
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    /**
     * 是否启用 0:未启用/1:启用
     */
    @TableField(value = "is_enable")
    private Boolean isEnable;

    /**
     * 盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 对密钥的摘要
     */
    @TableField(value = "digest")
    private String digest;

    public static final String COL_EMAIL = "email";

    public static final String COL_PASSWORD = "password";

    public static final String COL_ROLES = "roles";

    public static final String COL_LAST_LOGIN_TIME = "last_login_time";

    public static final String COL_LAST_LOGIN_IP = "last_login_ip";

    public static final String COL_IS_ENABLE = "is_enable";

    public static final String COL_SALT = "salt";

    public static final String COL_DIGEST = "digest";
}