package com.czh.redis.framework.security.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.redis.common.entity.SysUser;
import com.czh.redis.common.mapper.SysUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * AdminUserDetails服务
 * 需要在 admin 模块注册为 bean
 * @author czh
 * @date 2020/6/17
 */
public class AdminUserDetailsService implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        return new AdminUserDetails(sysUser);
    }

    public AdminUserDetails loadUserById(Integer id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        return new AdminUserDetails(sysUser);
    }
}
