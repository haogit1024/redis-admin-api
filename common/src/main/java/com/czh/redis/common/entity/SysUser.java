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
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class SysUser extends BaseEntity {
    public static final String COL_DEL_FLAG = "del_flag";
    public static final String COL_ROLES = "roles";
    public static final String COL_DIGEST = "digest";
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 最后登陆时间
     */
    @TableField(value = "last_login_time")
    private Integer lastLoginTime;

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

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_EMAIL = "email";

    public static final String COL_PHONE = "phone";

    public static final String COL_LAST_LOGIN_TIME = "last_login_time";

    public static final String COL_LAST_LOGIN_IP = "last_login_ip";

    public static final String COL_IS_ENABLE = "is_enable";

    public static final String COL_SALT = "salt";
}