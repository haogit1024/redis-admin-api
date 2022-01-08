package com.czh.redis.common.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author czh
 * @date 2020/6/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
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
     * 手机号码
     */
    private String phone;
}
