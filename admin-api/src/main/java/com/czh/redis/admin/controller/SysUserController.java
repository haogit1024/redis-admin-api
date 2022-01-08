package com.czh.redis.admin.controller;

import com.czh.redis.admin.service.SysUserService;
import com.czh.redis.common.entity.SysUser;
import com.czh.redis.common.mapper.SysUserMapper;
import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.common.view.SysUserView;
import com.czh.redis.framework.annotation.Response;
import com.czh.redis.framework.controller.BaseRestController;
import org.springframework.web.bind.annotation.*;

/**
 * @author czh
 * @date 2020/6/11
 */
@RestController
@RequestMapping("sysUser")
@Response
public class SysUserController extends BaseRestController<SysUserService, SysUserMapper, SysUser, SysUserView> {

    @PostMapping("login")
    public String login(@RequestBody SysUserView vo) {
        String username = vo.getUsername();
        String password = vo.getPassword();
        return getServiceInstance().login(username, password);
    }

    @PostMapping("logout")
    public boolean logout() {
        return getServiceInstance().logout(SpringUtil.getJwtUserId());
    }

    @PutMapping("pwd")
    public boolean updatePwd(Integer id, String oldPwd, String newPwd) {
        return getServiceInstance().updatePwd(id, oldPwd, newPwd);
    }

    @PostMapping("register")
    public Integer register(@RequestBody SysUserView vo) {
        return getServiceInstance().register(vo.getUsername(), vo.getPassword(), vo.getEmail(), vo.getPhone());
    }
}
