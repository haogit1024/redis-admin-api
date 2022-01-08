package com.czh.redis.admin.service;

import com.czh.redis.common.util.JwtUtil;
import com.czh.redis.common.util.RedisUtil;
import com.czh.redis.common.util.Utils;
import com.czh.redis.common.view.SysUserView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class SysUserServiceTest {
    @Autowired
    private SysUserService service;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;

    private final String username = "admin";
    private final String password = Utils.md5("123456");

    @Test
    void login() {
        String jwt = service.login(username, password);
        Integer userId = jwtUtil.getUserIdFromJwt(jwt);
        assert userId != -1 : "解释用户jwt失败";
        String jwtRedisKey = jwtUtil.getRedisKey(userId);
        assert redisUtil.hasKey(jwtRedisKey) : "缓存jwt失败";
        String cacheJwt = (String) redisUtil.get(jwtRedisKey);
        assert jwt.equals(cacheJwt) : "jwt和缓存不一致";
        SysUserView sysUser = service.get(userId);
        assert sysUser != null : "获取用户信息失败";
    }

    @Test
    void logout() {
        String jwt = service.login(username, password);
        Integer userId = jwtUtil.getUserIdFromJwt(jwt);
        String jwtRedisKey = jwtUtil.getRedisKey(userId);
        redisUtil.del(jwtRedisKey);
    }

    @Test
    void updatePwd() {
        String jwt = service.login(username, password);
        Integer userId = jwtUtil.getUserIdFromJwt(jwt);
        String newPassword = Utils.md5("23456");
        service.updatePwd(userId, password, newPassword);
    }

    @Test
    void register() {
        String newUsername = username + Utils.randomStr(6);
        Integer userId = service.register(newUsername, password, "test@outlook.com", "18888888888");
        assert userId != -1 : "注册失败";
    }
}