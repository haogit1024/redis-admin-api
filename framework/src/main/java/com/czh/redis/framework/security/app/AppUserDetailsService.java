package com.czh.redis.framework.security.app;

import com.czh.redis.common.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * AdminUserDetails服务
 * 需要在 app 模块注册为 bean
 * @author chenzh
 * @date 2020/7/2
 */
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public AppUserDetails loadUserById(Integer id) {
        return new AppUserDetails(userMapper.selectById(id));
    }
}
