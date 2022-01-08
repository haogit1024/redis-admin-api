package com.czh.redis.common.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author chenzh
 * @date 2020/7/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView extends BaseView {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 邮箱/登录账号
     */
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 最后登陆时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登陆id
     */
    private String lastLoginIp;

    /**
     * 是否启用 0:未启用/1:启用
     */
    private Boolean isEnable;

    /**
     * 盐
     */
    private String salt;

    /**
     * 对密钥的摘要
     */
    private String digest;
}
