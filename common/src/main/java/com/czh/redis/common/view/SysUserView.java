package com.czh.redis.common.view;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author czh
 * @date 2020/6/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserView extends BaseView {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 最后登陆时间
     */
    private Integer lastLoginTime;

    /**
     * 最后登陆id
     */
    private String lastLoginIp;
}
