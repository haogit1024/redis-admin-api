package com.czh.redis.framework.security.app;

import com.czh.redis.common.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author chenzh
 * @date 2020/7/2
 */
@Data
@Slf4j
public class AppUserDetails implements UserDetails {
    private Integer id;

    private String username;

    private String password;

    private String roles;

    private String salt;

    private String digest;

    public AppUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
//        this.roles = user.getRoles();
        this.salt = user.getSalt();
        this.digest = user.getDigest();
        this.roles = "ROLE_USER";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("app user details getAuthorities roles: {}", this.roles);
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
}
