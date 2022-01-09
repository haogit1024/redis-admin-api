package com.czh.redis.common.emums;

import java.text.MessageFormat;

/**
 * 响应参数
 */
public enum ResultEnum {
    // 成功
    SUCCESS(1, "成功"),

    // 1001 - 1999 参数错误
    PARAM_ERROR(1001, "参数错误: {0}"),

    // 2001 - 2999 用户错误
    USER_LOGIN_ERROR(2001, "用户名或密码错误"),
    USER_NOT_EXISTED(2002, "用户不存在"),
    USER_HAS_EXISTED(2003, "用户已存在"),
    USER_PASSWORD_NOT_MATCHES(2004, "用户密码不匹配"),

    // 3001 - 3999 系统错误
    SERVER_ERROR(3001, "系统繁忙"),
    EXPORT_DATA_NONE(3002, "没有需要导出的数据"),

    // 4101 - 4100 redis错误
    REDIS_CONNECTION_ERROR(4001, "redis链接失败，{0}"),
    TYPE_NOT_SUPPER(4002, "不支持数据类型，type: {}"),

    // 5001 - 5999 数据库错误
    DATA_NOT_FOUND(5001, "{0}未找到"),
    DATA_SAVE_ERROR(5002, "{0}保存失败"),
    DATA_UPDATE_ERROR(5003, "{0}更新失败"),

    // 6001 - 6999 其他错误
    TOKEN_ERROR(6001, "非法token或token已过期"),
    ;
    private final Integer code;
    private String message;
    private final String template;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.template = message;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getTemplate() {
        return template;
    }

    public ResultEnum format(Object... msgArgs) {
        this.message = MessageFormat.format(template, msgArgs);
        return this;
    }

    public ResultEnum append(String message) {
        this.message = this.template + "," + message;
        return this;
    }
}
