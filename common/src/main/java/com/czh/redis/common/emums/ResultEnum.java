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

    // 4001 - 4999 业务错误(备用, 看视具体业务情况要不要用)
    DIGEST_ERROR(4001, "特征计算错误, 请检查secret key是否正确"),
    AES_ENCODE_ERROR(4002, "加密失败"),
    AES_DECODE_ERROR(4003, "解密失败, 请检查密钥是否正确"),
    EXPORT_DATA_NONE(4004, "没有需要导出的数据"),

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
