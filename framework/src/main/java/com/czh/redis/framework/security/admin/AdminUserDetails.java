package com.czh.redis.framework.security.admin;

import com.czh.redis.common.entity.SysUser;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author czh
 * @date 2020/6/17
 */
@Setter
public class AdminUserDetails implements UserDetails {
    private Integer id;

    private String username;

    private String password;

    private String roles;

    public AdminUserDetails(SysUser sysUser) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.roles = sysUser.getRoles();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(this.roles);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
